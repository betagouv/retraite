package utils.engine.data.enums;

public enum RegimeAligne {

	// @formatter:off

	CNAV("http://CNAV quand-prendre-sa-retraite",
			"http://CNAV mon-releve-de-carriere",
			"http://CNAV EVA",
			"http://CNAV urlDispositifDepartAvantAgeLegal",
			"http://CNAV urlInfosPenibilite",
			"http://CNAV urlInfosDepartRetraite"),

	MSA("http://www.msa.fr/lfr/retraite/conditions-age-depart-taux-plein",
			"http://MSA mon-releve-de-carriere",
			"http://MSA EVA",
			"http://MSA urlDispositifDepartAvantAgeLegal",
			"http://MSA urlInfosPenibilite",
			"http://MSA urlInfosDepartRetraite"),

	RSI("http://rsi.fr/quand-prendre-sa-retraite",
			"http://rsi.fr/mon-releve-de-carriere",
			"http://RSI EVA",
			"http://RSI urlDispositifDepartAvantAgeLegal",
			"http://RSI urlInfosPenibilite",
			"http://www.rsi.fr/partir-en-retraite");

	// @formatter:on

	public final String urlAgeDepart;
	public final String urlDroits;
	public final String urlSimulMontant;
	public final String urlDispositifDepartAvantAgeLegal;
	public final String urlInfosPenibilite;
	public final String urlInfosDepartRetraite;

	private RegimeAligne(final String urlAgeDepart,
			final String urlDroits,
			final String urlSimulMontant,
			final String urlDispositifDepartAvantAgeLegal,
			final String urlInfosPenibilite,
			final String urlInfosDepartRetraite) {
		this.urlAgeDepart = urlAgeDepart;
		this.urlDroits = urlDroits;
		this.urlSimulMontant = urlSimulMontant;
		this.urlDispositifDepartAvantAgeLegal = urlDispositifDepartAvantAgeLegal;
		this.urlInfosPenibilite = urlInfosPenibilite;
		this.urlInfosDepartRetraite = urlInfosDepartRetraite;
	}

}
