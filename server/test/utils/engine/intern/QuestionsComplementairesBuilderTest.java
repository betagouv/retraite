package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static utils.engine.data.enums.ComplementQuestionDescriptor.ACCORD_INFO_RELEVE_CARRIERE;
import static utils.engine.data.enums.ComplementQuestionDescriptor.CONSULT_RELEVE_CARRIERE;
import static utils.engine.data.enums.ComplementQuestionDescriptor.DEMARCHES_EN_LIGNE;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.QuestionChoice;
import utils.engine.data.QuestionComplementaire;
import utils.engine.data.enums.ComplementQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class QuestionsComplementairesBuilderTest {

	private QuestionsComplementairesBuilder questionsComplementairesBuilder;

	@Before
	public void setUp() {
		questionsComplementairesBuilder = new QuestionsComplementairesBuilder();
	}

	@Test
	public void should_throw_error_if_less_than_2_regimes() {

		final List<QuestionComplementaire> questions = questionsComplementairesBuilder.buildQuestions();

		assertThat(questions).hasSize(3);

		final QuestionComplementaire question0 = questions.get(0);
		assertThat(question0.type).isEqualTo(DEMARCHES_EN_LIGNE);

		final QuestionComplementaire question1 = questions.get(1);
		assertThat(question1.type).isEqualTo(CONSULT_RELEVE_CARRIERE);

		final QuestionComplementaire question2 = questions.get(2);
		assertThat(question2.type).isEqualTo(ACCORD_INFO_RELEVE_CARRIERE);
		assertThat(question2.condition.key).isEqualTo(CONSULT_RELEVE_CARRIERE);
		assertThat(question2.condition.value).isEqualTo(OUI);

		assertIntegrity(questions);

	}

	private void assertIntegrity(final List<QuestionComplementaire> questions) {
		assertConditionIsWithExistingValueInQuestion(questions);
	}

	private void assertConditionIsWithExistingValueInQuestion(final List<QuestionComplementaire> questions) {
		for (final QuestionComplementaire questionComplementaire : questions) {
			if (questionComplementaire.condition != null) {
				assertChoiceValueIsPresentInQuestion(questionComplementaire.condition.value,
						(ComplementQuestionDescriptor) questionComplementaire.condition.key);
			}
		}
	}

	private void assertChoiceValueIsPresentInQuestion(final QuestionChoiceValue value, final ComplementQuestionDescriptor complementQuestionDescriptor) {
		final QuestionChoice[] choices = complementQuestionDescriptor.getQuestionChoices();
		for (final QuestionChoice questionChoice : choices) {
			if (questionChoice.getValue().equals(value)) {
				return;
			}
		}
		fail("La valeur " + value + "n'est pas présente dans les choix " + Arrays.asList(choices));
	}

}
