package utils.engine;

import java.util.List;
import java.util.Map;

import utils.JsonUtils;
import utils.engine.data.StringPair;
import utils.engine.data.StringPairsList;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class ResponsesHistoryToStringsConverter {

	public StringPairsList convert(final String liquidateurReponsesHistory) {
		final Map<String, List<String>> map = JsonUtils.fromJson(liquidateurReponsesHistory, Map.class);
		final StringPairsList result = new StringPairsList();
		for (final String key : map.keySet()) {
			final LiquidateurQuestionDescriptor liquidateurQuestionDescriptor = LiquidateurQuestionDescriptor.valueOf(key);
			result.add(createQuestionAndResponse(liquidateurQuestionDescriptor, map.get(key)));
		}
		return result;
	}

	private StringPair createQuestionAndResponse(final LiquidateurQuestionDescriptor liquidateurQuestionDescriptor, final List<String> responses) {
		String responsesStr = "";
		for (final String response : responses) {
			if (!responsesStr.isEmpty()) {
				responsesStr += ",";
			}
			responsesStr += liquidateurQuestionDescriptor.getChoice(QuestionChoiceValue.valueOf(response)).getText();
		}
		return new StringPair(liquidateurQuestionDescriptor.getTitle(), responsesStr);
	}

}
