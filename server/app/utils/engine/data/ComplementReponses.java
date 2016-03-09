package utils.engine.data;

import static utils.JsonUtils.fromJson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import utils.engine.data.enums.ComplementQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class ComplementReponses {

	private static final Type TYPE = new TypeToken<Map<ComplementQuestionDescriptor, List<QuestionChoiceValue>>>() {
	}.getType();

	// Static

	public static ComplementReponses retrieveComplementReponsesFromJson(final String hidden_complementReponses) {
		final ComplementReponses liquidateurReponses = new ComplementReponses();
		if (hidden_complementReponses != null && !hidden_complementReponses.isEmpty()) {
			final Map<ComplementQuestionDescriptor, List<QuestionChoiceValue>> fromJson = fromJson(hidden_complementReponses, TYPE);
			liquidateurReponses.reponses = fromJson;
		}
		return liquidateurReponses;
	}

	// Attributs

	private Map<ComplementQuestionDescriptor, List<QuestionChoiceValue>> reponses;

	public ComplementReponses() {
		reponses = new HashMap<ComplementQuestionDescriptor, List<QuestionChoiceValue>>();
	}

	public Map<ComplementQuestionDescriptor, List<QuestionChoiceValue>> getReponses() {
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
		final ComplementReponses other = (ComplementReponses) obj;
		if (reponses == null) {
			if (other.reponses != null)
				return false;
		} else if (!reponses.equals(other.reponses))
			return false;
		return true;
	}

}
