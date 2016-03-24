package utils.engine;

import static utils.engine.data.enums.QuestionChoiceValue.getFirstFromJsonArray;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;

public class QuestionSolverD implements QuestionSolver {

	@Override
	public RegimeLiquidateurAndUserStatus solve(final RegimeAligne[] regimesAlignes, final String liquidateurReponseJsonStr) {
		final QuestionChoiceValue choiceValue = getFirstFromJsonArray(liquidateurReponseJsonStr);
		switch (choiceValue) {
		case SANTE_CPAM:
			return new RegimeLiquidateurAndUserStatus(CNAV, null);
		case SANTE_MSA:
			return new RegimeLiquidateurAndUserStatus(MSA, null);
		case SANTE_RSI:
			return new RegimeLiquidateurAndUserStatus(RSI, null);
		}
		throw new IllegalStateException(
				"Situation non prévu : liquidateurReponseJsonStr=" + liquidateurReponseJsonStr);
	}

}
