package utils.engine;

import static utils.JsonUtils.fromJson;

import java.util.Arrays;
import java.util.List;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;

public class SolverQuestionB {

	public RegimeLiquidateurAndUserStatus solve(final RegimeAligne[] regimesAlignes, final String liquidateurReponseJsonStr) {
		// final QuestionChoiceValue choiceValue = getUserStatus(liquidateurReponseJsonStr);
		// switch (choiceValue) {
		// case NSA:
		// if (contains(regimesAlignes, CNAV, MSA, RSI)) {
		// return new RegimeLiquidateurAndUserStatus(null, STATUS_NSA);
		// }
		// if (contains(regimesAlignes, CNAV, MSA)) {
		// return new RegimeLiquidateurAndUserStatus(CNAV, STATUS_NSA);
		// }
		// if (contains(regimesAlignes, RSI, MSA)) {
		// return new RegimeLiquidateurAndUserStatus(RSI, STATUS_NSA);
		// }
		// case SA:
		// return new RegimeLiquidateurAndUserStatus();
		// case DEUX_ACTIVITES:
		// return new RegimeLiquidateurAndUserStatus(MSA, null);
		// }
		throw new IllegalStateException(
				"Situation non prévu : regimesAlignes=" + Arrays.asList(regimesAlignes) + " , liquidateurReponseJsonStr=" + liquidateurReponseJsonStr);
	}

	private QuestionChoiceValue getUserStatus(final String liquidateurReponseJsonStr) {
		final List<String> responses = fromJson(liquidateurReponseJsonStr, List.class);
		return QuestionChoiceValue.valueOf(responses.get(0));
	}

}
