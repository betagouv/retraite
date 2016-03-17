package utils.engine;

import static utils.engine.EngineUtils.contains;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_A;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_B;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_C;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionChoiceValue.SALARIE;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import java.util.ArrayList;
import java.util.List;

import controllers.data.PostData;
import utils.engine.data.QuestionChoice;
import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.RenderData;
import utils.engine.data.enums.LiquidateurQuestionDescriptor2;
import utils.engine.data.enums.RegimeAligne;

public class DisplayerLiquidateurQuestions {

	private final SolverQuestionA solverQuestionA;
	private final SolverQuestionB solverQuestionB;

	public DisplayerLiquidateurQuestions(final SolverQuestionA solverQuestionA, final SolverQuestionB solverQuestionB) {
		this.solverQuestionA = solverQuestionA;
		this.solverQuestionB = solverQuestionB;
	}

	public void fillData(final PostData data, final RenderData renderData, final String regimes, final RegimeAligne[] regimesAlignes) {
		renderData.hidden_step = "displayLiquidateurQuestions";
		renderData.hidden_regimes = regimes;
		processNextStep(data, renderData, regimes, regimesAlignes);
		if (renderData.questionLiquidateur.liquidateurQuestionDescriptor != null) {
			renderData.hidden_liquidateurStep = renderData.questionLiquidateur.liquidateurQuestionDescriptor.toString();
		}
	}

	private void processNextStep(final PostData data, final RenderData renderData, final String regimes, final RegimeAligne[] regimesAlignes) {
		final LiquidateurQuestionDescriptor2 previousStep = getStep(data.hidden_liquidateurStep);
		if (isBefore(previousStep, QUESTION_A)) {
			if (contains(regimesAlignes, RegimeAligne.MSA)) {
				renderData.questionLiquidateur.liquidateurQuestionDescriptor = QUESTION_A;
				return;
			}
		}
		if (previousStep == QUESTION_A) {
			final RegimeLiquidateurAndUserStatus solved = solverQuestionA.solve(regimesAlignes, data.liquidateurReponseJsonStr);
			renderData.hidden_liquidateur = toStringOrNull(solved.getRegimeLiquidateur());
			renderData.hidden_userStatus = toStringOrNull(solved.getStatus());
		}
		if (isBefore(previousStep, QUESTION_B) && renderData.hidden_liquidateur == null) {
			renderData.questionLiquidateur.liquidateurQuestionDescriptor = QUESTION_B;
			renderData.questionLiquidateur.choices = generateSpecificChoicesForQuestionB(regimesAlignes);
			return;
		}
		if (previousStep == QUESTION_B) {
			final RegimeLiquidateurAndUserStatus solved = solverQuestionB.solve(data.liquidateurReponseJsonStr);
			renderData.hidden_liquidateur = toStringOrNull(solved.getRegimeLiquidateur());
			renderData.hidden_userStatus = toStringOrNull(solved.getStatus());
		}
		if (isBefore(previousStep, QUESTION_C)) {
			renderData.questionLiquidateur.liquidateurQuestionDescriptor = QUESTION_C;
			if (isRsiLiquidateur(data, renderData)) {
				renderData.questionLiquidateur.choices = generateSpecificChoicesForQuestionC();
			}
			return;
		}

		// Sinon, on ne fait rien, renderData.hidden_liquidateurStep=null indique qu'il n'y a plus de questions
	}

	private boolean isRsiLiquidateur(final PostData data, final RenderData renderData) {
		return RSI.toString().equals(data.hidden_liquidateur) || RSI.toString().equals(renderData.hidden_liquidateur);
	}

	private List<QuestionChoice> generateSpecificChoicesForQuestionB(final RegimeAligne[] regimesAlignes) {
		if (contains(regimesAlignes, MSA, RSI, CNAV)) {
			// Pas de filtre
			return null;
		}
		final List<QuestionChoice> choices = new ArrayList<>();
		int cumulableChoices = 0;
		if (contains(regimesAlignes, CNAV)) {
			choices.add(QUESTION_B.getChoice(SALARIE));
			cumulableChoices += 1;
		}
		if (contains(regimesAlignes, RSI)) {
			choices.add(QUESTION_B.getChoice(INDEP));
			choices.add(QUESTION_B.getChoice(CONJOINT_INDEP));
			cumulableChoices += 1;
		}
		if (contains(regimesAlignes, MSA)) {
			choices.add(QUESTION_B.getChoice(NSA));
			choices.add(QUESTION_B.getChoice(SA));
			cumulableChoices += 2;
		}
		if (cumulableChoices >= 2) {
			choices.add(QUESTION_B.getChoice(DEUX_ACTIVITES));
		}
		return choices;
	}

	private List<QuestionChoice> generateSpecificChoicesForQuestionC() {
		final List<QuestionChoice> choices = new ArrayList<>();
		choices.add(QUESTION_C.getChoice(INVALIDITE_RSI));
		choices.add(QUESTION_C.getChoice(PENIBILITE));
		return choices;
	}

	private boolean isBefore(final LiquidateurQuestionDescriptor2 step1, final LiquidateurQuestionDescriptor2 step2) {
		return step1 == null || step1.isBefore(step2);
	}

	private LiquidateurQuestionDescriptor2 getStep(final String step) {
		if (step == null) {
			return null;
		}
		try {
			return LiquidateurQuestionDescriptor2.valueOf(step);
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

	private String toStringOrNull(final Object o) {
		return o == null ? null : o.toString();
	}

}
