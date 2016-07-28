package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static utils.JsonUtils.toJson;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_CPAM;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_MSA;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_RSI;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class QuestionSolverDTest {

	private QuestionSolverD questionSolverD;

	@Before
	public void setUp() {
		questionSolverD = new QuestionSolverD();
	}

	@Test
	public void test() {

		withAnswer(SANTE_CPAM)
				.callSolver()
				.assertLiquidateurIs(CNAV)
				.andStateIs(null);

		withAnswer(SANTE_MSA)
				.callSolver()
				.assertLiquidateurIs(MSA)
				.andStateIs(null);

		withAnswer(SANTE_RSI)
				.callSolver()
				.assertLiquidateurIs(RSI)
				.andStateIs(null);

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
			solved = questionSolverD.solve(null, liquidateurReponseJsonStr);
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
