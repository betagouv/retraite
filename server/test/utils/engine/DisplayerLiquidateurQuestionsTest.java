package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.engine.data.enums.EcranSortie.ECRAN_SORTIE_PENIBILITE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_A;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_B;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_C;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_D;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionChoiceValue.SALARIE;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_MSA;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_RSI;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_CHEF;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.engine.data.QuestionChoice;
import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.RenderData;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class DisplayerLiquidateurQuestionsTest {

	private PostData postData;
	private RenderData renderData;
	private final String regimes = "CNAV,CCMSA,AGIRC ARRCO";

	private QuestionSolverA solverQuestionAMock;
	private QuestionSolverB solverQuestionBMock;
	private QuestionSolverC solverQuestionCMock;

	private DisplayerLiquidateurQuestions displayerLiquidateurQuestions;

	@Before
	public void setUp() throws Exception {
		postData = new PostData();
		renderData = new RenderData();

		solverQuestionAMock = mock(QuestionSolverA.class);
		when(solverQuestionAMock.solve(any(RegimeAligne[].class), anyString()))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		solverQuestionBMock = mock(QuestionSolverB.class);
		when(solverQuestionBMock.solve(any(RegimeAligne[].class), anyString()))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		solverQuestionCMock = mock(QuestionSolverC.class);
		when(solverQuestionCMock.solve(any(RegimeAligne[].class), anyString()))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions = new DisplayerLiquidateurQuestions(solverQuestionAMock, solverQuestionBMock, solverQuestionCMock);
	}

	@Test
	public void should_set_step_and_store_regimes() {

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, null);

		assertThat(renderData.hidden_step).isEqualTo("displayLiquidateurQuestions");
		assertThat(renderData.hidden_regimes).isEqualTo(regimes);
	}

	@Test
	public void test_debut_question_A_si_MSA() {

		// Step : (null) --> QUESTION_A

		postData.hidden_liquidateurStep = null;
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV, MSA };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_A");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_A);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_debut_question_B_si_pas_MSA() {

		// Step : (null) --> QUESTION_B

		postData.hidden_liquidateurStep = null;
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_B");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_B);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(SALARIE);
	}

	@Test
	public void test_question_B_apres_question_A_avec_filtre_et_avec_deux_activites() {

		// Step : QUESTION_A --> QUESTION_B avec DEUX_ACTIVITES

		postData.hidden_liquidateurStep = "QUESTION_A";
		postData.liquidateurReponseJsonStr = "[\"NSA\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA, RSI };

		when(solverQuestionAMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(null, STATUS_NSA));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_B");
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isEqualTo("STATUS_NSA");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_B);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(NSA, SA, INDEP, CONJOINT_INDEP, DEUX_ACTIVITES);
	}

	@Test
	public void test_question_B_apres_question_A_avec_filtre_et_sans_deux_activites() {

		// Step : QUESTION_A --> QUESTION_B sans DEUX_ACTIVITES

		postData.hidden_liquidateurStep = "QUESTION_A";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_B");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_B);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(INDEP, CONJOINT_INDEP);
	}

	@Test
	public void test_question_C_apres_question_A_si_RSI_et_regime_liquidateur_determine() {

		// Step : QUESTION_A --> QUESTION_C (= pas de QUESTION_B si régime liquidateur déterminé)

		postData.hidden_liquidateurStep = "QUESTION_A";
		postData.liquidateurReponseJsonStr = "[\"NSA\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };

		when(solverQuestionAMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(RegimeAligne.MSA, UserStatus.STATUS_NSA));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_C");
		assertThat(renderData.hidden_liquidateur).isEqualTo("MSA");
		assertThat(renderData.hidden_userStatus).isEqualTo("STATUS_NSA");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_C_apres_question_B_si_RSI() {

		// Step : QUESTION_B --> QUESTION_C

		postData.hidden_liquidateurStep = "QUESTION_B";
		postData.liquidateurReponseJsonStr = "[\"INDEP\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };

		when(solverQuestionBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_C");
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_C_apres_question_B_avec_filtre_si_RSI_liquidateur_depuis_ancienne_question() {

		// Step : QUESTION_B --> QUESTION_C

		postData.hidden_liquidateurStep = "QUESTION_B";
		postData.liquidateurReponseJsonStr = "[\"INDEP\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = "RSI";

		when(solverQuestionBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_C");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(INVALIDITE_RSI, PENIBILITE);
	}

	@Test
	public void test_question_C_apres_question_B_avec_filtre_si_RSI_liquidateur_depuis_derniere_question() {

		// Step : QUESTION_B --> QUESTION_C

		postData.hidden_liquidateurStep = "QUESTION_B";
		postData.liquidateurReponseJsonStr = "[\"INDEP\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = null;

		when(solverQuestionBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(RSI, STATUS_CHEF));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_C");
		assertThat(renderData.hidden_liquidateur).isEqualTo("RSI");
		assertThat(renderData.hidden_userStatus).isEqualTo("STATUS_CHEF");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(INVALIDITE_RSI, PENIBILITE);
	}

	@Test
	public void test_ecran_sortie_apres_question_C() {

		// Step : QUESTION_C --> Ecran de sortie PENINILITE

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[\"PENIBILITE\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = null;

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(ECRAN_SORTIE_PENIBILITE));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isNull();
		assertThat(renderData.ecranSortie).isEqualTo(ECRAN_SORTIE_PENIBILITE);
	}

	@Test
	public void test_question_D_apres_question_C_pas_de_filtre_si_3_regimes() {

		// Step : QUESTION_C --> QUESTION_D

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV, MSA, RSI };
		postData.hidden_liquidateur = null;

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_D");
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_D);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_D_apres_question_C_avec_filtre_si_2_regimes() {

		// Step : QUESTION_C --> QUESTION_D

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA, RSI };
		postData.hidden_liquidateur = null;

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_D");
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_D);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(SANTE_MSA, SANTE_RSI);
	}

	@Test
	public void test_no_more_question() {

		postData.hidden_liquidateurStep = "QUESTION_D";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	private List<QuestionChoiceValue> choicesValues(final List<QuestionChoice> choices) {
		if (choices == null) {
			return null;
		}
		final ArrayList<QuestionChoiceValue> choicesValues = new ArrayList<>();
		for (final QuestionChoice questionChoice : choices) {
			choicesValues.add(questionChoice.getValue());
		}
		return choicesValues;
	}

}
