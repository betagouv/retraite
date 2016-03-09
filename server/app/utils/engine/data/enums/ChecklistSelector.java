package utils.engine.data.enums;

import static utils.engine.data.enums.ChecklistName.CNAV;
import static utils.engine.data.enums.ChecklistName.MSA;
import static utils.engine.data.enums.ChecklistName.RSI;

public enum ChecklistSelector {

	// @formatter:off

	SELECT_CNAV(CNAV),
	SELECT_RSI(RSI),
	SELECT_MSA(MSA),
	SELECT_RSI_AVANT_73(RSI),
	SELECT_RSI_INVALIDITE(RSI),
	SELECT_CNAV_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE(CNAV),
	SELECT_MSA_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE(MSA);

	// @formatter:on

	private final ChecklistName checklistName;

	private ChecklistSelector(final ChecklistName checklistName) {
		this.checklistName = checklistName;
	}

	public ChecklistName getChecklistName() {
		return checklistName;
	}

}
