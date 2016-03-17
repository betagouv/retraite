package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_A;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_B;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionChoiceValue.SALARIE;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.engine.data.QuestionASolved;
import utils.engine.data.QuestionChoice;
import utils.engine.data.RenderData;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class DisplayerLiquidateurQuestionsTest {

	private PostData postData;
	private RenderData renderData;
	private final String regimes = "CNAV,CCMSA,AGIRC ARRCO";

	private SolverQuestionA solverQuestionAMock;

	private DisplayerLiquidateurQuestions displayerLiquidateurQuestions;

	@Before
	public void setUp() throws Exception {
		postData = new PostData();
		renderData = new RenderData();

		solverQuestionAMock = mock(SolverQuestionA.class);
		when(solverQuestionAMock.solve(any(RegimeAligne[].class), anyString()))
				.thenReturn(new QuestionASolved());

		displayerLiquidateurQuestions = new DisplayerLiquidateurQuestions(solverQuestionAMock);
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
		assertThat(renderData.questionLiquidateur.choices).isNull();
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
		postData.liquidateurReponseJsonStr = "[\"INDEP_AVANT_73\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA, RSI };

		when(solverQuestionAMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new QuestionASolved(RegimeAligne.MSA, UserStatus.NSA));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_B");
		assertThat(renderData.hidden_liquidateur).isEqualTo("MSA");
		assertThat(renderData.hidden_userStatus).isEqualTo("NSA");
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
	public void test_no_more_question() {

		postData.hidden_liquidateurStep = "QUESTION_B";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	private List<QuestionChoiceValue> choicesValues(final List<QuestionChoice> choices) {
		final ArrayList<QuestionChoiceValue> choicesValues = new ArrayList<>();
		for (final QuestionChoice questionChoice : choices) {
			choicesValues.add(questionChoice.getValue());
		}
		return choicesValues;
	}

}
