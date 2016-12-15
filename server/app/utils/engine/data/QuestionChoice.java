package utils.engine.data;

import utils.engine.data.enums.QuestionChoiceValue;

public class QuestionChoice {

	private final String text;
	private final String textChecklist;
	private final QuestionChoiceValue value;
	
	private Boolean checked = Boolean.FALSE;

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public QuestionChoice(final String text, final QuestionChoiceValue value) {
		this.text = text;
		this.textChecklist = null;
		this.value = value;
	}
	
	public QuestionChoice(final String text, final String textChecklist, final QuestionChoiceValue value) {
		this.text = text;
		this.textChecklist = textChecklist;
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
		return "QuestionChoice [text=" + text + ", value=" + value + ", checked=" + checked + "]";
	}

	public String getTextChecklist() {
		return textChecklist;
	}

}
