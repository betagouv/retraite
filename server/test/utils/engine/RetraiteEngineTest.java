package utils.engine;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.JsonUtils.toJson;
import static utils.engine.data.enums.ComplementQuestionDescriptor.ACCORD_INFO_RELEVE_CARRIERE;
import static utils.engine.data.enums.ComplementQuestionDescriptor.CONSULT_RELEVE_CARRIERE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.CHEF_EXPLOITATION_AGRICOLE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import models.FakeData;
import utils.RetraiteException;
import utils.dao.DaoFakeData;
import utils.engine.data.ComplementReponses;
import utils.engine.data.Departement;
import utils.engine.data.LiquidateurReponses;
import utils.engine.data.MonthAndYear;
import utils.engine.data.QuestionComplementaire;
import utils.engine.data.QuestionLiquidateur;
import utils.engine.data.RenderData;
import utils.engine.data.UserChecklist;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.ValueAndText;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.QuestionComplementairesEvaluator;
import utils.engine.intern.QuestionsLiquidateurBuilder;
import utils.engine.intern.StepFormsDataProvider;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;
import utils.engine.utils.AgeCalculator;
import utils.engine.utils.AgeLegalEvaluator;
import utils.wsinforetraite.InfoRetraiteReal;
import utils.wsinforetraite.InfoRetraiteResult;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;
import utils.wsinforetraite.InfoRetraiteResult.Status;

public class RetraiteEngineTest {

	private final List<Departement> departementsMock = createListeDepartements();
	private final String allRegimes = "CNAV,CCMSA,AGIRC ARRCO";
	private final List<ValueAndText> listeMoisAvecPremierMock = asList(vet("1", "1er Janvier"), vet("12", "1er Décembre"));
	private final List<String> listeAnneesDepartMock = asList("2015", "2016");
	private final List<QuestionLiquidateur> questionsLiquidateur = createQuestionsLiquidateur();
	private final List<QuestionComplementaire> questionsComplementaire = createQuestionsComplementaires();
	private final LiquidateurReponses liquidateurReponse = createLiquidateurReponses();
	private final String liquidateurReponseJsonStr = toJson(liquidateurReponse.getReponses());
	private final ComplementReponses complementReponse = createComplementReponses();
	private final String complementReponseJsonStr = toJson(complementReponse.getReponses());
	private final List<FakeData> fakeDataMock = createFakeDataList();
	private InfoRetraiteReal infoRetraiteMock;
	private CalculateurRegimeAlignes calculateurRegimeAlignesMock;
	private UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilderMock;
	private UserChecklistGenerator userChecklistGeneratorMock;
	private QuestionComplementairesEvaluator questionComplementairesEvaluatorMock;
	private AgeCalculator ageCalculatorMock;
	private AgeLegalEvaluator ageLegalEvaluatorMock;
	private DisplayerAdditionalQuestions displayerAdditionalQuestions;
	private DisplayerChecklist displayerChecklist;

	private RetraiteEngine retraiteEngine;

	@Before
	public void setUp() throws Exception {
		final StepFormsDataProvider departementsProvider = mock(StepFormsDataProvider.class);
		when(departementsProvider.getListDepartements()).thenReturn(departementsMock);
		when(departementsProvider.getListMoisAvecPremier()).thenReturn(listeMoisAvecPremierMock);
		when(departementsProvider.getListAnneesDepart()).thenReturn(listeAnneesDepartMock);

		infoRetraiteMock = mock(InfoRetraiteReal.class);
		when(infoRetraiteMock.retrieveRegimes("DUPONT", "1 50 12 18 123 456", "1/2/3")).thenReturn(allRegimes);

		userChecklistGeneratorMock = mock(UserChecklistGenerator.class);

		calculateurRegimeAlignesMock = mock(CalculateurRegimeAlignes.class);

		final QuestionsLiquidateurBuilder questionsLiquidateurBuilderMock = mock(QuestionsLiquidateurBuilder.class);
		when(questionsLiquidateurBuilderMock.buildQuestions(new RegimeAligne[] { CNAV, MSA })).thenReturn(questionsLiquidateur);

		final DaoFakeData daoFakeDataMock = mock(DaoFakeData.class);
		when(daoFakeDataMock.findAll()).thenReturn(fakeDataMock);

		userChecklistGenerationDataBuilderMock = mock(UserChecklistGenerationDataBuilder.class);

		questionComplementairesEvaluatorMock = mock(QuestionComplementairesEvaluator.class);

		ageCalculatorMock = mock(AgeCalculator.class);
		when(ageCalculatorMock.getAge("1/2/3")).thenReturn(62);

		ageLegalEvaluatorMock = mock(AgeLegalEvaluator.class);
		when(ageLegalEvaluatorMock.isAgeLegal("1/2/3", "11", "2017")).thenReturn(true);

		displayerAdditionalQuestions = mock(DisplayerAdditionalQuestions.class);
		displayerChecklist = mock(DisplayerChecklist.class);

		retraiteEngine = new RetraiteEngine(departementsProvider, infoRetraiteMock, calculateurRegimeAlignesMock,
				questionsLiquidateurBuilderMock, daoFakeDataMock, ageCalculatorMock, ageLegalEvaluatorMock, displayerChecklist,
				displayerAdditionalQuestions);
	}

	@Test
	public void error_if_no_step() {
		try {
			retraiteEngine.processToNextStep(new PostData());
			fail("Devrait déclencher une exception");
		} catch (final RetraiteException e) {
			assertThat(e.getMessage()).isEqualTo("Situation anormale : pas d'étape (step) pour le traitement");
		}
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

		final RenderData renderData = retraiteEngine.processToNextStep(null);

		assertThat(renderData.hidden_step).isEqualTo("welcome");
	}

	@Test
	public void step_display_getUserData() {

		// Step : welcome --> getUserData

		final PostData postData = new PostData();
		postData.hidden_step = "welcome";

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("getUserData");
		assertThat(renderData.departements).isSameAs(departementsMock);
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
		when(infoRetraiteMock.retrieveRegimes("DUPONT", "1 50 12 18 123 456", "1/2/3")).thenReturn("");

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("getUserData");
		assertThat(renderData.departements).isSameAs(departementsMock);
		assertThat(renderData.errorMessage).startsWith("Désolé");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
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

		assertThat(renderData.hidden_step).isEqualTo("displaySortieTropJeune");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("65");
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

		final InfoRetraiteResultRegime infoRegimeCarpimko = createInfoRetraiteResultRegime("CARPIMKO");
		final InfoRetraiteResult infoRetraiteResult = new InfoRetraiteResult(Status.FOUND, new InfoRetraiteResultRegime[] {
				infoRegimeCarpimko
		});
		when(infoRetraiteMock.retrieveAllInformations("DUPONT", "1 50 12 18 123 456", "1/2/3")).thenReturn(infoRetraiteResult);
		when(calculateurRegimeAlignesMock.getRegimesAlignes(anyString())).thenReturn(new RegimeAligne[] {});

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("displaySortieAucunRegimeDeBaseAligne");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("65");
		assertThat(renderData.regimesInfos).containsExactly(infoRegimeCarpimko);
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

		assertThat(renderData.hidden_step).isEqualTo("displayDepartureDate");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("65");
		assertThat(renderData.hidden_regimes).isEqualTo(allRegimes);
		assertThat(renderData.listeMoisAvecPremier).isSameAs(listeMoisAvecPremierMock);
		assertThat(renderData.listeAnneesDepart).isSameAs(listeAnneesDepartMock);
	}

	@Test
	public void step_display_liquidateur_questions() {

		// Step : getUserData --> displayLiquidateurQuestions

		final PostData postData = new PostData();
		postData.hidden_step = "getUserData";
		postData.nom = "DUPONT";
		postData.naissance = "1/2/3";
		postData.nir = "1 50 12 18 123 456";
		postData.departement = "65";

		when(calculateurRegimeAlignesMock.getRegimesAlignes(anyString())).thenReturn(new RegimeAligne[] { CNAV, MSA });

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("displayLiquidateurQuestions");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("65");
		assertThat(renderData.hidden_regimes).isEqualTo(allRegimes);
		assertThat(renderData.questionsLiquidateur).isSameAs(questionsLiquidateur);
	}

	@Test
	public void step_display_departure_date() {

		// Step : displayLiquidateurQuestions --> displayDepartureDate

		final PostData postData = new PostData();
		postData.hidden_step = "displayLiquidateurQuestions";
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_regimes = "d,e";
		postData.hidden_departement = "973";
		postData.liquidateurReponseJsonStr = liquidateurReponseJsonStr;

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("displayDepartureDate");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("973");
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
		postData.hidden_departement = "973";
		postData.hidden_regimes = "a,b,c";
		postData.hidden_liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		postData.departInconnu = true;

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("displaySortieDepartInconnu");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("973");
		assertThat(renderData.hidden_regimes).isEqualTo("a,b,c");
		assertThat(renderData.hidden_liquidateurReponseJsonStr).isEqualTo(liquidateurReponseJsonStr);
	}

	@Test
	public void step_display_age_trop_jeune_question_carriere_longue() {

		// Step : displayDepartureDate --> displayQuestionCarriereLongue

		final PostData postData = new PostData();
		postData.hidden_step = "displayDepartureDate";
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_departement = "973";
		postData.hidden_regimes = "a,b,c";
		postData.hidden_liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		postData.departMois = "11";
		postData.departAnnee = "2017";
		postData.departInconnu = false;

		when(ageLegalEvaluatorMock.isAgeLegal("1/2/3", "11", "2017")).thenReturn(false);

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		assertThat(renderData.hidden_step).isEqualTo("displayQuestionCarriereLongue");
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("973");
		assertThat(renderData.hidden_regimes).isEqualTo("a,b,c");
		assertThat(renderData.hidden_departMois).isEqualTo("11");
		assertThat(renderData.hidden_departAnnee).isEqualTo("2017");
		assertThat(renderData.hidden_liquidateurReponseJsonStr).isEqualTo(liquidateurReponseJsonStr);
	}

	@Test
	public void step_display_additionnal_questions_after_carriere_longue() {

		// Step : displayQuestionCarriereLongue --> displayAdditionalQuestions

		final PostData postData = new PostData();
		postData.hidden_step = "displayQuestionCarriereLongue";
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_departement = "973";
		postData.hidden_regimes = "a,b,c";
		postData.hidden_liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		postData.hidden_departMois = "11";
		postData.hidden_departAnnee = "2017";

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerAdditionalQuestions).fillData(isA(PostData.class), isA(RenderData.class));
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("973");
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
		postData.hidden_departement = "973";
		postData.hidden_regimes = "a,b,c";
		postData.hidden_liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		postData.departMois = "11";
		postData.departAnnee = "2017";
		postData.departInconnu = false;

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerAdditionalQuestions).fillData(isA(PostData.class), isA(RenderData.class));
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("973");
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
		postData.hidden_departement = "973";
		postData.hidden_regimes = "CNAV";
		postData.hidden_liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		postData.hidden_departMois = "11";
		postData.hidden_departAnnee = "2017";
		postData.hidden_attestationCarriereLongue = true;
		postData.complementReponseJsonStr = complementReponseJsonStr;

		final MonthAndYear dateDepart = new MonthAndYear("11", "2017");
		final Regime[] regimes = new Regime[] { Regime.CNAV };
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };
		final UserChecklist userChecklistMock = createUserChecklist();
		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(dateDepart, "973", regimes, regimesAlignes,
				false, true);

		when(calculateurRegimeAlignesMock.getRegimesAlignes("CNAV")).thenReturn(regimesAlignes);
		when(questionComplementairesEvaluatorMock.isParcoursDemat(complementReponse)).thenReturn(true);
		when(userChecklistGenerationDataBuilderMock.build(eq(dateDepart), eq("973"), eq(regimes), eq(regimesAlignes), eq(liquidateurReponse),
				eq(complementReponse), eq(true), eq(true), eq(true))).thenReturn(userChecklistGenerationData);
		when(userChecklistGeneratorMock.generate(same(userChecklistGenerationData), eq(liquidateurReponse))).thenReturn(userChecklistMock);

		final RenderData renderData = retraiteEngine.processToNextStep(postData);

		verify(displayerChecklist).fillData(isA(PostData.class), isA(RenderData.class));
		assertThat(renderData.hidden_step).isNull();
		assertThat(renderData.hidden_nom).isEqualTo("DUPONT");
		assertThat(renderData.hidden_naissance).isEqualTo("1/2/3");
		assertThat(renderData.hidden_nir).isEqualTo("1 50 12 18 123 456");
		assertThat(renderData.hidden_departement).isEqualTo("973");
		assertThat(renderData.hidden_regimes).isEqualTo("CNAV");
		assertThat(renderData.hidden_liquidateurReponseJsonStr).isEqualTo(postData.hidden_liquidateurReponseJsonStr);
		assertThat(renderData.hidden_complementReponseJsonStr).isEqualTo(complementReponseJsonStr);
		assertThat(renderData.hidden_departMois).isEqualTo("11");
		assertThat(renderData.hidden_departAnnee).isEqualTo("2017");
		assertThat(renderData.hidden_attestationCarriereLongue).isTrue();
	}

	// Méthodes privées

	private List<Departement> createListeDepartements() {
		final List<Departement> departements = new ArrayList<>();
		departements.add(new Departement("01", "Ain"));
		departements.add(new Departement("02", "Aisne"));
		return departements;
	}

	static UserChecklist createUserChecklist() {
		return new UserChecklist();
	}

	private List<QuestionLiquidateur> createQuestionsLiquidateur() {
		final ArrayList<QuestionLiquidateur> questions = new ArrayList<>();
		questions.add(new QuestionLiquidateur());
		questions.add(new QuestionLiquidateur());
		return questions;
	}

	static LiquidateurReponses createLiquidateurReponses() {
		final LiquidateurReponses liquidateurReponses = new LiquidateurReponses();
		liquidateurReponses.getReponses().put(CHEF_EXPLOITATION_AGRICOLE, asList(NON));
		liquidateurReponses.getReponses().put(DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE, asList(CONJOINT));
		return liquidateurReponses;
	}

	static ComplementReponses createComplementReponses() {
		final ComplementReponses complementReponses = new ComplementReponses();
		complementReponses.getReponses().put(CONSULT_RELEVE_CARRIERE, asList(NON));
		complementReponses.getReponses().put(ACCORD_INFO_RELEVE_CARRIERE, asList(NON));
		return complementReponses;
	}

	static List<QuestionComplementaire> createQuestionsComplementaires() {
		final ArrayList<QuestionComplementaire> questions = new ArrayList<>();
		questions.add(new QuestionComplementaire());
		questions.add(new QuestionComplementaire());
		return questions;
	}

	private List<FakeData> createFakeDataList() {
		return asList(new FakeData(), new FakeData());
	}

	private ValueAndText vet(final String value, final String text) {
		return new ValueAndText(value, text);
	}

	private InfoRetraiteResultRegime createInfoRetraiteResultRegime(final String nom) {
		final InfoRetraiteResultRegime infoRetraiteResultRegime = new InfoRetraiteResultRegime();
		infoRetraiteResultRegime.nom = nom;
		return infoRetraiteResultRegime;
	}

}
