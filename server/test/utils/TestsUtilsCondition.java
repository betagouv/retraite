package utils;

import java.util.HashMap;

import models.Condition;

public class TestsUtilsCondition {

	public static Condition createConditionDelai(final String plusOuMoins, final String nombre, final String unite) {
		final Condition condition = createConditionWithType("delai");
		condition.props.put("type", "delai");
		condition.props.put("plusOuMoins", plusOuMoins);
		condition.props.put("nombre", nombre);
		condition.props.put("unite", unite);
		return condition;
	}

	public static Condition createConditionStatut(final String statut) {
		final Condition condition = createConditionWithType("statut");
		condition.props.put("statut", statut);
		return condition;
	}

	public static Condition createConditionRegimeDetecte(final String regime) {
		final Condition condition = createConditionWithType("regimeDetecte");
		condition.props.put("regime", regime);
		return condition;
	}

	public static Condition createConditionWithType(final String type) {
		final Condition condition = new Condition();
		condition.props = new HashMap<String, String>() {
			{
				put("type", type);
			}
		};
		return condition;
	}

}
