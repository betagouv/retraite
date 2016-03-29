package utils.engine;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.JsonUtils.toJson;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.UserStatus.STATUS_CHEF;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.RetraiteException;
import utils.engine.data.ComplementReponses;
import utils.engine.data.MonthAndYear;
import utils.engine.data.RenderData;
import utils.engine.data.UserChecklist;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.QuestionComplementairesEvaluator;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;
import utils.engine.utils.DateProvider;

public class DisplayerChecklistTest {

	private QuestionComplementairesEvaluator questionComplementairesEvaluatorMock;
	private UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilderMock;
	private UserChecklistGenerator userChecklistGeneratorMock;
	private CalculateurRegimeAlignes calculateurRegimeAlignesMock;

	private DisplayerChecklist displayerChecklist;

	@Before
	public void setUp() throws Exception {

		questionComplementairesEvaluatorMock = mock(QuestionComplementairesEvaluator.class);
		userChecklistGenerationDataBuilderMock = mock(UserChecklistGenerationDataBuilder.class);
		userChecklistGeneratorMock = mock(UserChecklistGenerator.class);
		final DateProvider dateProviderFake = new DateProviderFake(23, 12, 2015);
		calculateurRegimeAlignesMock = mock(CalculateurRegimeAlignes.class);
		displayerChecklist = new DisplayerChecklist(questionComplementairesEvaluatorMock, userChecklistGenerationDataBuilderMock, userChecklistGeneratorMock,
				dateProviderFake, calculateurRegimeAlignesMock);
	}

	@Test
	public void step_display_checklist() {

		final ComplementReponses complementReponse = RetraiteEngineTest.createComplementReponses();
		final String complementReponseJsonStr = toJson(complementReponse.getReponses());
		final UserChecklist userChecklistMock = RetraiteEngineTest.createUserChecklist();

		final PostData postData = new PostData();
		postData.hidden_step = "displayAdditionalQuestions";
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_departement = "973";
		postData.hidden_regimes = "CNAV";
		postData.hidden_liquidateurReponseJsonStr = null;
		postData.hidden_departMois = "2";
		postData.hidden_departAnnee = "2017";
		postData.hidden_attestationCarriereLongue = true;
		postData.complementReponseJsonStr = complementReponseJsonStr;
		postData.hidden_liquidateur = CNAV;

		final MonthAndYear dateDepart = new MonthAndYear("2", "2017");
		final Regime[] regimes = new Regime[] { Regime.CNAV };
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };
		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(dateDepart, "973", regimes, regimesAlignes,
				false, true);
		final List<UserStatus> userStatus = asList(STATUS_CHEF);

		when(calculateurRegimeAlignesMock.getRegimesAlignes("CNAV")).thenReturn(regimesAlignes);
		when(questionComplementairesEvaluatorMock.isParcoursDemat(complementReponse)).thenReturn(true);
		when(userChecklistGenerationDataBuilderMock.build(eq(dateDepart), eq("973"), eq(regimes), eq(regimesAlignes), eq(postData.hidden_liquidateur),
				eq(complementReponse), eq(true), eq(true), eq(true), eq(userStatus))).thenReturn(userChecklistGenerationData);
		when(userChecklistGeneratorMock.generate(same(userChecklistGenerationData), eq(postData.hidden_liquidateur))).thenReturn(userChecklistMock);
		final RenderData renderData = new RenderData();

		displayerChecklist.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displayCheckList");
		assertThat(renderData.hidden_complementReponseJsonStr).isEqualTo(complementReponseJsonStr);
		assertThat(renderData.userChecklist).isSameAs(userChecklistMock);

		final Map<String, String> expectedUserInfos = new HashMap<String, String>() {
			{
				put("Document produit le", "23/12/2015");
				put("Nom de naissance", "DUPONT");
				put("Date de départ envisagée", "01/02/2017");
			}
		};
		assertThat(renderData.userInfos).isEqualTo(expectedUserInfos);
	}

	@Test
	public void step_display_checklist_error() {

		// Step : displayAdditionalQuestions --> displayCheckList

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create().get();
		final List<UserStatus> userStatus = asList(STATUS_CHEF);

		when(calculateurRegimeAlignesMock.getRegimesAlignes(anyString())).thenReturn(new RegimeAligne[] { CNAV });
		when(userChecklistGenerationDataBuilderMock.build(any(MonthAndYear.class), any(String.class), any(Regime[].class), any(RegimeAligne[].class),
				any(RegimeAligne.class), any(ComplementReponses.class), eq(false), eq(true), eq(true), eq(userStatus))).thenReturn(userChecklistGenerationData);

		// Simulation Exception
		doThrow(new RetraiteException("xxx")).when(userChecklistGeneratorMock).generate(any(UserChecklistGenerationData.class), any(RegimeAligne.class));

		final PostData postData = new PostData();
		postData.hidden_step = "displayAdditionalQuestions";
		postData.hidden_regimes = "CNAV,xxx";
		final RenderData renderData = new RenderData();

		displayerChecklist.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displayCheckList");
		assertThat(renderData.userChecklist).isNull();
		assertThat(renderData.errorMessage).isEqualTo("Désolé, impossible de déterminer le régime liquidateur...");
	}

}
