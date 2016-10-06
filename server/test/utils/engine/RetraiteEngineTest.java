package utils.engine;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static utils.JsonUtils.toJson;
import static utils.TestsUtils.createInfoRetraiteResult;
import static utils.TestsUtils.createInfoRetraiteResultRegime;
import static utils.engine.data.enums.EcranSortie.ECRAN_SORTIE_PENIBILITE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_A;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_B;
import static utils.engine.data.enums.Regime.BFSP;
import static utils.engine.data.enums.Regime.CARPIMKO;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_CHEF;
import static utils.wsinforetraite.InfoRetraiteResult.Status.NOTFOUND;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import controllers.data.PostData;
import models.FakeData;
import utils.RetraiteBadNaissanceFormatException;
import utils.RetraiteException;
import utils.dao.DaoFakeData;
import utils.engine.data.MonthAndYear;
import utils.engine.data.RenderData;
import utils.engine.data.UserChecklist;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;
import utils.engine.utils.AgeCalculator;
import utils.engine.utils.AgeLegalEvaluator;
import utils.wsinforetraite.InfoRetraiteResult;
import utils.wsinforetraite.InfoRetraiteWsUr;

public class RetraiteEngineTest {

	private final InfoRetraiteResult allRegimesInfos = createInfoRetraiteResult(Regime.CNAV, Regime.MSA, Regime.AGIRC_ARRCO);
	private final InfoRetraiteResult regimesInfosAutres = createInfoRetraiteResult(Regime.CARPIMKO, Regime.BFSP);
	private final String allRegimesInfosAsJson = toJson(allRegimesInfos.regimes);
	private final String allRegimesStr = "CNAV,CCMSA,AGIRC_ARRCO";
	private final List<FakeData> fakeDataMock = createFakeDataList();
	private InfoRetraiteWsUr infoRetraiteMock;
	private CalculateurRegimeAlignes calculateurRegimeAlignesMock;
	private UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilderMock;
	private UserChecklistGenerator userChecklistGeneratorMock;
	private AgeCalculator ageCalculatorMock;
	private AgeLegalEvaluator ageLegalEvaluatorMock;
	private DisplayerSortieTropJeune displayerSortieTropJeuneMock;
	private DisplayerLiquidateurQuestions displayerLiquidateurQuestionsMock;
	private DisplayerSortiePenibilite displayerSortiePenibiliteMock;
	private DisplayerSortieDepartInconnu displayerSortieDepartInconnuMock;
	private DisplayerDepartureDate displayerDepartureDateMock;
	private DisplayerAdditionalQuestions displayerAdditionalQuestionsMock;
	private DisplayerChecklist displayerChecklistMock;
	private DisplayerSortieQuestionCarriereLongue displayerSortieQuestionCarriereLongueMock;

	private RetraiteEngine retraiteEngine;
	private final String liquidateurReponseJsonStr = "[\"OUI\"]";

	@Before
	public void setUp() throws Exception {

		infoRetraiteMock = mock(InfoRetraiteWsUr.class);
		when(infoRetraiteMock.retrieveAllInformations("DUPONT", "1 50 12 18 123 456", "1/2/3")).thenReturn(allRegimesInfos);

		userChecklistGeneratorMock = mock(UserChecklistGenerator.class);

		calculateurRegimeAlignesMock = mock(CalculateurRegimeAlignes.class);

		final DaoFakeData daoFakeDataMock = mock(DaoFakeData.class);
		when(daoFakeDataMock.findAll()).thenReturn(fakeDataMock);

		userChecklistGenerationDataBuilderMock = mock(UserChecklistGenerationDataBuilder.class);

		ageCalculatorMock = mock(AgeCalculator.class);
		when(ageCalculatorMock.getAge("1/2/3")).thenReturn(62);

		ageLegalEvaluatorMock = mock(AgeLegalEvaluator.class);
		when(ageLegalEvaluatorMock.isAgeLegal("1/2/3", "11", "2017")).thenReturn(true);

		displayerSortieTropJeuneMock = mock(DisplayerSortieTropJeune.class);
		displayerLiquidateurQuestionsMock = mock(DisplayerLiquidateurQuestions.class);
		displayerSortiePenibiliteMock = mock(DisplayerSortiePenibilite.class);
		displayerSortieDepartInconnuMock = mock(DisplayerSortieDepartInconnu.class);
		displayerDepartureDateMock = mock(DisplayerDepartureDate.class);
		displayerAdditionalQuestionsMock = mock(DisplayerAdditionalQuestions.class);
		displayerChecklistMock = mock(DisplayerChecklist.class);
		displayerSortieQuestionCarriereLongueMock = mock(DisplayerSortieQuestionCarriereLongue.class);

		retraiteEngine = new RetraiteEngine(infoRetraiteMock, calculateurRegimeAlignesMock, daoFakeDataMock,
				ageCalculatorMock, ageLegalEvaluatorMock, displayerLiquidateurQuestionsMock, displayerDepartureDateMock, displayerAdditionalQuestionsMock,
				displayerChecklistMock, displayerSortieQuestionCarriereLongueMock, displayerSortieDepartInconnuMock, displayerSortiePenibiliteMock,
				displayerSortieTropJeuneMock);
	}

	@After
	public void assertNoMoreInteractionsOnMocks() {
		verifyNoMoreInteractions(displayerLiquidateurQuestionsMock, displayerDepartureDateMock, displayerAdditionalQuestionsMock, displayerChecklistMock,
				displayerSortieQuestionCarriereLongueMock, displayerSortieDepartInconnuMock, displayerSortiePenibiliteMock, displayerSortieTropJeuneMock);
	}

	@Test
	public void error_if_step_inconnu() {

		final PostData postData = new PostData();
		postData.hidden_step = "step-inconnu";

		try {
			retraiteEngine.processToNextStep(postData);
			fail("Devrait déclencher une exception");
		} catch (final RetraiteException e) {
			assertThat(e.getMessage()).isEqualTo("Situation anormale : l'étape 'step-inconnu' pour le traitement");
		}
	}

	@Test
	public void step_display_welcome() {

		// Step : (null) --> welcome

		final PostData postData = new PostData();

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("welcome");
	}

	@Test
	public void step_display_getUserData() {

		// Step : welcome --> getUserData

		final PostData postData = new PostData();
		postData.hidden_step = "welcome";

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("getUserData");
	}

	@Test
	public void step_display_getUserData_with_error_if_user_not_found() {

		// Step : getUserData --> getUserData

		final PostData postData = new PostData();
		postData.hidden_step = "getUserData";
		postData.nom = "DUPONT";
		postData.naissance = "1/2/3";
		postData.nir = "1 50 12 18 123 456";
		postData.departement = "65";

		// Pour ce test uniquement, on change le mock pour ne rien renvoyer
		final InfoRetraiteResult infoRetraiteResult = new InfoRetraiteResult(NOTFOUND, null);
		when(infoRetraiteMock.retrieveAllInformations("DUPONT", "1 50 12 18 123 456", "1/2/3")).thenReturn(infoRetraiteResult);

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("getUserData");
		assertThat(renderData.errorMessage).startsWith("Désolé");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
	}

	@Test
	public void step_display_getUserData_with_error_if_error_in_date_naissance() {

		// Step : getUserData --> getUserData

		final PostData postData = new PostData();
		postData.hidden_step = "getUserData";
		postData.nom = "DUPONT";
		postData.naissance = "00/00/1952";
		postData.nir = "1 50 12 18 123 456";
		postData.departement = "65";

		doThrow(new RetraiteBadNaissanceFormatException("")).when(ageCalculatorMock).getAge("00/00/1952");

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("getUserData");
		assertThat(renderData.errorMessage).startsWith("Désolé");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("00/00/1952");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("65");
	}

	@Test
	public void step_display_sortie_age_trop_jeune() {

		// Step : getUserData --> displaySortieTropJeune

		final PostData postData = new PostData();
		postData.hidden_step = "getUserData";
		postData.nom = "DUPONT";
		postData.naissance = "1/2/3";
		postData.nir = "1 50 12 18 123 456";
		postData.departement = "65";

		when(ageCalculatorMock.getAge("1/2/3")).thenReturn(54);

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerSortieTropJeuneMock).fillData(isA(PostData.class), isA(RenderData.class));
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("65");
	}

	@Test
	public void step_display_departure_date_si_age_trop_jeune_mais_option_force55() {

		// Step : getUserData --> displayDepartureDate

		final PostData postData = new PostData();
		postData.hidden_step = "getUserData";
		postData.nom = "DUPONT";
		postData.naissance = "1/2/3";
		postData.nir = "1 50 12 18 123 456";
		postData.departement = "65";
		postData.isForce55 = true;

		when(ageCalculatorMock.getAge("1/2/3")).thenReturn(54);
		when(calculateurRegimeAlignesMock.getRegimesAlignes(anyString())).thenReturn(new RegimeAligne[] { CNAV });

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerDepartureDateMock).fillData(isA(PostData.class), isA(RenderData.class), eq(allRegimesStr));
		assertThat(renderData.hidden_liquidateur).isEqualTo(CNAV);
	}

	@Test
	public void step_display_sortie_aucun_regime_de_base_aligne() {

		// Step : getUserData --> displaySortieAucunRegimeDeBaseAligne

		final PostData postData = new PostData();
		postData.hidden_step = "getUserData";
		postData.nom = "DUPONT";
		postData.naissance = "1/2/3";
		postData.nir = "1 50 12 18 123 456";
		postData.departement = "65";

		when(infoRetraiteMock.retrieveAllInformations("DUPONT", "1 50 12 18 123 456", "1/2/3")).thenReturn(regimesInfosAutres);
		when(calculateurRegimeAlignesMock.getRegimesAlignes(anyString())).thenReturn(new RegimeAligne[] {});

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("displaySortieAucunRegimeDeBaseAligne");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("65");
		assertThat(renderData.regimesInfosAucunRegimeDeBaseAligne)
				.containsExactly(createInfoRetraiteResultRegime(CARPIMKO), createInfoRetraiteResultRegime(BFSP));
	}

	@Test
	public void step_display_departure_date_without_questions() {

		// Step : getUserData --> displayDepartureDate

		final PostData postData = new PostData();
		postData.hidden_step = "getUserData";
		postData.nom = "DUPONT";
		postData.naissance = "1/2/3";
		postData.nir = "1 50 12 18 123 456";
		postData.departement = "65";

		when(calculateurRegimeAlignesMock.getRegimesAlignes(anyString())).thenReturn(new RegimeAligne[] { CNAV });

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerDepartureDateMock).fillData(isA(PostData.class), isA(RenderData.class), eq(allRegimesStr));
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("65");
		assertThat(renderData.hidden_regimes).isEqualTo(allRegimesStr);
		assertThat(renderData.hidden_regimesInfosJsonStr).isEqualTo(allRegimesInfosAsJson);
		assertThat(renderData.hidden_liquidateur).isEqualTo(CNAV);
	}

	@Test
	public void step_display_liquidateur_questions_si_2_regimes() {

		// Step : getUserData --> displayLiquidateurQuestions

		final PostData postData = new PostData();
		postData.hidden_step = "getUserData";
		postData.nom = "DUPONT";
		postData.naissance = "1/2/3";
		postData.nir = "1 50 12 18 123 456";
		postData.departement = "65";

		when(calculateurRegimeAlignesMock.getRegimesAlignes(anyString())).thenReturn(new RegimeAligne[] { CNAV, MSA });

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerLiquidateurQuestionsMock).fillData(isA(PostData.class), isA(RenderData.class), isA(RegimeAligne[].class));
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("65");
		assertThat(renderData.hidden_regimes).isEqualTo(allRegimesStr);
		assertThat(renderData.hidden_regimesInfosJsonStr).isEqualTo(allRegimesInfosAsJson);
	}

	@Test
	public void step_display_liquidateur_questions_si_3_regimes() {

		// Step : getUserData --> displayLiquidateurQuestions

		final PostData postData = new PostData();
		postData.hidden_step = "getUserData";
		postData.nom = "DUPONT";
		postData.naissance = "1/2/3";
		postData.nir = "1 50 12 18 123 456";

		when(calculateurRegimeAlignesMock.getRegimesAlignes(anyString())).thenReturn(new RegimeAligne[] { CNAV, MSA, RSI });

		retraiteEngine.processToNextStep(postData);

		verify(displayerLiquidateurQuestionsMock).fillData(isA(PostData.class), isA(RenderData.class), isA(RegimeAligne[].class));
	}

	@Test
	public void step_display_liquidateur_questions_next_step() {

		// Step : displayLiquidateurQuestions --> displayLiquidateurQuestions

		final PostData postData = new PostData();
		postData.hidden_step = "displayLiquidateurQuestions";
		postData.hidden_liquidateurStep = null;
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_regimes = "d,e";
		postData.hidden_departement = "987";
		postData.hidden_liquidateur = RSI;
		postData.liquidateurReponseJsonStr = liquidateurReponseJsonStr;

		when(calculateurRegimeAlignesMock.getRegimesAlignes(anyString())).thenReturn(new RegimeAligne[] { CNAV, MSA, RSI });

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final RenderData renderData = invocation.getArgumentAt(1, RenderData.class);
				renderData.hidden_liquidateurStep = QUESTION_A;
				return null;
			}
		}).when(displayerLiquidateurQuestionsMock).fillData(any(PostData.class), any(RenderData.class), any(RegimeAligne[].class));

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerLiquidateurQuestionsMock).fillData(isA(PostData.class), isA(RenderData.class), isA(RegimeAligne[].class));
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("987");
		assertThat(renderData.hidden_regimes).isEqualTo("d,e");
		assertThat(renderData.hidden_liquidateur).isEqualTo(RSI);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_A);
		assertThat(renderData.hidden_liquidateurReponseJsonStr).isEqualTo(liquidateurReponseJsonStr);
	}

	@Test
	public void step_display_ecran_sortie_penibilite() {

		// Step : displayLiquidateurQuestions --> displaySortiePenibilite

		final PostData postData = new PostData();
		postData.hidden_step = "displayLiquidateurQuestions";
		postData.hidden_liquidateurStep = QUESTION_B;
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_regimes = "d,e";
		postData.liquidateurReponseJsonStr = liquidateurReponseJsonStr;

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final RenderData renderData = invocation.getArgumentAt(1, RenderData.class);
				renderData.ecranSortie = ECRAN_SORTIE_PENIBILITE;
				return null;
			}
		}).when(displayerLiquidateurQuestionsMock).fillData(any(PostData.class), any(RenderData.class), any(RegimeAligne[].class));

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerLiquidateurQuestionsMock).fillData(isA(PostData.class), isA(RenderData.class), isNull(RegimeAligne[].class));
		verify(displayerSortiePenibiliteMock).fillData(isA(PostData.class), isA(RenderData.class));
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_regimes).isEqualTo("d,e");
		assertThat(renderData.hidden_liquidateurReponseJsonStr).isEqualTo(liquidateurReponseJsonStr);
	}

	@Test
	public void step_display_departure_date() {

		// Step : displayLiquidateurQuestions --> displayDepartureDate

		final PostData postData = new PostData();
		postData.hidden_step = "displayLiquidateurQuestions";
		postData.hidden_liquidateurStep = QUESTION_B;
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_regimes = "d,e";
		postData.liquidateurReponseJsonStr = liquidateurReponseJsonStr;

		// displayerLiquidateurQuestions.fillData() doit laisser
		// renderData.hidden_liquidateurStep = null pour indiquer qu'il n'y a plus de questions

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerLiquidateurQuestionsMock).fillData(isA(PostData.class), isA(RenderData.class), isNull(RegimeAligne[].class));
		verify(displayerDepartureDateMock).fillData(isA(PostData.class), isA(RenderData.class), (String) isNull());
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_regimes).isEqualTo("d,e");
		assertThat(renderData.hidden_liquidateurReponseJsonStr).isEqualTo(liquidateurReponseJsonStr);
	}

	@Test
	public void step_display_sortie_depart_inconnu() {

		// Step : displayDepartureDate --> displaySortieDepartInconnu

		final PostData postData = new PostData();
		postData.hidden_step = "displayDepartureDate";
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_regimes = "a,b,c";
		postData.hidden_liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		postData.departInconnu = true;

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerSortieDepartInconnuMock).fillData(isA(PostData.class), isA(RenderData.class));
		assertThat(renderData.hidden_step).isEqualTo("displaySortieDepartInconnu");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_regimes).isEqualTo("a,b,c");
		assertThat(renderData.hidden_liquidateurReponseJsonStr).isEqualTo(liquidateurReponseJsonStr);
	}

	@Test
	public void step_display_age_trop_jeune_question_carriere_longue() {

		// Step : displayDepartureDate --> displaySortieQuestionCarriereLongue

		final PostData postData = new PostData();
		postData.hidden_step = "displayDepartureDate";
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_regimes = "a,b,c";
		postData.hidden_liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		postData.departMois = "11";
		postData.departAnnee = "2017";
		postData.departInconnu = false;

		when(ageLegalEvaluatorMock.isAgeLegal("1/2/3", "11", "2017")).thenReturn(false);

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerSortieQuestionCarriereLongueMock).fillData(isA(PostData.class), isA(RenderData.class));
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_regimes).isEqualTo("a,b,c");
		assertThat(renderData.hidden_departMois).isEqualTo("11");
		assertThat(renderData.hidden_departAnnee).isEqualTo("2017");
		assertThat(renderData.hidden_liquidateurReponseJsonStr).isEqualTo(liquidateurReponseJsonStr);
	}

	@Test
	public void step_display_additionnal_questions_after_carriere_longue() {

		// Step : displaySortieQuestionCarriereLongue --> displayAdditionalQuestions

		final PostData postData = new PostData();
		postData.hidden_step = "displaySortieQuestionCarriereLongue";
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_regimes = "a,b,c";
		postData.hidden_liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		postData.hidden_departMois = "11";
		postData.hidden_departAnnee = "2017";

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerAdditionalQuestionsMock).fillData(isA(PostData.class), isA(RenderData.class));
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_regimes).isEqualTo("a,b,c");
		assertThat(renderData.hidden_departMois).isEqualTo("11");
		assertThat(renderData.hidden_departAnnee).isEqualTo("2017");
		assertThat(renderData.hidden_liquidateurReponseJsonStr).isEqualTo(liquidateurReponseJsonStr);
		assertThat(renderData.hidden_attestationCarriereLongue).isTrue();
	}

	@Test
	public void step_display_additionnal_questions() {

		// Step : displayDepartureDate --> displayAdditionalQuestions

		final PostData postData = new PostData();
		postData.hidden_step = "displayDepartureDate";
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_regimes = "a,b,c";
		postData.hidden_liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		postData.departMois = "11";
		postData.departAnnee = "2017";
		postData.departInconnu = false;

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerAdditionalQuestionsMock).fillData(isA(PostData.class), isA(RenderData.class));
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_regimes).isEqualTo("a,b,c");
		assertThat(renderData.hidden_departMois).isEqualTo("11");
		assertThat(renderData.hidden_departAnnee).isEqualTo("2017");
		assertThat(renderData.hidden_liquidateurReponseJsonStr).isEqualTo(liquidateurReponseJsonStr);
	}

	@Test
	public void step_display_checklist() {

		// Step : displayAdditionalQuestions --> displayCheckList

		final PostData postData = new PostData();
		postData.hidden_step = "displayAdditionalQuestions";
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_regimes = "CNAV";
		postData.hidden_liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		postData.hidden_departMois = "11";
		postData.hidden_departAnnee = "2017";
		postData.hidden_attestationCarriereLongue = true;
		postData.departement = "987";

		// Temp
		postData.departMois = "11";
		postData.departAnnee = "2017";

		final MonthAndYear dateDepart = new MonthAndYear("11", "2017");
		final Regime[] regimes = new Regime[] { Regime.CNAV };
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };
		final UserChecklist userChecklistMock = createUserChecklist();
		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(dateDepart, "987", regimes, regimesAlignes,
				true, false, "[{\"nom\":\"CNAV\"}");
		final RegimeAligne regimeLiquidateur = CNAV;
		final List<UserStatus> userStatus = asList(STATUS_CHEF);

		when(calculateurRegimeAlignesMock.getRegimesAlignes("CNAV")).thenReturn(regimesAlignes);
		when(userChecklistGenerationDataBuilderMock.build(eq(dateDepart), eq("987"), eq(regimes), eq(regimesAlignes), eq(regimeLiquidateur),
				eq(true), eq(true), eq(userStatus), eq(false), eq(""))).thenReturn(userChecklistGenerationData);
		when(userChecklistGeneratorMock.generate(same(userChecklistGenerationData), eq(regimeLiquidateur))).thenReturn(userChecklistMock);

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerChecklistMock).fillData(isA(PostData.class), isA(RenderData.class));
		assertThat(renderData.hidden_step).isNull();
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("987");
		assertThat(renderData.hidden_regimes).isEqualTo("CNAV");
		assertThat(renderData.hidden_liquidateurReponseJsonStr).isEqualTo(postData.hidden_liquidateurReponseJsonStr);
		assertThat(renderData.hidden_departMois).isEqualTo("11");
		assertThat(renderData.hidden_departAnnee).isEqualTo("2017");
		assertThat(renderData.hidden_attestationCarriereLongue).isTrue();
	}

	// Méthodes privées

	static UserChecklist createUserChecklist() {
		return new UserChecklist();
	}

	private List<FakeData> createFakeDataList() {
		return asList(new FakeData(), new FakeData());
	}

}
