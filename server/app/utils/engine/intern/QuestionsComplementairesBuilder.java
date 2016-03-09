package utils.engine.intern;

import static utils.engine.data.enums.ComplementQuestionDescriptor.ACCORD_INFO_RELEVE_CARRIERE;
import static utils.engine.data.enums.ComplementQuestionDescriptor.CONSULT_RELEVE_CARRIERE;
import static utils.engine.data.enums.ComplementQuestionDescriptor.DEMARCHES_EN_LIGNE;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;

import java.util.ArrayList;
import java.util.List;

import utils.engine.data.QuestionComplementaire;
import utils.engine.data.QuestionCondition;
import utils.engine.data.enums.ComplementQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class QuestionsComplementairesBuilder {

	public List<QuestionComplementaire> buildQuestions() {
		final List<QuestionComplementaire> questions = new ArrayList<>();
		questions.add(createQuestionsOfType(DEMARCHES_EN_LIGNE));
		questions.add(createQuestionsOfType(CONSULT_RELEVE_CARRIERE));
		questions.add(createQuestionsOfType(ACCORD_INFO_RELEVE_CARRIERE, CONSULT_RELEVE_CARRIERE, OUI));
		return questions;
	}

	private QuestionComplementaire createQuestionsOfType(final ComplementQuestionDescriptor type) {
		final QuestionComplementaire question = new QuestionComplementaire();
		question.type = type;
		return question;
	}

	private QuestionComplementaire createQuestionsOfType(final ComplementQuestionDescriptor type, final ComplementQuestionDescriptor conditionKey,
			final QuestionChoiceValue conditionValue) {
		final QuestionComplementaire question = createQuestionsOfType(type);
		question.condition = new QuestionCondition(conditionKey, conditionValue);
		return question;
	}

}
