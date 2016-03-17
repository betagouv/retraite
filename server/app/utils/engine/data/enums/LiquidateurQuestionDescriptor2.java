package utils.engine.data.enums;

import static utils.engine.data.enums.QuestionType.SIMPLE;

import utils.engine.data.QuestionChoice;

public enum LiquidateurQuestionDescriptor2 {

	// @formatter:off

	QUESTION_A(
		"Au cours de votre carrière, avez-vous été ?",
			SIMPLE,
			choice("Chef d'exploitation ou d'entreprise agricole", QuestionChoiceValue.NSA),
			choice("Salarié agricole", QuestionChoiceValue.SA),
			choice("Les deux", QuestionChoiceValue.DEUX_ACTIVITES)
	),

	QUESTION_B(
		"Quelle est votre activité actuelle ou la dernière activité que vous avez exercée ?",
			SIMPLE,
			choice("Salarié (non agricole)", QuestionChoiceValue.SALARIE),
			choice("Chef d'exploitation", QuestionChoiceValue.NSA),
			choice("Salarié agricole", QuestionChoiceValue.SA),
			choice("Artisan ou commerçant", QuestionChoiceValue.INDEP),
			choice("Conjoint collaborateur d'un artisan commerçant", QuestionChoiceValue.CONJOINT_INDEP),
			choice("Deux activités en même temps", QuestionChoiceValue.DEUX_ACTIVITES)
	);

	// @formatter:on

	@SuppressWarnings("unused")
	private final String title;
	@SuppressWarnings("unused")
	private final QuestionType questionType;
	private final QuestionChoice[] questionChoices;

	LiquidateurQuestionDescriptor2(final String title, final QuestionType questionType, final QuestionChoice... questionChoices) {
		this.title = title;
		this.questionType = questionType;
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
