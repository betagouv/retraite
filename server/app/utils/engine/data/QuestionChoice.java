package utils.engine.data;

import utils.engine.data.enums.ChecklistSelector;
import utils.engine.data.enums.QuestionChoiceValue;

public class QuestionChoice {

	private final String text;
	private final QuestionChoiceValue value;
	private final ChecklistSelector selector;

	public QuestionChoice(final String text, final QuestionChoiceValue value, final ChecklistSelector selector) {
		this.text = text;
		this.value = value;
		this.selector = selector;
	}

	public String getText() {
		return text;
	}

	public QuestionChoiceValue getValue() {
		return value;
	}

	public ChecklistSelector getSelector() {
		return selector;
	}

	@Override
	public String toString() {
		return "QuestionChoice[text=" + text + ", value=" + value + ", selector=" + selector + "]";
	}

}
