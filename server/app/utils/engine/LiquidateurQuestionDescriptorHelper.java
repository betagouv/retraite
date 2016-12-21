package utils.engine;

import java.util.ArrayList;
import java.util.List;

import controllers.data.PostData;

import static utils.engine.EngineUtils.contains;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_A;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_B;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_C;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_D;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_E;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_F;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP_OLD;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.NSA_OLD;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionChoiceValue.SALARIE;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_CPAM;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_MSA;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_RSI;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import utils.engine.data.CommonExchangeData;
import utils.engine.data.QuestionChoice;
import utils.engine.data.RenderData;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.RegimeAligne;

public class LiquidateurQuestionDescriptorHelper {

	private final LiquidateurQuestionDescriptor liquidateurQuestionDescriptor;

	public LiquidateurQuestionDescriptorHelper(LiquidateurQuestionDescriptor liquidateurQuestionDescriptor) {
		super();
		this.liquidateurQuestionDescriptor = liquidateurQuestionDescriptor;
	}

	
	public List<QuestionChoice> getSpecificsChoices(final RegimeAligne[] regimesAlignes) {
		return getSpecificsChoices(regimesAlignes, null);
	}
	
	public List<QuestionChoice> getSpecificsChoices(final RegimeAligne[] regimesAlignes, RegimeAligne liquidateur) {
		List<QuestionChoice> choices = null;
		
		switch (liquidateurQuestionDescriptor) {
			case QUESTION_A:
				break;
			case QUESTION_B:
				if (contains(regimesAlignes, MSA, RSI, CNAV)) {
					// Pas de filtre
					break;
				}
				choices = new ArrayList<>();
				int cumulableChoices = 0;
				if (contains(regimesAlignes, CNAV)) {
					choices.add(QUESTION_B.getChoice(SALARIE));
					cumulableChoices += 1;
				}
				if (contains(regimesAlignes, RSI)) {
					choices.add(QUESTION_B.getChoice(INDEP));
					choices.add(QUESTION_B.getChoice(INDEP_OLD));
					choices.add(QUESTION_B.getChoice(CONJOINT_INDEP));
					cumulableChoices += 1;
				}
				if (contains(regimesAlignes, MSA)) {
					choices.add(QUESTION_B.getChoice(NSA));
					choices.add(QUESTION_B.getChoice(NSA_OLD));
					choices.add(QUESTION_B.getChoice(SA));
					cumulableChoices += 2;
				}
				if (cumulableChoices >= 2) {
					choices.add(QUESTION_B.getChoice(DEUX_ACTIVITES));
				}
				break;
			case QUESTION_C:
				if (RSI == liquidateur) {
					choices = new ArrayList<>();
					choices.add(QUESTION_C.getChoice(INVALIDITE_RSI));
					choices.add(QUESTION_C.getChoice(PENIBILITE));
				}
				break;
			case QUESTION_D:
				if (contains(regimesAlignes, MSA, RSI, CNAV)) {
					// Pas de filtre
					break;
				}
				choices = new ArrayList<>();
				if (contains(regimesAlignes, CNAV)) {
					choices.add(QUESTION_D.getChoice(SANTE_CPAM));
				}
				if (contains(regimesAlignes, RSI)) {
					choices.add(QUESTION_D.getChoice(SANTE_RSI));
				}
				if (contains(regimesAlignes, MSA)) {
					choices.add(QUESTION_D.getChoice(SANTE_MSA));
				}
				break;
			case QUESTION_E:
				break;
			case QUESTION_F:
				break;
		}
		
		return choices;
	}
}
