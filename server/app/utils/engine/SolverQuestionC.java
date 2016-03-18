package utils.engine;

import static utils.engine.data.enums.EcranSortie.ECRAN_SORTIE_PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.getFromJsonArray;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_PENIBILITE;

import java.util.List;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.QuestionChoiceValue;

public class SolverQuestionC {

	public RegimeLiquidateurAndUserStatus solve(final String liquidateurReponseJsonStr) {
		final List<QuestionChoiceValue> choiceValues = getFromJsonArray(liquidateurReponseJsonStr);
		if (choiceValues == null) {
			return new RegimeLiquidateurAndUserStatus(null, null);
		}
		if (choiceValues.contains(PENIBILITE)) {
			return new RegimeLiquidateurAndUserStatus(ECRAN_SORTIE_PENIBILITE);
		}
		if (choiceValues.contains(INVALIDITE_RSI)) {
			return new RegimeLiquidateurAndUserStatus(RSI, STATUS_PENIBILITE);
		}
		if (choiceValues.contains(QuestionChoiceValue.INDEP_AVANT_73)) {
			return new RegimeLiquidateurAndUserStatus(RSI, null);
		}
		throw new IllegalStateException(
				"Situation non prévu : liquidateurReponseJsonStr=" + liquidateurReponseJsonStr);
	}

}
