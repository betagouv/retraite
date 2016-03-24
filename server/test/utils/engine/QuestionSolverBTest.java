package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionChoiceValue.SALARIE;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_CHEF;
import static utils.engine.data.enums.UserStatus.STATUS_CONJOINT_COLLABORATEUR;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;
import static utils.engine.data.enums.UserStatus.STATUS_SA;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class QuestionSolverBTest {

	private QuestionSolverB questionSolverB;

	@Before
	public void setUp() {
		questionSolverB = new QuestionSolverB();
	}

	@Test
	public void test() {

		withAnswer(SALARIE)
				.callSolver()
				.assertLiquidateurIs(CNAV)
				.andStateIs(null);

		withAnswer(NSA)
				.callSolver()
				.assertLiquidateurIs(MSA)
				.andStateIs(STATUS_NSA);

		withAnswer(SA)
				.callSolver()
				.assertLiquidateurIs(MSA)
				.andStateIs(STATUS_SA);

		withAnswer(INDEP)
				.callSolver()
				.assertLiquidateurIs(RSI)
				.andStateIs(STATUS_CHEF);

		withAnswer(CONJOINT_INDEP)
				.callSolver()
				.assertLiquidateurIs(RSI)
				.andStateIs(STATUS_CONJOINT_COLLABORATEUR);

		withAnswer(DEUX_ACTIVITES)
				.callSolver()
				.assertLiquidateurIs(null)
				.andStateIs(null);

	}

	private TestContext withAnswer(final QuestionChoiceValue choiceValue) {
		return new TestContext(choiceValue);
	}

	private class TestContext {

		private final String liquidateurReponseJsonStr;
		private RegimeLiquidateurAndUserStatus solved;

		public TestContext(final QuestionChoiceValue choiceValue) {
			this.liquidateurReponseJsonStr = "[\"" + choiceValue.toString() + "\"]";
		}

		public TestContext callSolver() {
			solved = questionSolverB.solve(null, liquidateurReponseJsonStr);
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
