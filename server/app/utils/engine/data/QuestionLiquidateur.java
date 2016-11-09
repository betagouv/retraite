package utils.engine.data;

import java.util.List;

import utils.engine.data.enums.LiquidateurQuestionDescriptor;

public class QuestionLiquidateur {

	public LiquidateurQuestionDescriptor liquidateurQuestionDescriptor;

	public List<QuestionChoice> choices;

	@Override
	public String toString() {
		return "QuestionLiquidateur [liquidateurQuestionDescriptor=" + liquidateurQuestionDescriptor + ", choices="
				+ choices + "]";
	}

}
