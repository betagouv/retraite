package utils.engine.intern;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.ComplementQuestionDescriptor.CONSULT_RELEVE_CARRIERE;
import static utils.engine.data.enums.ComplementQuestionDescriptor.DEMARCHES_EN_LIGNE;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;
import static utils.engine.data.enums.QuestionChoiceValue.PARFOIS;
import static utils.engine.data.enums.QuestionChoiceValue.SOUVENT;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.ComplementReponses;
import utils.engine.data.enums.ComplementQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;

public class QuestionComplementairesEvaluatorTest {

	private QuestionComplementairesEvaluator questionComplementairesEvaluator;

	@Before
	public void setUp() {
		questionComplementairesEvaluator = new QuestionComplementairesEvaluator();
	}

	@Test
	public void should_return_false_if_question_demarche_en_ligne_sans_reponse() {
		final ComplementReponses complementReponse = new ComplementReponses();
		complementReponse.getReponses().put(CONSULT_RELEVE_CARRIERE, asList(OUI));
		assertThat(questionComplementairesEvaluator.isParcoursDemat(complementReponse)).isFalse();
	}

	@Test
	public void should_return_true_for_souvent() {
		final ComplementReponses complementReponse = new ComplementReponses();
		complementReponse.getReponses().put(DEMARCHES_EN_LIGNE, asList(SOUVENT));
		assertThat(questionComplementairesEvaluator.isParcoursDemat(complementReponse)).isTrue();
	}

	@Test
	public void should_return_true_for_parfois() {
		final ComplementReponses complementReponse = new ComplementReponses();
		complementReponse.getReponses().put(DEMARCHES_EN_LIGNE, asList(PARFOIS));
		assertThat(questionComplementairesEvaluator.isParcoursDemat(complementReponse)).isTrue();
	}

	@Test
	public void should_return_false_for_jamais() {
		final ComplementReponses complementReponse = new ComplementReponses();
		complementReponse.getReponses().put(ComplementQuestionDescriptor.DEMARCHES_EN_LIGNE, Arrays.asList(QuestionChoiceValue.JAMAIS));
		assertThat(questionComplementairesEvaluator.isParcoursDemat(complementReponse)).isFalse();
	}
}
