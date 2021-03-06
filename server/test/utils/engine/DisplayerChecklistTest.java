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
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.UserStatus.STATUS_CHEF;
import static utils.engine.data.enums.UserStatus.STATUS_INVALIDITE_RSI;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.RetraiteException;
import utils.engine.data.MonthAndYear;
import utils.engine.data.RenderData;
import utils.engine.data.StringPairsList;
import utils.engine.data.UserChecklist;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.StepFormsDataProvider;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;
import utils.engine.utils.DateProvider;

public class DisplayerChecklistTest {

	private UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilderMock;
	private UserChecklistGenerator userChecklistGeneratorMock;
	private CalculateurRegimeAlignes calculateurRegimeAlignesMock;

	private DisplayerChecklist displayerChecklist;

	@Before
	public void setUp() throws Exception {

		userChecklistGenerationDataBuilderMock = mock(UserChecklistGenerationDataBuilder.class);
		userChecklistGeneratorMock = mock(UserChecklistGenerator.class);
		final DateProvider dateProviderFake = new DateProviderFake(23, 12, 2015);
		calculateurRegimeAlignesMock = mock(CalculateurRegimeAlignes.class);
		displayerChecklist = new DisplayerChecklist(userChecklistGenerationDataBuilderMock, userChecklistGeneratorMock, dateProviderFake,
				calculateurRegimeAlignesMock, new StepFormsDataProvider(dateProviderFake));
	}

	@Test
	public void step_display_checklist() {

		final UserChecklist userChecklistMock = RetraiteEngineTest.createUserChecklist();

		final PostData postData = new PostData();
		postData.hidden_step = "displayAdditionalQuestions";
		postData.hidden_nom = "DUPONT";
		postData.hidden_naissance = "1/2/3";
		postData.hidden_nir = "1 50 12 18 123 456";
		postData.hidden_regimes = "CNAV";
		postData.hidden_regimesInfosJsonStr = "[{\"nom\":\"CNAV\",\"adresse\":\"addr CNAV\"}]";
		postData.hidden_liquidateurReponseJsonStr = null;
		postData.hidden_departMois = "2";
		postData.hidden_departAnnee = "2017";
		postData.hidden_attestationCarriereLongue = true;
		postData.hidden_liquidateur = CNAV;
		postData.hidden_userStatus = asList(STATUS_NSA, STATUS_INVALIDITE_RSI, STATUS_CHEF);
		postData.hidden_liquidateurReponsesHistory = "responses history";
		postData.departement = "973";

		final MonthAndYear dateDepart = new MonthAndYear("2", "2017");
		final Regime[] regimes = new Regime[] { Regime.CNAV };
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };
		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(dateDepart, "973", regimes, regimesAlignes,
				true, false, "[{\"nom\":\"CNAV\"}");

		when(calculateurRegimeAlignesMock.getRegimesAlignes("CNAV")).thenReturn(regimesAlignes);
		when(userChecklistGenerationDataBuilderMock.build(eq(dateDepart), eq("973"), eq(regimes), eq(regimesAlignes), eq(postData.hidden_liquidateur),
				eq(true), eq(true), eq(postData.hidden_userStatus), eq(false), eq("[{\"nom\":\"CNAV\",\"adresse\":\"addr CNAV\"}]")))
						.thenReturn(userChecklistGenerationData);
		when(userChecklistGeneratorMock.generate(same(userChecklistGenerationData), eq(postData.hidden_liquidateur))).thenReturn(userChecklistMock);
		final StringPairsList questionsAndResponses = new StringPairsList() {
			{
				add("a", "x");
				add("b", "y");
			}
		};

		final RenderData renderData = new RenderData();

		displayerChecklist.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displayCheckList");
		assertThat(renderData.userChecklist).isSameAs(userChecklistMock);

		final StringPairsList expectedUserInfos = new StringPairsList() {
			{
				add("Document produit le", "23/12/2015");
				add("Nom de naissance", "DUPONT");
				add("Date de départ envisagée", "01/02/2017");
				add("Département de résidence", "Guyane");
			}
		};
		assertThat(renderData.userInfos).isEqualTo(expectedUserInfos);
		assertThat(renderData.questionsAndResponses).isEqualTo(questionsAndResponses);
	}

	@Test
	public void step_display_checklist_error() {

		// Step : displayAdditionalQuestions --> displayCheckList

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create().get();

		when(calculateurRegimeAlignesMock.getRegimesAlignes(anyString())).thenReturn(new RegimeAligne[] { CNAV });
		when(userChecklistGenerationDataBuilderMock.build(any(MonthAndYear.class), anyString(), any(Regime[].class), any(RegimeAligne[].class),
				any(RegimeAligne.class), eq(true), eq(true), any(List.class), eq(false), anyString())).thenReturn(userChecklistGenerationData);

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
