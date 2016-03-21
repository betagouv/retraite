package utils.engine;

import static utils.engine.data.enums.QuestionChoiceValue.getFirstFromJsonArray;
import static utils.engine.data.enums.RegimeAligne.MSA;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class QuestionSolverE implements QuestionSolver {

	@Override
	public RegimeLiquidateurAndUserStatus solve(final RegimeAligne[] regimesAlignes, final String liquidateurReponseJsonStr) {
		final QuestionChoiceValue choiceValue = getFirstFromJsonArray(liquidateurReponseJsonStr);
		switch (choiceValue) {
		case OUI:
			return new RegimeLiquidateurAndUserStatus(MSA, UserStatus.STATUS_NSA);
		case NON:
			return new RegimeLiquidateurAndUserStatus(MSA, UserStatus.STATUS_SA);
		}
		throw new IllegalStateException(
				"Situation non prévu : liquidateurReponseJsonStr=" + liquidateurReponseJsonStr);
	}

}
