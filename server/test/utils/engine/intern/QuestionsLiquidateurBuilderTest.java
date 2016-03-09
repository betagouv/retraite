package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.CHEF_EXPLOITATION_AGRICOLE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_INDEP_CONJOINT_AUTRE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_NSA_CONJOINT_INDEP;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DIVERSES_SITUATIONS_CNAV_RSI;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DIVERSES_SITUATIONS_RSI_MSA;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.ORGA_FRAIS_SANTE_CPAM_MSA;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.ORGA_FRAIS_SANTE_CPAM_RSI;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.ORGA_FRAIS_SANTE_RSI_MSA;
import static utils.engine.data.enums.QuestionChoiceValue.AUTRE;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import utils.RetraiteException;
import utils.engine.data.QuestionChoice;
import utils.engine.data.QuestionLiquidateur;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;

public class QuestionsLiquidateurBuilderTest {

	private QuestionsLiquidateurBuilder questionsLiquidateurBuilder;

	@Before
	public void setUp() {
		questionsLiquidateurBuilder = new QuestionsLiquidateurBuilder();
	}

	@Test
	public void should_throw_error_if_less_than_2_regimes() {

		try {
			questionsLiquidateurBuilder.buildQuestions(array(CNAV));
			fail("Devrait déclencher une excpetion");
		} catch (final RetraiteException e) {
		}

	}

	@Test
	public void should_throw_error_if_unsupported_situation() {

		try {
			questionsLiquidateurBuilder.buildQuestions(array(CNAV, MSA, RSI));
			fail("Devrait déclencher une excpetion");
		} catch (final RetraiteException e) {
		}

	}

	@Test
	public void CNAV_MSA() {

		final List<QuestionLiquidateur> questions = questionsLiquidateurBuilder.buildQuestions(array(CNAV, MSA));

		assertThat(questions).hasSize(3);

		final QuestionLiquidateur question0 = questions.get(0);
		assertThat(question0.type).isEqualTo(CHEF_EXPLOITATION_AGRICOLE);

		final QuestionLiquidateur question1 = questions.get(1);
		assertThat(question1.type).isEqualTo(DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE);
		assertThat(question1.condition.key).isEqualTo(CHEF_EXPLOITATION_AGRICOLE);
		assertThat(question1.condition.value).isEqualTo(NON);

		final QuestionLiquidateur question2 = questions.get(2);
		assertThat(question2.type).isEqualTo(ORGA_FRAIS_SANTE_CPAM_MSA);
		assertThat(question2.condition.key).isEqualTo(DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE);
		assertThat(question2.condition.value).isEqualTo(DEUX_ACTIVITES);

		assertIntegrity(questions);
	}

	@Test
	public void CNAV_RSI() {

		final List<QuestionLiquidateur> questions = questionsLiquidateurBuilder.buildQuestions(array(CNAV, RSI));

		assertThat(questions).hasSize(3);

		final QuestionLiquidateur question0 = questions.get(0);
		assertThat(question0.type).isEqualTo(DIVERSES_SITUATIONS_CNAV_RSI);

		final QuestionLiquidateur question1 = questions.get(1);
		assertThat(question1.type).isEqualTo(DERN_ACT_INDEP_CONJOINT_AUTRE);
		assertThat(question1.condition.key).isEqualTo(DIVERSES_SITUATIONS_CNAV_RSI);
		assertThat(question1.condition.value).isEqualTo(AUTRE);

		final QuestionLiquidateur question2 = questions.get(2);
		assertThat(question2.type).isEqualTo(ORGA_FRAIS_SANTE_CPAM_RSI);
		assertThat(question2.condition.key).isEqualTo(DERN_ACT_INDEP_CONJOINT_AUTRE);
		assertThat(question2.condition.value).isEqualTo(DEUX_ACTIVITES);

		assertIntegrity(questions);
	}

	@Test
	public void RSI_MSA() {

		final List<QuestionLiquidateur> questions = questionsLiquidateurBuilder.buildQuestions(array(RSI, MSA));

		assertThat(questions).hasSize(3);

		final QuestionLiquidateur question0 = questions.get(0);
		assertThat(question0.type).isEqualTo(DIVERSES_SITUATIONS_RSI_MSA);

		final QuestionLiquidateur question1 = questions.get(1);
		assertThat(question1.type).isEqualTo(DERN_ACT_NSA_CONJOINT_INDEP);
		assertThat(question1.condition.key).isEqualTo(DIVERSES_SITUATIONS_RSI_MSA);
		assertThat(question1.condition.value).isEqualTo(AUTRE);

		final QuestionLiquidateur question2 = questions.get(2);
		assertThat(question2.type).isEqualTo(ORGA_FRAIS_SANTE_RSI_MSA);
		assertThat(question2.condition.key).isEqualTo(DERN_ACT_NSA_CONJOINT_INDEP);
		assertThat(question2.condition.value).isEqualTo(DEUX_ACTIVITES);

		assertIntegrity(questions);
	}

	private void assertIntegrity(final List<QuestionLiquidateur> questions) {
		assertConditionIsWithExistingValueInQuestion(questions);
	}

	private void assertConditionIsWithExistingValueInQuestion(final List<QuestionLiquidateur> questions) {
		for (final QuestionLiquidateur questionLiquidateur : questions) {
			if (questionLiquidateur.condition != null) {
				assertChoiceValueIsPresentInQuestion(questionLiquidateur.condition.value, (LiquidateurQuestionDescriptor) questionLiquidateur.condition.key);
			}
		}
	}

	private void assertChoiceValueIsPresentInQuestion(final QuestionChoiceValue value, final LiquidateurQuestionDescriptor liquidateurQuestionDescriptor) {
		final QuestionChoice[] choices = liquidateurQuestionDescriptor.getQuestionChoices();
		for (final QuestionChoice questionChoice : choices) {
			if (questionChoice.getValue().equals(value)) {
				return;
			}
		}
		fail("La valeur " + value + " n'est pas présente dans les choix " + Arrays.asList(choices));
	}

	private RegimeAligne[] array(final RegimeAligne... regimes) {
		return regimes;
	}
}
