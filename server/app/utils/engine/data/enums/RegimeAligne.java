package utils.engine.data.enums;

public enum RegimeAligne {

	// @formatter:off

	CNAV(ChecklistName.CNAV,
			"http://CNAV quand-prendre-sa-retraite",
			"http://CNAV mon-releve-de-carriere",
			"http://CNAV EVA",
			"http://CNAV urlDispositifDepartAvantAgeLegal",
			"http://CNAV urlInfosPenibilite"),

	MSA(ChecklistName.MSA,
			"http://www.msa.fr/lfr/retraite/conditions-age-depart-taux-plein",
			"http://MSA mon-releve-de-carriere",
			"http://MSA EVA",
			"http://MSA urlDispositifDepartAvantAgeLegal",
			"http://MSA urlInfosPenibilite"),

	RSI(ChecklistName.RSI,
			"http://rsi.fr/quand-prendre-sa-retraite",
			"http://rsi.fr/mon-releve-de-carriere",
			"http://RSI EVA",
			"http://RSI urlDispositifDepartAvantAgeLegal",
			"http://RSI urlInfosPenibilite");

	// @formatter:on

	private final ChecklistName checklistName;
	public final String urlAgeDepart;
	public final String urlDroits;
	public final String urlSimulMontant;
	public final String urlDispositifDepartAvantAgeLegal;
	public final String urlInfosPenibilite;

	private RegimeAligne(final ChecklistName checklistName,
			final String urlAgeDepart,
			final String urlDroits,
			final String urlSimulMontant,
			final String urlDispositifDepartAvantAgeLegal,
			final String urlInfosPenibilite) {
		this.checklistName = checklistName;
		this.urlAgeDepart = urlAgeDepart;
		this.urlDroits = urlDroits;
		this.urlSimulMontant = urlSimulMontant;
		this.urlDispositifDepartAvantAgeLegal = urlDispositifDepartAvantAgeLegal;
		this.urlInfosPenibilite = urlInfosPenibilite;
	}

	public ChecklistName getChecklistName() {
		return checklistName;
	}

}
