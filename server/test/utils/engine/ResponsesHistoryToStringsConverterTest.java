package utils.engine;

import static org.fest.assertions.Assertions.assertThat;

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
	public void test() {

		final String liquidateurReponsesHistory = "{\"QUESTION_A\":[\"NSA\"],\"QUESTION_B\":[\"INDEP\"]}";

		final StringPairsList questionsAndResponses = responsesHistoryToStringsConverter.convert(liquidateurReponsesHistory);

		final StringPairsList expected = new StringPairsList() {
			{
				add("Au cours de votre carrière, avez-vous été ?", "Chef d'exploitation ou d'entreprise agricole");
				add("Quelle est votre activité actuelle ou la dernière activité que vous avez exercée ?", "Artisan ou commerçant");
			}
		};
		assertThat(questionsAndResponses).isEqualTo(expected);
	}
}
