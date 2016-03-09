package utils.engine.intern;

import static java.util.Arrays.asList;
import static utils.engine.EngineUtils.areRegimes;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.CHEF_EXPLOITATION_AGRICOLE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_INDEP_CONJOINT_AUTRE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_NSA_CONJOINT_INDEP;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DIVERSES_SITUATIONS_CNAV_RSI;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DIVERSES_SITUATIONS_RSI_MSA;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.ORGA_FRAIS_SANTE_CPAM_MSA;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.ORGA_FRAIS_SANTE_CPAM_RSI;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.ORGA_FRAIS_SANTE_RSI_MSA;
import static utils.engine.data.enums.QuestionChoiceValue.AUTRE;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import java.util.ArrayList;
import java.util.List;

import utils.RetraiteException;
import utils.engine.data.QuestionCondition;
import utils.engine.data.QuestionLiquidateur;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;

public class QuestionsLiquidateurBuilder {

	public List<QuestionLiquidateur> buildQuestions(final RegimeAligne[] regimesAlignes) {
		if (regimesAlignes.length < 2) {
			throw new RetraiteException("Cette fonction doit être appelée avec au moins 2 régimes");
		}
		final ArrayList<QuestionLiquidateur> questions = new ArrayList<>();
		if (areRegimes(regimesAlignes, CNAV, MSA)) {
			questions.add(createQuestionsOfType(CHEF_EXPLOITATION_AGRICOLE));
			questions.add(createQuestionsOfType(DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE, CHEF_EXPLOITATION_AGRICOLE, NON));
			questions.add(createQuestionsOfType(ORGA_FRAIS_SANTE_CPAM_MSA, DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE, DEUX_ACTIVITES));
		} else if (areRegimes(regimesAlignes, CNAV, RSI)) {
			questions.add(createQuestionsOfType(DIVERSES_SITUATIONS_CNAV_RSI));
			questions.add(createQuestionsOfType(DERN_ACT_INDEP_CONJOINT_AUTRE, DIVERSES_SITUATIONS_CNAV_RSI, AUTRE));
			questions.add(createQuestionsOfType(ORGA_FRAIS_SANTE_CPAM_RSI, DERN_ACT_INDEP_CONJOINT_AUTRE, DEUX_ACTIVITES));
		} else if (areRegimes(regimesAlignes, RSI, MSA)) {
			questions.add(createQuestionsOfType(DIVERSES_SITUATIONS_RSI_MSA));
			questions.add(createQuestionsOfType(DERN_ACT_NSA_CONJOINT_INDEP, DIVERSES_SITUATIONS_RSI_MSA, AUTRE));
			questions.add(createQuestionsOfType(ORGA_FRAIS_SANTE_RSI_MSA, DERN_ACT_NSA_CONJOINT_INDEP, DEUX_ACTIVITES));
		} else {
			throw new RetraiteException("Combinaison de régimes alignés non prévue : " + asList(regimesAlignes));
		}
		return questions;
	}

	private QuestionLiquidateur createQuestionsOfType(final LiquidateurQuestionDescriptor type) {
		final QuestionLiquidateur question = new QuestionLiquidateur();
		question.type = type;
		return question;
	}

	private QuestionLiquidateur createQuestionsOfType(final LiquidateurQuestionDescriptor type, final LiquidateurQuestionDescriptor conditionKey,
			final QuestionChoiceValue conditionValue) {
		final QuestionLiquidateur question = createQuestionsOfType(type);
		question.condition = new QuestionCondition(conditionKey, conditionValue);
		return question;
	}

}
