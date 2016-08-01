package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_A;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_B;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_C;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP_AVANT_73;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.StringPairsList;

public class ResponsesHistoryToStringsConverterTest {

	private ResponsesHistoryToStringsConverter responsesHistoryToStringsConverter;

	@Before
	public void setup() {
		responsesHistoryToStringsConverter = new ResponsesHistoryToStringsConverter();
	}

	@Test
	public void should_return_empty_list_if_no_history() {

		final String liquidateurReponsesHistory = "";

		final StringPairsList questionsAndResponses = responsesHistoryToStringsConverter.convert(liquidateurReponsesHistory);

		final StringPairsList expected = new StringPairsList();
		assertThat(questionsAndResponses).isEqualTo(expected);
	}

	@Test
	public void should_return_list_of_pairs() {

		final String liquidateurReponsesHistory = "[{\"question\":\"QUESTION_A\",\"responses\":[\"NSA\"]},{\"question\":\"QUESTION_B\",\"responses\":[\"INDEP\"]},{\"question\":\"QUESTION_C\",\"responses\":[]},{\"question\":\"QUESTION_C\",\"responses\":[\"INDEP_AVANT_73\",\"INVALIDITE_RSI\"]}]";

		final StringPairsList questionsAndResponses = responsesHistoryToStringsConverter.convert(liquidateurReponsesHistory);

		final StringPairsList expected = new StringPairsList() {
			{
				add(QUESTION_A.getTitle(), QUESTION_A.getChoice(NSA).getText());
				add(QUESTION_B.getTitle(), QUESTION_B.getChoice(INDEP).getText());
				add(QUESTION_C.getTitle(), "Aucune");
				add(QUESTION_C.getTitle(), QUESTION_C.getChoice(INDEP_AVANT_73).getText() + "," + QUESTION_C.getChoice(INVALIDITE_RSI).getText());
			}
		};
		assertThat(questionsAndResponses).isEqualTo(expected);
	}
}
