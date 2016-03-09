package utils.doc;

import models.Condition;

public class ConverterCondition {

	public String convert(final Condition condition) {
		final String type = condition.props.get("type");
		if (type.equals("delai")) {
			final String plusOuMoins = condition.props.get("plusOuMoins");
			final String nombre = condition.props.get("nombre");
			final String unite = condition.props.get("unite");
			final String s = "Si le délai restant avant la date de départ prévue est "
					+ (plusOuMoins.equals("PLUS") ? "supérieur" : "inférieur") + " à "
					+ nombre + " " + (unite.equals("MOIS") ? "mois" : (nombre.equals("1") ? "année" : "années"));
			return s;
		}
		if (type.equals("regimeDetecte")) {
			final String regime = condition.props.get("regime");
			if (regime.equals("agirc-arrco")) {
				return "Si régimes AGIRC-ARRCO détectés";
			}
			if (regime.equals("regimes-base-hors-alignés")) {
				return "Si régimes de base hors alignés détectés";
			}
			if (regime.equals("regimes-complémentaires-hors-agirc-arrco")) {
				return "Si régimes complémentaires hors AGIRC-ARRCO détectés";
			}
			return "Si régimes AGIRC-ARRCO détectés";
		}
		if (type.equals("statut")) {
			final String statut = condition.props.get("statut");
			if (statut.equals("nsa")) {
				return "Si l’assuré appartient à la catégorie NSA";
			}
			if (statut.equals("sa")) {
				return "Si l’assuré appartient à la catégorie SA";
			}
			if (statut.equals("conjoint-collaborateur")) {
				return "Si l’assuré est un conjoint collaborateur";
			}
			if (statut.equals("chef-entreprise")) {
				return "Si l’assuré est un chef d'entreprise";
			}
			throw new IllegalArgumentException("Unexpected statut '" + statut + "'");
		}
		if (type.equals("carriere-a-reconstituer")) {
			return "Si l’assuré doit reconstituer sa carrière";
		}
		throw new IllegalArgumentException("Unexpected condition type '" + type + "'");
	}

}
