package utils.engine;

import static utils.engine.EngineUtils.contains;
import static utils.engine.data.enums.QuestionChoiceValue.getFromJsonArray;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;

import java.util.Arrays;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;

public class SolverQuestionA {

	public RegimeLiquidateurAndUserStatus solve(final RegimeAligne[] regimesAlignes, final String liquidateurReponseJsonStr) {
		final QuestionChoiceValue choiceValue = getFromJsonArray(liquidateurReponseJsonStr);
		switch (choiceValue) {
		case NSA:
			if (contains(regimesAlignes, CNAV, MSA, RSI)) {
				return new RegimeLiquidateurAndUserStatus(null, STATUS_NSA);
			}
			if (contains(regimesAlignes, CNAV, MSA)) {
				return new RegimeLiquidateurAndUserStatus(CNAV, STATUS_NSA);
			}
			if (contains(regimesAlignes, RSI, MSA)) {
				return new RegimeLiquidateurAndUserStatus(RSI, STATUS_NSA);
			}
		case SA:
			return new RegimeLiquidateurAndUserStatus();
		case DEUX_ACTIVITES:
			return new RegimeLiquidateurAndUserStatus(MSA, null);
		}
		throw new IllegalStateException(
				"Situation non prévu : regimesAlignes=" + Arrays.asList(regimesAlignes) + " , liquidateurReponseJsonStr=" + liquidateurReponseJsonStr);
	}

}
