package utils.engine.data;

import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.QuestionDescriptor;

public class QuestionCondition {

	public QuestionDescriptor key;

	public QuestionChoiceValue value;

	public QuestionCondition(final QuestionDescriptor key, final QuestionChoiceValue value) {
		this.key = key;
		this.value = value;
	}

}
