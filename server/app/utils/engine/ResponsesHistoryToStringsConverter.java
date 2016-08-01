package utils.engine;

import static utils.JsonUtils.fromJson;

import java.util.List;

import utils.engine.data.QuestionAndResponses;
import utils.engine.data.QuestionAndResponsesList;
import utils.engine.data.StringPair;
import utils.engine.data.StringPairsList;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class ResponsesHistoryToStringsConverter {

	public StringPairsList convert(final String liquidateurReponsesHistory) {
		final StringPairsList result = new StringPairsList();
		if (!liquidateurReponsesHistory.isEmpty()) {
			final List<QuestionAndResponses> list = fromJson(liquidateurReponsesHistory, QuestionAndResponsesList.class);
			for (final QuestionAndResponses questionAndResponses : list) {
				final LiquidateurQuestionDescriptor liquidateurQuestionDescriptor = LiquidateurQuestionDescriptor.valueOf(questionAndResponses.question);
				result.add(createQuestionAndResponse(liquidateurQuestionDescriptor, questionAndResponses.responses));
			}
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
		if (responsesStr.isEmpty()) {
			responsesStr = "Aucune";
		}
		return new StringPair(liquidateurQuestionDescriptor.getTitle(), responsesStr);
	}

}
