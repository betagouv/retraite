package utils.engine.data.enums;

public enum RegimeAligne {

	// @formatter:off

	CNAV(ChecklistName.CNAV,
			"http://CNAV quand-prendre-sa-retraite",
			"http://CNAV mon-releve-de-carriere",
			"http://CNAV EVA"),

	MSA(ChecklistName.MSA,
			"http://MSA quand-prendre-sa-retraite",
			"http://MSA mon-releve-de-carriere",
			"http://CNAV MSA"),

	RSI(ChecklistName.RSI,
			"http://rsi.fr/quand-prendre-sa-retraite",
			"http://rsi.fr/mon-releve-de-carriere",
			"http://RSI EVA");

	// @formatter:on

	private final ChecklistName checklistName;
	public final String urlAgeDepart;
	public final String urlDroits;
	public final String urlSimulMontant;

	private RegimeAligne(final ChecklistName checklistName, final String urlAgeDepart, final String urlDroits, final String urlSimulMontant) {
		this.checklistName = checklistName;
		this.urlAgeDepart = urlAgeDepart;
		this.urlDroits = urlDroits;
		this.urlSimulMontant = urlSimulMontant;
	}

	public ChecklistName getChecklistName() {
		return checklistName;
	}

}
