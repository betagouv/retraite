package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static utils.JsonUtils.toJson;
import static utils.engine.data.enums.EcranSortie.ECRAN_SORTIE_PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP_AVANT_73;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_PENIBILITE;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.EcranSortie;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class QuestionSolverCTest {

	private QuestionSolverC questionSolverC;

	@Before
	public void setUp() {
		questionSolverC = new QuestionSolverC();
	}

	@Test
	public void test() {

		withAnswerNull()
				.callSolver()
				.assertLiquidateurIs(null)
				.andStateIs(null);

		withAnswerEmpty()
				.callSolver()
				.assertLiquidateurIs(null)
				.andStateIs(null);

		withAnswer(INDEP_AVANT_73)
				.callSolver()
				.assertLiquidateurIs(RSI)
				.andStateIs(null);

		withAnswer(INDEP_AVANT_73, INVALIDITE_RSI)
				.callSolver()
				.assertLiquidateurIs(RSI)
				.andStateIs(STATUS_PENIBILITE);

		withAnswer(INDEP_AVANT_73, INVALIDITE_RSI, PENIBILITE)
				.callSolver()
				.assertLiquidateurIs(null)
				.andStateIs(null)
				.andIsEcranSortie(ECRAN_SORTIE_PENIBILITE);

	}

	private TestContext withAnswerNull() {
		return new TestContext();
	}

	private TestContext withAnswerEmpty() {
		return new TestContext(new QuestionChoiceValue[0]);
	}

	private TestContext withAnswer(final QuestionChoiceValue... choiceValues) {
		return new TestContext(choiceValues);
	}

	private class TestContext {

		private final String liquidateurReponseJsonStr;
		private RegimeLiquidateurAndUserStatus solved;

		public TestContext() {
			this.liquidateurReponseJsonStr = null;
		}

		public TestContext(final QuestionChoiceValue[] choiceValues) {
			this.liquidateurReponseJsonStr = toJson(choiceValues);
		}

		public TestContext callSolver() {
			solved = questionSolverC.solve(null, liquidateurReponseJsonStr);
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

		public void andIsEcranSortie(final EcranSortie ecranSortie) {
			assertThat(solved.getEcranSortie())
					.overridingErrorMessage("ecranSortie=" + solved.getEcranSortie() + " is not equal to expected value " + toString(ecranSortie))
					.isEqualTo(ecranSortie);
		}

		private String toString(final Object o) {
			return o == null ? "null" : o.toString();
		}

	}

}
