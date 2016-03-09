package utils;

import static org.junit.Assert.fail;

import java.util.List;

import utils.engine.data.ComplementReponses;
import utils.engine.data.LiquidateurReponses;
import utils.engine.data.QuestionChoice;
import utils.engine.data.enums.ComplementQuestionDescriptor;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class TestsUtils {

	public static String quote(final String jsonString) {
		return jsonString.replaceAll("''", "££").replaceAll("'", "\"").replaceAll("££", "'");
	}

	public static void assertReponseValuesAreInQuestions(final LiquidateurReponses reponses) {
		for (final LiquidateurQuestionDescriptor liquidateurQuestionDescriptor : reponses.getReponses().keySet()) {
			final List<QuestionChoiceValue> values = reponses.getReponses().get(liquidateurQuestionDescriptor);
			for (final QuestionChoiceValue value : values) {
				assertReponseValueIsInQuestion(liquidateurQuestionDescriptor, value);
			}
		}
	}

	public static void assertReponseValuesAreInQuestions(final ComplementReponses reponses) {
		for (final ComplementQuestionDescriptor complementQuestionDescriptor : reponses.getReponses().keySet()) {
			final List<QuestionChoiceValue> values = reponses.getReponses().get(complementQuestionDescriptor);
			for (final QuestionChoiceValue value : values) {
				assertReponseValueIsInQuestion(complementQuestionDescriptor, value);
			}
		}
	}

	// Méthodes privées

	private static void assertReponseValueIsInQuestion(final LiquidateurQuestionDescriptor liquidateurQuestionDescriptor, final QuestionChoiceValue value) {
		for (final QuestionChoice questionChoice : liquidateurQuestionDescriptor.getQuestionChoices()) {
			if (questionChoice.getValue() == value) {
				return;
			}
		}
		fail("La valeur " + value + " n'est pas parmi les choix possibles pour la question " + liquidateurQuestionDescriptor);
	}

	private static void assertReponseValueIsInQuestion(final ComplementQuestionDescriptor complementQuestionDescriptor, final QuestionChoiceValue value) {
		for (final QuestionChoice questionChoice : complementQuestionDescriptor.getQuestionChoices()) {
			if (questionChoice.getValue() == value) {
				return;
			}
		}
		fail("La valeur " + value + " n'est pas parmi les choix possibles pour la question " + complementQuestionDescriptor);
	}

}
