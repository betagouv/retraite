package utils.engine.data.enums;

import static utils.engine.data.enums.Regime.RegimeType.BASE_ALIGNE;
import static utils.engine.data.enums.Regime.RegimeType.BASE_AUTRE;
import static utils.engine.data.enums.Regime.RegimeType.COMPLEMENTAIRE;

import java.util.ArrayList;
import java.util.List;

import play.Logger;

public enum Regime {
	AGIRC_ARRCO(COMPLEMENTAIRE, "AGIRC ARRCO"),
	BFSP(BASE_AUTRE, "Banque de France"),
	CARCD(BASE_AUTRE, "Caisse d'assurance retraite des chirurgiens dentistes"),
	CARMF(BASE_AUTRE, "Caisse autonome de retraite des médecins de France"),
	CARPIMKO(BASE_AUTRE, "Caisse de retraite des auxiliaires médicaux"),
	CARPV(BASE_AUTRE, "Caisse autonome de retraite et de prévoyance des vétérinaires"),
	CARSAF(BASE_AUTRE, "Caisse de retraite des sages femmes françaises"),
	CAVAMAC(BASE_AUTRE, "Caisse de retraite des agents d'assurance"),
	CAVEC(BASE_AUTRE, "Caisse d'assurance vieillesse des experts comptables"),
	CAVIMAC(BASE_ALIGNE, "Caisse d'assurance vieillesse invalidité et maladie des cultes"),
	CAVOM(BASE_AUTRE, "Caisse d'assurance vieillesse des officiers ministériels"),
	CAVP(BASE_AUTRE, "Caisse d'assurance vieillesse des pharmaciens"),
	CIPAV(BASE_AUTRE, "Caisse de retraite des professions libérales, architectes et divers autres"),
	CNAV(BASE_ALIGNE, "CNAV"),
	CNBF(BASE_AUTRE, "Caisse nationale des barreaux français"),
	CNIEG(BASE_AUTRE, "Caisse nationale des industries électriques et gazières"),
	CNRACL(BASE_AUTRE, "Régime obligatoire de base des agents des collectivités territoriales"),
	CPRP(BASE_AUTRE, "SNCF"),
	CRE(BASE_AUTRE, "RATP"),
	CRN(BASE_AUTRE, "Caisse de retraite des notaires"),
	CRPCF(BASE_AUTRE, "Comédie française"),
	CROPERA(BASE_AUTRE, "Opéra national de Paris"),
	CRPCEN(BASE_AUTRE, "Caisse de retraite et de prévoyance des clercs et employés de notaires"),
	ENIM(BASE_AUTRE, "Régime social des marins"),
	FSPOEIE(BASE_AUTRE, "Fonds spécial des pensions des ouvriers des établissements industriels de l'État"),
	IRCANTEC(COMPLEMENTAIRE, "IRCANTEC"),
	IRCEC(COMPLEMENTAIRE, "Caisse nationale de retraite complémentaire des artistes auteurs"),
	MSA(BASE_ALIGNE, "MSA"),
	RAFP(COMPLEMENTAIRE, "Agents titulaires de l'Etat, des CT et de la FPH"),
	RSI(BASE_ALIGNE, "RSI"),
	SRE(BASE_AUTRE, "Service des retraites de l'Etat");

	public enum RegimeType {
		BASE_ALIGNE, BASE_AUTRE, COMPLEMENTAIRE
	};

	private final RegimeType type;
	private final String nom;

	Regime(final RegimeType type, final String nom) {
		this.type = type;
		this.nom = nom;
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
				Logger.error(new Exception("stack"), "Nom de regime innatendu : '" + regimeStr + "' . Liste reçue : " + stringList);
			}
		}
		return regimes.toArray(new Regime[regimes.size()]);
	}

	public static Regime valueOfNom(final String nom) {
		if (nom.equals("AGIRC-ARRCO")) {
			return AGIRC_ARRCO;
		}
		if (nom.equals("CCMSA")) {
			return MSA;
		}
		if (nom.equals("ORGANIC") || nom.equals("CANCAVA")) {
			return RSI;
		}
		return valueOf(nom);
	}

}
