package utils.engine.data.enums;

import static utils.JsonUtils.fromJson;

import java.util.ArrayList;
import java.util.List;

public enum QuestionChoiceValue {

	OUI, NON, CONJOINT, AUTRE, DEUX_ACTIVITES, CPAM, MSA, INDEP,
	INDEP_AVANT_73, INVALIDITE_RSI, PENIBILITE, HORS_TERRITOIRE_FRANCAIS,
	AUCUNE, SA, NSA, CONJOINT_INDEP, RSI, NE_SAIT_PAS, SOUVENT, PARFOIS, JAMAIS, SALARIE;

	public static QuestionChoiceValue getFirstFromJsonArray(final String jsonStr) {
		if (jsonStr == null) {
			return null;
		}
		final List<String> responses = fromJson(jsonStr, List.class);
		return QuestionChoiceValue.valueOf(responses.get(0));
	}

	public static List<QuestionChoiceValue> getFromJsonArray(final String jsonStr) {
		if (jsonStr == null) {
			return null;
		}
		final List<String> responses = fromJson(jsonStr, List.class);
		final List<QuestionChoiceValue> values = new ArrayList<>();
		for (final String response : responses) {
			values.add(QuestionChoiceValue.valueOf(response));
		}
		return values;
	}

}
