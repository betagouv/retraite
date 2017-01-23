package utils.engine.data.enums;

import static utils.engine.data.enums.Regime.RegimeType.BASE_ALIGNE;
import static utils.engine.data.enums.Regime.RegimeType.BASE_AUTRE;
import static utils.engine.data.enums.Regime.RegimeType.COMPLEMENTAIRE;

import java.util.ArrayList;
import java.util.List;

import play.Logger;

public enum Regime {
	AGIRC_ARRCO(COMPLEMENTAIRE, "0068"),
	BFSP(BASE_AUTRE, "2223"),
	CARCD(BASE_AUTRE, "1158"),
	CARMF(BASE_AUTRE, "1037"),
	CARPIMKO(BASE_AUTRE, "1036"),
	CARPV(BASE_AUTRE, "1155"),
	CARSAF(BASE_AUTRE, "1154"),
	CAVAMAC(BASE_AUTRE, "1033"),
	CAVEC(BASE_AUTRE, "1034"),
	CAVIMAC(BASE_ALIGNE, "0263"),
	CAVOM(BASE_AUTRE, "1152"),
	CAVP(BASE_AUTRE, "1153"),
	CCMSA(BASE_ALIGNE, "0017"),
	CCMSA_NSA(BASE_ALIGNE, "2295"),
	CCMSA_SA(BASE_ALIGNE, "2294"),
	CIPAV(BASE_AUTRE, "1038"),
	CNAV(BASE_ALIGNE, "0018"),
	CNBF(BASE_AUTRE, "1157"),
	CNIEG(BASE_AUTRE, "0067"),
	CNRACL(BASE_AUTRE, "0066"),
	CPRPSNCF(BASE_AUTRE, "0065"),
	CRE(BASE_AUTRE, "0073"),
	CRN(BASE_AUTRE, "1151"),
	CRPCF(BASE_AUTRE, "0090"),
	CRPNPAC(BASE_AUTRE, "2142"),
	CROPERA(BASE_AUTRE, "0106"),
	CRPCEN(BASE_AUTRE, "0089"),
	ENIM(BASE_AUTRE, "1041"),
	FSPOEIE(BASE_AUTRE, "0078"),
	IRCANTEC(COMPLEMENTAIRE, "0069"),
	IRCEC(COMPLEMENTAIRE, "2139"),
	MINES(BASE_AUTRE, "0075"),
	PORT_STRASBOURG(BASE_AUTRE, "0472"),
	RAFP(COMPLEMENTAIRE, "2141"),
	RCI(COMPLEMENTAIRE, "3029"),
	RSI_ARTISANS(BASE_ALIGNE, "0070"),
	RSI_COMMERCANTS(BASE_ALIGNE, "0071"),
	SRE(BASE_AUTRE, "0074");

	public enum RegimeType {
		BASE_ALIGNE, BASE_AUTRE, COMPLEMENTAIRE
	};

	private final RegimeType type;
	private final String code;

	Regime(final RegimeType type, final String code) {
		this.type = type;
		this.code = code;
	}

	public RegimeType getType() {
		return type;
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
