package utils.engine.intern;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.ComplementQuestionDescriptor.ACCORD_INFO_RELEVE_CARRIERE;
import static utils.engine.data.enums.ComplementQuestionDescriptor.CONSULT_RELEVE_CARRIERE;
import static utils.engine.data.enums.QuestionChoiceValue.NE_SAIT_PAS;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import utils.TestsUtils;
import utils.engine.data.ComplementReponses;
import utils.engine.data.enums.ComplementQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class LiquidateurReponsesEvaluatorTest {

	private LiquidateurReponsesEvaluator liquidateurReponsesEvaluator;

	@Before
	public void setUp() {
		liquidateurReponsesEvaluator = new LiquidateurReponsesEvaluator();
	}

	@Test
	public void test_isCarriereAReconstituer() {

		// @formatter:off

		final List<TestCarriereData> data = asList(
			data(
				false,
				question(CONSULT_RELEVE_CARRIERE, OUI),
				question(ACCORD_INFO_RELEVE_CARRIERE, OUI)
			),
			data(
				true,
				question(CONSULT_RELEVE_CARRIERE, OUI),
				question(ACCORD_INFO_RELEVE_CARRIERE, NON)
			),
			data(
				true,
				question(CONSULT_RELEVE_CARRIERE, OUI),
				question(ACCORD_INFO_RELEVE_CARRIERE, NE_SAIT_PAS)
			),
			data(
				true,
				question(CONSULT_RELEVE_CARRIERE, NON)
			)
		);

		// @formatter:on

		for (final TestCarriereData testCarriereData : data) {
			final ComplementReponses reponses = new ComplementReponses();
			for (final Question question : testCarriereData.questions) {
				reponses.getReponses().put(question.descriptor, asList(question.value));
			}
			TestsUtils.assertReponseValuesAreInQuestions(reponses);

			final boolean result = liquidateurReponsesEvaluator.isCarriereAReconstituer(reponses);

			assertThat(result)
					.overridingErrorMessage("Test failed for : " + testCarriereData.questions + " and expected result "
							+ testCarriereData.expectedResult)
					.isEqualTo(testCarriereData.expectedResult);
		}
	}

	private Question question(final ComplementQuestionDescriptor descriptor, final QuestionChoiceValue value) {
		return new Question(descriptor, value);
	}

	private static class Question {

		private final ComplementQuestionDescriptor descriptor;
		private final QuestionChoiceValue value;

		public Question(final ComplementQuestionDescriptor descriptor, final QuestionChoiceValue value) {
			this.descriptor = descriptor;
			this.value = value;
		}

	}

	private TestCarriereData data(final boolean expectedResult, final Question... questions) {
		return new TestCarriereData(questions, expectedResult);
	}

	private static class TestCarriereData {
		private final Question[] questions;
		private final boolean expectedResult;

		public TestCarriereData(final Question[] questions, final boolean expectedResult) {
			this.questions = questions;
			this.expectedResult = expectedResult;
		}
	}

}
