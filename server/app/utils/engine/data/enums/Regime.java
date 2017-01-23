package utils.engine.data.enums;

import static utils.engine.data.enums.Regime.RegimeType.BASE_ALIGNE;
import static utils.engine.data.enums.Regime.RegimeType.BASE_AUTRE;
import static utils.engine.data.enums.Regime.RegimeType.COMPLEMENTAIRE;

import java.util.ArrayList;
import java.util.List;

import play.Logger;

public enum Regime {
	AGIRC_ARRCO(COMPLEMENTAIRE, "AGIRC-ARRCO","0068"),
	BFSP(BASE_AUTRE, "Caisse de retraite de la Banque de France","2223"),
	CARCD(BASE_AUTRE, "Caisse autonome de retraite des chirurgiens dentistes","1158"),
	CARMF(BASE_AUTRE, "Caisse autonome de retraite des médecins de France","1037"),
	CARPIMKO(BASE_AUTRE, "Caisse autonome de retraite et de prévoyance des auxiliaires médicaux","1036"),
	CARPV(BASE_AUTRE, "Caisse autonome de retraite et de prévoyance des vétérinaires","1155"),
	CARSAF(BASE_AUTRE, "Caisse autonome de retraite des sages-femmes françaises","1154"),
	CAVAMAC(BASE_AUTRE, "Caisse de retraite des agents d'assurance","1033"),
	CAVEC(BASE_AUTRE, "Caisse d'assurance vieillesse des experts comptables","1034"),
	CAVIMAC(BASE_ALIGNE, "Caisse d'assurance vieillesse invalidité et maladie des cultes","0263"),
	CAVOM(BASE_AUTRE, "Caisse d'assurance vieillesse des officiers ministériels","1152"),
	CAVP(BASE_AUTRE, "Caisse d'assurance vieillesse des pharmaciens","1153"),
	CCMSA(BASE_ALIGNE, "MSA", "0017"),
	CCMSA_NSA(BASE_ALIGNE, "Regime des exploitants non salariés agricoles", "2295"),
	CCMSA_SA(BASE_ALIGNE, "Regime des salariés agricoles", "2294"),
	CIPAV(BASE_AUTRE, "Caisse de retraite des professions libérales, architectes et divers autres","1038"),
	CNAV(BASE_ALIGNE, "CNAV", "0018"),
	CNBF(BASE_AUTRE, "Caisse nationale des barreaux français", "1157"),
	CNIEG(BASE_AUTRE, "Caisse nationale des industries électriques et gazières", "0067"),
	CNRACL(BASE_AUTRE, "Régime obligatoire de base des agents des collectivités territoriales", "0066"),
	CPRPSNCF(BASE_AUTRE, "Caisse de prévoyance et de retraite du personnel de la SNCF", "0065"),
	CRE(BASE_AUTRE, "RATP", "0073"),
	CRN(BASE_AUTRE, "Caisse de retraite des notaires","1151"),
	CRPCF(BASE_AUTRE, "Comédie française","0090"),
	CRPNPAC(BASE_AUTRE, "Caisse de retraite du personnel navigant professionnel de l'aéronautique civile", "2142"),
	CROPERA(BASE_AUTRE, "Opéra national de Paris", "0106"),
	CRPCEN(BASE_AUTRE, "Caisse de retraite et de prévoyance des clercs et employés de notaires", "0089"),
	ENIM(BASE_AUTRE, "Régime social des marins", "1041"),
	FSPOEIE(BASE_AUTRE, "Fonds spécial des pensions des ouvriers des établissements industriels de l'État", "0078"),
	IRCANTEC(COMPLEMENTAIRE, "IRCANTEC", "0069"),
	IRCEC(COMPLEMENTAIRE, "Caisse nationale de retraite complémentaire des artistes auteurs", "2139"),
	MINES(BASE_AUTRE, "Caisse de retraite des Mines", "0075"),
	PORT_STRASBOURG(BASE_AUTRE, "Port autonome de Strasbourg", "0472"),
	RAFP(COMPLEMENTAIRE, "Agents titulaires de l'Etat, des CT et de la FPH", "2141"),
	RCI(COMPLEMENTAIRE, "Régime complémentaire des indépendants", "3029"),
	RSI_ARTISANS(BASE_ALIGNE, "Retraite de base des artisans", "0070"),
	RSI_COMMERCANTS(BASE_ALIGNE, "Retraite de base des commerçants et industriels", "0071"),
	SRE(BASE_AUTRE, "Service des retraites de l'Etat", "0074");

	public enum RegimeType {
		BASE_ALIGNE, BASE_AUTRE, COMPLEMENTAIRE
	};

	private final RegimeType type;
	private final String nom;
	private final String code;

	Regime(final RegimeType type, final String nom, final String code) {
		this.type = type;
		this.nom = nom;
		this.code = code;
	}

	public RegimeType getType() {
		return type;
	}

	public String getNom() {
		return nom;
	}

	public static Regime[] fromStringList(final String stringList) {
		if (stringList == null) {
			return new Regime[0];
		}
		final List<Regime> regimes = new ArrayList<>();
		final String[] regimesStr = stringList
				.trim()
				.replace("AGIRC ARRCO", "AGIRC_ARRCO")
				.replace("AGIRC-ARRCO", "AGIRC_ARRCO")
				.replace("ORGANIC", "RSI")
				.replace("CANCAVA", "RSI")
				.replace("CCMSA", "MSA")
				.split(",");
		for (final String regimeStr : regimesStr) {
			try {
				regimes.add(Regime.valueOf(regimeStr));
			} catch (final IllegalArgumentException e) {
				Logger.error(new Exception("stack"), "Nom de regime innatendu : '" + regimeStr + "' . Liste reçue : '" + stringList + "'");
			}
		}
		return regimes.toArray(new Regime[regimes.size()]);
	}

	public static Regime valueOfNom(final String nom) {
		final String nomTrimed = nom.trim();
		if (nomTrimed.equals("AGIRC-ARRCO") || nomTrimed.equals("AGIRC ARRCO")) {
			return AGIRC_ARRCO;
		}
		if (nomTrimed.equals("ORGANIC")) {
			return Regime.RSI_COMMERCANTS;
		}
		if (nomTrimed.equals("CANCAVA")) {
			return Regime.RSI_ARTISANS;
		}
		return valueOf(nomTrimed);
	}

	public String getCode() {
		return code;
	}

}
