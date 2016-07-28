package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static utils.JsonUtils.toJson;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_CHEF;
import static utils.engine.data.enums.UserStatus.STATUS_CONJOINT_COLLABORATEUR;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class QuestionSolverFTest {

	private QuestionSolverF questionSolverF;

	@Before
	public void setUp() {
		questionSolverF = new QuestionSolverF();
	}

	@Test
	public void test() {

		withAnswer(OUI)
				.callSolver()
				.assertLiquidateurIs(RSI)
				.andStateIs(STATUS_CHEF);

		withAnswer(NON)
				.callSolver()
				.assertLiquidateurIs(RSI)
				.andStateIs(STATUS_CONJOINT_COLLABORATEUR);

	}

	private TestContext withAnswer(final QuestionChoiceValue... choiceValues) {
		return new TestContext(choiceValues);
	}

	private class TestContext {

		private final String liquidateurReponseJsonStr;
		private RegimeLiquidateurAndUserStatus solved;

		public TestContext(final QuestionChoiceValue[] choiceValues) {
			this.liquidateurReponseJsonStr = toJson(choiceValues);
		}

		public TestContext callSolver() {
			solved = questionSolverF.solve(null, liquidateurReponseJsonStr);
			return this;
		}

		public TestContext assertLiquidateurIs(final RegimeAligne regime) {
			assertThat(solved.getRegimeLiquidateur())
					.overridingErrorMessage("liquidateur=" + solved.getRegimeLiquidateur() + " is not equal to expected value " + toString(regime))
					.isEqualTo(regime);
			return this;
		}

		public TestContext andStateIs(final UserStatus status) {
			assertThat(solved.getStatus())
					.overridingErrorMessage("status=" + solved.getStatus() + " is not equal to expected value " + toString(status))
					.isEqualTo(status);
			return this;
		}

		private String toString(final Object o) {
			return o == null ? "null" : o.toString();
		}

	}

}
