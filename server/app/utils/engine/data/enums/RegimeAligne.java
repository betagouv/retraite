package utils.engine.data.enums;

public enum RegimeAligne {

	CNAV(ChecklistName.CNAV), MSA(ChecklistName.MSA), RSI(ChecklistName.RSI);

	private final ChecklistName checklistName;

	private RegimeAligne(final ChecklistName checklistName) {
		this.checklistName = checklistName;
	}

	public ChecklistName getChecklistName() {
		return checklistName;
	}

}
