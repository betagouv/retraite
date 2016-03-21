package utils.engine;

import static utils.engine.data.enums.QuestionChoiceValue.getFirstFromJsonArray;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_CHEF;
import static utils.engine.data.enums.UserStatus.STATUS_CONJOINT_COLLABORATEUR;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;

public class QuestionSolverF implements QuestionSolver {

	@Override
	public RegimeLiquidateurAndUserStatus solve(final RegimeAligne[] regimesAlignes, final String liquidateurReponseJsonStr) {
		final QuestionChoiceValue choiceValue = getFirstFromJsonArray(liquidateurReponseJsonStr);
		switch (choiceValue) {
		case OUI:
			return new RegimeLiquidateurAndUserStatus(RSI, STATUS_CHEF);
		case NON:
			return new RegimeLiquidateurAndUserStatus(RSI, STATUS_CONJOINT_COLLABORATEUR);
		}
		throw new IllegalStateException(
				"Situation non prévu : liquidateurReponseJsonStr=" + liquidateurReponseJsonStr);
	}

}
