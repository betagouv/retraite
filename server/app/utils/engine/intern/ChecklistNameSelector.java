package utils.engine.intern;

import static java.util.Arrays.asList;
import static utils.JsonUtils.toJson;

import utils.RetraiteException;
import utils.engine.data.enums.ChecklistName;
import utils.engine.data.enums.RegimeAligne;

public class ChecklistNameSelector {

	public ChecklistName select(final RegimeAligne[] regimesAlignes, final RegimeAligne regimeLiquidateur) {

		switch (regimeLiquidateur) {
		case CNAV:
			return ChecklistName.CNAV;
		case MSA:
			return ChecklistName.MSA;
		case RSI:
			return ChecklistName.RSI;
		}

		throw new RetraiteException("Situation anormale : impossible de déterminer la checklist : regimesAlignes=" + asList(regimesAlignes)
				+ ", regimeLiquidateur=" + toJson(regimeLiquidateur));

	}

}
