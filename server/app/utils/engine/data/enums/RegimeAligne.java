package utils.engine.data.enums;

public enum RegimeAligne {

	// @formatter:off

	CNAV(
			"https://www.lassuranceretraite.fr/portail-info/home/salaries/age-et-montant-de-ma-retraite/a-quel-age-vais-je-partir.html;CMS_PUB=7F81DF2223FB37F324C1DD5309DF6D28",
			"https://www.lassuranceretraite.fr/portail-info/home/salaries/mon-releve-de-carriere.html;CMS_PUB=E0F9E2DF7A8CB8BD51E22B21F4D5A573",
			"https://www.lassuranceretraite.fr/portail-services-ihm/index.html#/sec/afficherIframe/EVA",
			"https://www.lassuranceretraite.fr/portail-info/home/salaries/age-et-montant-de-ma-retraite/a-quel-age-vais-je-partir/departs-anticipes.html;CMS_PUB=7F81DF2223FB37F324C1DD5309DF6D28#header-b464966e-8d3d-4af3-85ef-5ab77cd8d041",
			"https://www.lassuranceretraite.fr/portail-info/home/salaries/mes-droits-selon-de-mon-parcours/penibilite-au-travail.html",
			"https://www.lassuranceretraite.fr/portail-info/home/salaries/mes-demarches/je-demande-ma-retraite/mes-demarches-pour-partir.html"),

	MSA(
			"http://www.msa.fr/lfr/retraite/conditions-age-depart-taux-plein",
			"https://monespaceprive.msa.fr/lfr/c/bookmarks/open_entry?entryId=39902446", // ou https://monespaceprive.msa.fr (Rubrique : Mes services en ligne / Mes déclarations, mes demandes / Retraite / Informations sur ma retraite)
			"https://monespaceprive.msa.fr/lfr/c/bookmarks/open_entry?entryId=39902446", // ou https://monespaceprive.msa.fr (Rubrique : Mes services en ligne / Mes déclarations, mes demandes / Retraite / Informations sur ma retraite)
			"http://www.msa.fr/retraite/depart",
			"http://www.msa.fr/retraite/penibilite",
			"http://www.msa.fr/retraite"),

	RSI(
			"http://rsi.fr/quand-prendre-sa-retraite",
			"http://rsi.fr/mon-releve-de-carriere",
			"http://www.info-retraite.fr/",
			"http://rsi.fr/retraite-anticipee.html",
			"http://rsi.fr/retraite-pour-penibilite",
			"http://rsi.fr/partir-en-retraite");

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
