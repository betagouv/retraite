package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static utils.JsonUtils.toJson;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;
import static utils.engine.data.enums.UserStatus.STATUS_SA;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class QuestionSolverETest {

	private QuestionSolverE questionSolverE;

	@Before
	public void setUp() {
		questionSolverE = new QuestionSolverE();
	}

	@Test
	public void test() {

		withAnswer(OUI)
				.callSolver()
				.assertLiquidateurIs(MSA)
				.andStateIs(STATUS_NSA);

		withAnswer(NON)
				.callSolver()
				.assertLiquidateurIs(MSA)
				.andStateIs(STATUS_SA);

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
			solved = questionSolverE.solve(null, liquidateurReponseJsonStr);
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
