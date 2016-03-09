package utils.engine.data;

import static utils.JsonUtils.fromJson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class LiquidateurReponses {

	private static final Type TYPE = new TypeToken<Map<LiquidateurQuestionDescriptor, List<QuestionChoiceValue>>>() {
	}.getType();

	// Static

	public static LiquidateurReponses retrieveLiquidateurReponsesFromJson(final String hidden_liquidateurReponses) {
		final LiquidateurReponses liquidateurReponses = new LiquidateurReponses();
		if (hidden_liquidateurReponses != null && !hidden_liquidateurReponses.isEmpty()) {
			final Map<LiquidateurQuestionDescriptor, List<QuestionChoiceValue>> fromJson = fromJson(hidden_liquidateurReponses, TYPE);
			liquidateurReponses.reponses = fromJson;
		}
		return liquidateurReponses;
	}

	// Attributs

	private Map<LiquidateurQuestionDescriptor, List<QuestionChoiceValue>> reponses;

	public LiquidateurReponses() {
		reponses = new HashMap<LiquidateurQuestionDescriptor, List<QuestionChoiceValue>>();
	}

	public Map<LiquidateurQuestionDescriptor, List<QuestionChoiceValue>> getReponses() {
		return reponses;
	}

	public boolean isEmpty() {
		return reponses == null || reponses.isEmpty();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reponses == null) ? 0 : reponses.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LiquidateurReponses other = (LiquidateurReponses) obj;
		if (reponses == null) {
			if (other.reponses != null)
				return false;
		} else if (!reponses.equals(other.reponses))
			return false;
		return true;
	}

}
