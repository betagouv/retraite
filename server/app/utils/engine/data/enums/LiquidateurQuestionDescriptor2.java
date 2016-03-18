package utils.engine.data.enums;

import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP_AVANT_73;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionChoiceValue.SALARIE;
import static utils.engine.data.enums.QuestionMandatoryOrOptionnal.MANDATORY;
import static utils.engine.data.enums.QuestionMandatoryOrOptionnal.OPTIONNAL;
import static utils.engine.data.enums.QuestionType.MULTIPLE;
import static utils.engine.data.enums.QuestionType.SIMPLE;

import utils.engine.data.QuestionChoice;

public enum LiquidateurQuestionDescriptor2 {

	// @formatter:off

	QUESTION_A(
		"Au cours de votre carrière, avez-vous été ?",
			SIMPLE,
			MANDATORY,
			choice("Chef d'exploitation ou d'entreprise agricole", NSA),
			choice("Salarié agricole", SA),
			choice("Les deux", DEUX_ACTIVITES)
	),

	QUESTION_B(
		"Quelle est votre activité actuelle ou la dernière activité que vous avez exercée ?",
			SIMPLE,
			MANDATORY,
			choice("Salarié (non agricole)", SALARIE),
			choice("Chef d'exploitation", NSA),
			choice("Salarié agricole", SA),
			choice("Artisan ou commerçant", INDEP),
			choice("Conjoint collaborateur d'un artisan commerçant", CONJOINT_INDEP),
			choice("Deux activités en même temps", DEUX_ACTIVITES)
	),

	QUESTION_C(
		"Êtes-vous dans l'une ou plusieurs des situations suivantes ?",
			MULTIPLE,
			OPTIONNAL,
			choice("J'ai exercé une activité d'indépendant avant 1973", INDEP_AVANT_73),
			choice("Je reçois une pension d'invalidité versée par le RSI", INVALIDITE_RSI),
			choice("Je souhaite bénéficier du dispositif de retraite pour pénibilité", PENIBILITE)
	);

	// @formatter:on

	@SuppressWarnings("unused")
	private final String title;
	@SuppressWarnings("unused")
	private final QuestionType questionType;
	@SuppressWarnings("unused")
	private final QuestionMandatoryOrOptionnal questionMandatoryOrOptionnal;
	private final QuestionChoice[] questionChoices;

	LiquidateurQuestionDescriptor2(final String title, final QuestionType questionType, final QuestionMandatoryOrOptionnal questionMandatoryOrOptionnal,
			final QuestionChoice... questionChoices) {
		this.title = title;
		this.questionType = questionType;
		this.questionMandatoryOrOptionnal = questionMandatoryOrOptionnal;
		this.questionChoices = questionChoices;
	}

	public QuestionChoice[] getQuestionChoices() {
		return questionChoices;
	}

	private static QuestionChoice choice(final String text, final QuestionChoiceValue value) {
		return choice(text, value, null);
	}

	private static QuestionChoice choice(final String text, final QuestionChoiceValue value, final ChecklistSelector selector) {
		return new QuestionChoice(text, value, selector);
	}

	public boolean isLast() {
		final LiquidateurQuestionDescriptor2[] values = values();
		return this == values[values.length - 1];
	}

	public boolean isBefore(final LiquidateurQuestionDescriptor2 otherStep) {
		final LiquidateurQuestionDescriptor2[] values = values();
		for (final LiquidateurQuestionDescriptor2 desc : values) {
			if (desc == otherStep) {
				return false;
			}
			if (desc == this) {
				return true;
			}
		}
		return true;
	}

	public QuestionChoice getChoice(final QuestionChoiceValue value) {
		for (final QuestionChoice questionChoice : questionChoices) {
			if (questionChoice.getValue() == value) {
				return questionChoice;
			}
		}
		return null;
	}

}
