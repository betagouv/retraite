package utils.engine.intern;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static utils.TestsUtils.assertReponseValuesAreInQuestions;
import static utils.engine.data.enums.ComplementQuestionDescriptor.ACCORD_INFO_RELEVE_CARRIERE;
import static utils.engine.data.enums.ComplementQuestionDescriptor.CONSULT_RELEVE_CARRIERE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.CHEF_EXPLOITATION_AGRICOLE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_INDEP_CONJOINT_AUTRE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_NSA_CONJOINT_INDEP;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE;
import static utils.engine.data.enums.QuestionChoiceValue.AUTRE;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.NE_SAIT_PAS;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;
import static utils.engine.data.enums.QuestionChoiceValue.SA;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import utils.TestsUtils;
import utils.engine.data.ComplementReponses;
import utils.engine.data.LiquidateurReponses;
import utils.engine.data.enums.ComplementQuestionDescriptor;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.intern.LiquidateurReponsesEvaluator;

public class LiquidateurReponsesEvaluatorTest {

	private LiquidateurReponsesEvaluator liquidateurReponsesEvaluator;

	@Before
	public void setUp() {
		liquidateurReponsesEvaluator = new LiquidateurReponsesEvaluator();
	}

	@Test
	public void test_NSA_false_if_no_responses() {

		final boolean result = liquidateurReponsesEvaluator.isNSA(null);

		assertThat(result).isFalse();
	}

	@Test
	public void test_NSA() {

		final List<TestStatutData> data = asList(
				data(CHEF_EXPLOITATION_AGRICOLE, OUI, true),
				data(DERN_ACT_NSA_CONJOINT_INDEP, NSA, true),
				data(DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE, SA, false),
				data(DERN_ACT_NSA_CONJOINT_INDEP, SA, false));

		for (final TestStatutData testStatutData : data) {
			final LiquidateurReponses reponses = new LiquidateurReponses();
			reponses.getReponses().put(testStatutData.descriptor, asList(testStatutData.value));
			assertReponseValuesAreInQuestions(reponses);

			final boolean result = liquidateurReponsesEvaluator.isNSA(reponses);

			assertThat(result)
					.overridingErrorMessage("Test failed for : " + testStatutData.descriptor + "=" + testStatutData.value)
					.isEqualTo(testStatutData.expectedResult);
		}
	}

	@Test
	public void test_SA_false_if_no_responses() {

		final boolean result = liquidateurReponsesEvaluator.isSA(new LiquidateurReponses());

		assertThat(result).isFalse();
	}

	@Test
	public void test_SA() {

		final List<TestStatutData> data = asList(
				data(CHEF_EXPLOITATION_AGRICOLE, OUI, false),
				data(DERN_ACT_NSA_CONJOINT_INDEP, NSA, false),
				data(DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE, SA, true),
				data(DERN_ACT_NSA_CONJOINT_INDEP, SA, true));

		for (final TestStatutData testStatutData : data) {
			final LiquidateurReponses reponses = new LiquidateurReponses();
			reponses.getReponses().put(testStatutData.descriptor, asList(testStatutData.value));
			assertReponseValuesAreInQuestions(reponses);

			final boolean result = liquidateurReponsesEvaluator.isSA(reponses);

			assertThat(result)
					.overridingErrorMessage("Test failed for : " + testStatutData.descriptor + "=" + testStatutData.value)
					.isEqualTo(testStatutData.expectedResult);
		}
	}

	@Test
	public void test_conjoint_collaborateur() {

		final List<TestStatutData> data = asList(
				data(DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE, SA, false),
				data(DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE, CONJOINT, true),
				data(DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE, AUTRE, false),
				data(DERN_ACT_INDEP_CONJOINT_AUTRE, CONJOINT, true),
				data(DERN_ACT_NSA_CONJOINT_INDEP, CONJOINT, true),
				data(DERN_ACT_NSA_CONJOINT_INDEP, CONJOINT_INDEP, true));

		for (final TestStatutData testStatutData : data) {
			final LiquidateurReponses reponses = new LiquidateurReponses();
			reponses.getReponses().put(testStatutData.descriptor, asList(testStatutData.value));
			assertReponseValuesAreInQuestions(reponses);

			final boolean result = liquidateurReponsesEvaluator.isConjointCollaborateur(reponses);

			assertThat(result)
					.overridingErrorMessage("Test failed for : " + testStatutData.descriptor + "=" + testStatutData.value + " and expected result "
							+ testStatutData.expectedResult)
					.isEqualTo(testStatutData.expectedResult);
		}
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

	private TestStatutData data(final LiquidateurQuestionDescriptor descriptor, final QuestionChoiceValue value, final boolean expectedResult) {
		return new TestStatutData(descriptor, value, expectedResult);
	}

	private static class TestStatutData {
		final LiquidateurQuestionDescriptor descriptor;
		final QuestionChoiceValue value;
		final boolean expectedResult;

		TestStatutData(final LiquidateurQuestionDescriptor descriptor, final QuestionChoiceValue value, final boolean expectedResult) {
			this.descriptor = descriptor;
			this.value = value;
			this.expectedResult = expectedResult;
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
