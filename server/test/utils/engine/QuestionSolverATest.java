package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class QuestionSolverATest {

	private QuestionSolverA questionSolverA;

	@Before
	public void setUp() {
		questionSolverA = new QuestionSolverA();
	}

	@Test
	public void test_CNAV_MSA() {

		final TestContext withRegimes = withRegimes(CNAV, MSA);

		// Réponse E1 = NSA

		withRegimes
				.andAnswer(QuestionChoiceValue.NSA)
				.callSolver()
				.assertLiquidateurIs(CNAV)
				.andStateIs(STATUS_NSA);

		// Réponse E2 = SA

		withRegimes
				.andAnswer(QuestionChoiceValue.SA)
				.callSolver()
				.assertLiquidateurIs(null)
				.andStateIs(null);

		// Réponse E3 = Les deux

		withRegimes
				.andAnswer(QuestionChoiceValue.DEUX_ACTIVITES)
				.callSolver()
				.assertLiquidateurIs(MSA)
				.andStateIs(null);
	}

	@Test
	public void test_RSI_MAS() {

		final TestContext withRegimes = withRegimes(RSI, MSA);

		// Réponse E1 = NSA

		withRegimes
				.andAnswer(QuestionChoiceValue.NSA)
				.callSolver()
				.assertLiquidateurIs(RegimeAligne.RSI)
				.andStateIs(UserStatus.STATUS_NSA);

		// Réponse E2 = SA

		withRegimes
				.andAnswer(QuestionChoiceValue.SA)
				.callSolver()
				.assertLiquidateurIs(null)
				.andStateIs(null);

		// Réponse E3 = Les deux

		withRegimes
				.andAnswer(QuestionChoiceValue.DEUX_ACTIVITES)
				.callSolver()
				.assertLiquidateurIs(RegimeAligne.MSA)
				.andStateIs(null);
	}

	@Test
	public void test_CNAV_RSI_MAS() {

		final TestContext withRegimes = withRegimes(CNAV, RSI, MSA);

		// Réponse E1 = NSA

		withRegimes
				.andAnswer(QuestionChoiceValue.NSA)
				.callSolver()
				.assertLiquidateurIs(null)
				.andStateIs(UserStatus.STATUS_NSA);

		// Réponse E2 = SA

		withRegimes
				.andAnswer(QuestionChoiceValue.SA)
				.callSolver()
				.assertLiquidateurIs(null)
				.andStateIs(null);

		// Réponse E3 = Les deux

		withRegimes
				.andAnswer(QuestionChoiceValue.DEUX_ACTIVITES)
				.callSolver()
				.assertLiquidateurIs(RegimeAligne.MSA)
				.andStateIs(null);
	}

	private TestContext withRegimes(final RegimeAligne... regimes) {
		return new TestContext(regimes);
	}

	private class TestContext {

		private final RegimeAligne[] regimes;
		private String liquidateurReponseJsonStr;
		private RegimeLiquidateurAndUserStatus solved;

		public TestContext(final RegimeAligne[] regimes) {
			this.regimes = regimes;
		}

		public TestContext andAnswer(final QuestionChoiceValue choiceValue) {
			this.liquidateurReponseJsonStr = "[\"" + choiceValue.toString() + "\"]";
			return this;
		}

		public TestContext callSolver() {
			solved = questionSolverA.solve(regimes, liquidateurReponseJsonStr);
			return this;
		}

		public TestContext assertLiquidateurIs(final RegimeAligne regime) {
			assertThat(solved.getRegimeLiquidateur())
					.overridingErrorMessage("liquidateur=" + solved.getRegimeLiquidateur() + " is not equal to expected value " + toString(regime))
					.isEqualTo(regime);
			return this;
		}

		public void andStateIs(final UserStatus status) {
			assertThat(solved.getStatus())
					.overridingErrorMessage("status=" + solved.getStatus() + " is not equal to expected value " + toString(status))
					.isEqualTo(status);
		}

		private String toString(final Object o) {
			return o == null ? "null" : o.toString();
		}

	}

}
