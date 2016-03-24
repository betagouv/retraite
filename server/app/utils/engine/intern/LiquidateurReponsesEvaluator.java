package utils.engine.intern;

import static utils.engine.data.enums.ComplementQuestionDescriptor.ACCORD_INFO_RELEVE_CARRIERE;
import static utils.engine.data.enums.ComplementQuestionDescriptor.CONSULT_RELEVE_CARRIERE;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;

import java.util.List;

import utils.engine.data.ComplementReponses;
import utils.engine.data.enums.ComplementQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class LiquidateurReponsesEvaluator {

	public boolean isCarriereAReconstituer(final ComplementReponses reponses) {
		return question(reponses, CONSULT_RELEVE_CARRIERE).hasNotAnswer(OUI) ||
				question(reponses, ACCORD_INFO_RELEVE_CARRIERE).hasNotAnswer(OUI);
	}

	private Question question(final ComplementReponses reponses, final ComplementQuestionDescriptor questionDescriptor) {
		return new Question(reponses.getReponses().get(questionDescriptor));
	}

	private static class Question {

		private final List<QuestionChoiceValue> reponsesForTheQuestion;

		public Question(final List<QuestionChoiceValue> reponsesForTheQuestion) {
			this.reponsesForTheQuestion = reponsesForTheQuestion;
		}

		public boolean hasNotAnswer(final QuestionChoiceValue value) {
			return reponsesForTheQuestion == null || !reponsesForTheQuestion.contains(value);
		}

	}
}
