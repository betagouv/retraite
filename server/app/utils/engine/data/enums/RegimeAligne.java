package utils.engine.data.enums;

public enum RegimeAligne {

	// @formatter:off

	CNAV(ChecklistName.CNAV,
			"http://CNAV quand-prendre-sa-retraite",
			"http://CNAV mon-releve-de-carriere",
			"http://CNAV EVA",
			"http://CNAV urlDispositifDepartAvantAgeLegal"),

	MSA(ChecklistName.MSA,
			"http://www.msa.fr/lfr/retraite/conditions-age-depart-taux-plein",
			"http://MSA mon-releve-de-carriere",
			"http://CNAV MSA",
			"http://CNAV urlDispositifDepartAvantAgeLegal"),

	RSI(ChecklistName.RSI,
			"http://rsi.fr/quand-prendre-sa-retraite",
			"http://rsi.fr/mon-releve-de-carriere",
			"http://RSI EVA",
			"http://RSI urlDispositifDepartAvantAgeLegal");

	// @formatter:on

	private final ChecklistName checklistName;
	public final String urlAgeDepart;
	public final String urlDroits;
	public final String urlSimulMontant;
	public final String urlDispositifDepartAvantAgeLegal;

	private RegimeAligne(final ChecklistName checklistName,
			final String urlAgeDepart,
			final String urlDroits,
			final String urlSimulMontant,
			final String urlDispositifDepartAvantAgeLegal) {
		this.checklistName = checklistName;
		this.urlAgeDepart = urlAgeDepart;
		this.urlDroits = urlDroits;
		this.urlSimulMontant = urlSimulMontant;
		this.urlDispositifDepartAvantAgeLegal = urlDispositifDepartAvantAgeLegal;
	}

	public ChecklistName getChecklistName() {
		return checklistName;
	}

}
