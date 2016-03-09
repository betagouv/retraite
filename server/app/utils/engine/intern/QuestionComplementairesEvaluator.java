package utils.engine.intern;

import static utils.engine.data.enums.QuestionChoiceValue.PARFOIS;
import static utils.engine.data.enums.QuestionChoiceValue.SOUVENT;

import java.util.List;

import utils.engine.data.ComplementReponses;
import utils.engine.data.enums.ComplementQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class QuestionComplementairesEvaluator {

	public boolean isParcoursDemat(final ComplementReponses complementReponse) {
		final List<QuestionChoiceValue> reponses = complementReponse.getReponses().get(ComplementQuestionDescriptor.DEMARCHES_EN_LIGNE);
		return notNullAndContainsSouventOrParfois(reponses);
	}

	private boolean notNullAndContainsSouventOrParfois(final List<QuestionChoiceValue> reponses) {
		return reponses != null && (reponses.contains(SOUVENT) || reponses.contains(PARFOIS));
	}

}
