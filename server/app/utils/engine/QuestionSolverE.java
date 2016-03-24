package utils.engine;

import static utils.engine.data.enums.QuestionChoiceValue.getFirstFromJsonArray;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;
import static utils.engine.data.enums.UserStatus.STATUS_SA;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;

public class QuestionSolverE implements QuestionSolver {

	@Override
	public RegimeLiquidateurAndUserStatus solve(final RegimeAligne[] regimesAlignes, final String liquidateurReponseJsonStr) {
		final QuestionChoiceValue choiceValue = getFirstFromJsonArray(liquidateurReponseJsonStr);
		switch (choiceValue) {
		case OUI:
			return new RegimeLiquidateurAndUserStatus(MSA, STATUS_NSA);
		case NON:
			return new RegimeLiquidateurAndUserStatus(MSA, STATUS_SA);
		}
		throw new IllegalStateException(
				"Situation non prévu : liquidateurReponseJsonStr=" + liquidateurReponseJsonStr);
	}

}
