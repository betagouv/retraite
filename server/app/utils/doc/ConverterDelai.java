package utils.doc;

import models.Delai;
import models.Delai.Unite;

public class ConverterDelai {

	public String convert(final Delai delai) {
		if (delai == null) {
			return "Non défini !";
		}
		switch (delai.type) {
		case AUCUN:
			return "Aucun";
		case DESQUE:
			return "Dès que possible";
		case AUPLUS:
			return "Au plus tard " + delai.min + " " + (delai.unite == Unite.MOIS ? "mois" : (delai.min == 1 ? "année" : "années"))
					+ " avant la date de départ prévue";
		case ENTRE:
			return "Entre " + delai.min + " et " + delai.max + " " + (delai.unite == Unite.MOIS ? "mois" : "années") + " avant la date de départ prévue";
		case APARTIR:
			return "A partir de " + delai.min + " " + (delai.unite == Unite.MOIS ? "mois" : (delai.min == 1 ? "année" : "années"))
					+ " avant la date de départ prévue";
		}
		throw new IllegalArgumentException("Unexpected type '" + delai.type + "' for delai");
	}

}
