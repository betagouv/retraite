package utils.engine.intern;

import static utils.engine.data.enums.ComplementQuestionDescriptor.ACCORD_INFO_RELEVE_CARRIERE;
import static utils.engine.data.enums.ComplementQuestionDescriptor.CONSULT_RELEVE_CARRIERE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.CHEF_EXPLOITATION_AGRICOLE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_INDEP_CONJOINT_AUTRE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_NSA_CONJOINT_INDEP;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;
import static utils.engine.data.enums.QuestionChoiceValue.SA;

import java.util.List;

import utils.engine.data.ComplementReponses;
import utils.engine.data.LiquidateurReponses;
import utils.engine.data.enums.ComplementQuestionDescriptor;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class LiquidateurReponsesEvaluator {

	public boolean isNSA(final LiquidateurReponses reponses) {
		if (contains(reponses, CHEF_EXPLOITATION_AGRICOLE, OUI)) {
			return true;
		}
		if (contains(reponses, DERN_ACT_NSA_CONJOINT_INDEP, NSA)) {
			return true;
		}
		return false;
	}

	public boolean isSA(final LiquidateurReponses reponses) {
		if (contains(reponses, DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE, SA)) {
			return true;
		}
		if (contains(reponses, DERN_ACT_NSA_CONJOINT_INDEP, SA)) {
			return true;
		}
		return false;
	}

	public boolean isConjointCollaborateur(final LiquidateurReponses reponses) {
		if (contains(reponses, DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE, CONJOINT)) {
			return true;
		}
		if (contains(reponses, DERN_ACT_INDEP_CONJOINT_AUTRE, CONJOINT)) {
			return true;
		}
		if (contains(reponses, DERN_ACT_NSA_CONJOINT_INDEP, CONJOINT)) {
			return true;
		}
		if (contains(reponses, DERN_ACT_NSA_CONJOINT_INDEP, CONJOINT_INDEP)) {
			return true;
		}
		return false;
	}

	public boolean isCarriereAReconstituer(final ComplementReponses reponses) {
		return question(reponses, CONSULT_RELEVE_CARRIERE).hasNotAnswer(OUI) ||
				question(reponses, ACCORD_INFO_RELEVE_CARRIERE).hasNotAnswer(OUI);
	}

	private Question question(final ComplementReponses reponses, final ComplementQuestionDescriptor questionDescriptor) {
		return new Question(reponses.getReponses().get(questionDescriptor));
	}

	private boolean contains(final LiquidateurReponses reponses, final LiquidateurQuestionDescriptor questionDescriptor, final QuestionChoiceValue value) {
		if (reponses == null) {
			return false;
		}
		final List<QuestionChoiceValue> reponsesForTheQuestion = reponses.getReponses().get(questionDescriptor);
		return reponsesForTheQuestion != null && reponsesForTheQuestion.contains(value);
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
