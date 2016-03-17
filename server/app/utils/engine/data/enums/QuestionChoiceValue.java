package utils.engine.data.enums;

import static utils.JsonUtils.fromJson;

import java.util.List;

public enum QuestionChoiceValue {

	OUI, NON, CONJOINT, AUTRE, DEUX_ACTIVITES, CPAM, MSA, INDEP,
	INDEP_AVANT_73, INVALIDITE_RSI, PENIBILITE, HORS_TERRITOIRE_FRANCAIS,
	AUCUNE, SA, NSA, CONJOINT_INDEP, RSI, NE_SAIT_PAS, SOUVENT, PARFOIS, JAMAIS, SALARIE;

	public static QuestionChoiceValue getFromJsonArray(final String jsonStr) {
		final List<String> responses = fromJson(jsonStr, List.class);
		return QuestionChoiceValue.valueOf(responses.get(0));
	}

}
