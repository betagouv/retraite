package utils.engine.data;

import utils.engine.data.enums.QuestionChoiceValue;

public class QuestionChoice {

	private final String text;
	private final QuestionChoiceValue value;

	public QuestionChoice(final String text, final QuestionChoiceValue value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public QuestionChoiceValue getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "QuestionChoice[text=" + text + ", value=" + value + "]";
	}

}
