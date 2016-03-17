package utils.engine;

import static utils.JsonUtils.fromJson;
import static utils.engine.EngineUtils.contains;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.NSA;

import java.util.Arrays;
import java.util.List;

import utils.engine.data.QuestionASolved;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;

public class SolverQuestionA {

	public QuestionASolved solve(final RegimeAligne[] regimesAlignes, final String liquidateurReponseJsonStr) {
		final QuestionChoiceValue choiceValue = getUserStatus(liquidateurReponseJsonStr);
		switch (choiceValue) {
		case NSA:
			if (contains(regimesAlignes, CNAV, MSA, RSI)) {
				return new QuestionASolved(null, NSA);
			}
			if (contains(regimesAlignes, CNAV, MSA)) {
				return new QuestionASolved(CNAV, NSA);
			}
			if (contains(regimesAlignes, RSI, MSA)) {
				return new QuestionASolved(RSI, NSA);
			}
		case SA:
			return new QuestionASolved();
		case DEUX_ACTIVITES:
			return new QuestionASolved(MSA, null);
		}
		throw new IllegalStateException(
				"Situation non prévu : regimesAlignes=" + Arrays.asList(regimesAlignes) + " , liquidateurReponseJsonStr=" + liquidateurReponseJsonStr);
	}

	private QuestionChoiceValue getUserStatus(final String liquidateurReponseJsonStr) {
		final List<String> responses = fromJson(liquidateurReponseJsonStr, List.class);
		return QuestionChoiceValue.valueOf(responses.get(0));
	}

}
