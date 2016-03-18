package utils.engine;

import static utils.engine.data.enums.QuestionChoiceValue.getFirstFromJsonArray;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_CHEF;
import static utils.engine.data.enums.UserStatus.STATUS_CONJOINT_COLLABORATEUR;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;
import static utils.engine.data.enums.UserStatus.STATUS_SA;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;

public class SolverQuestionB {

	public RegimeLiquidateurAndUserStatus solve(final String liquidateurReponseJsonStr) {
		final QuestionChoiceValue choiceValue = getFirstFromJsonArray(liquidateurReponseJsonStr);
		switch (choiceValue) {
		case SALARIE:
			return new RegimeLiquidateurAndUserStatus(CNAV, null);
		case NSA:
			return new RegimeLiquidateurAndUserStatus(MSA, STATUS_NSA);
		case SA:
			return new RegimeLiquidateurAndUserStatus(MSA, STATUS_SA);
		case INDEP:
			return new RegimeLiquidateurAndUserStatus(RSI, STATUS_CHEF);
		case CONJOINT_INDEP:
			return new RegimeLiquidateurAndUserStatus(RSI, STATUS_CONJOINT_COLLABORATEUR);
		case DEUX_ACTIVITES:
			return new RegimeLiquidateurAndUserStatus(null, null);
		}
		throw new IllegalStateException(
				"Situation non prévu : liquidateurReponseJsonStr=" + liquidateurReponseJsonStr);
	}

}
