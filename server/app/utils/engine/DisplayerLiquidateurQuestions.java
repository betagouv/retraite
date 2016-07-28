package utils.engine;

import static utils.JsonUtils.fromJson;
import static utils.JsonUtils.toJson;
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
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionChoiceValue.SALARIE;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_CPAM;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_MSA;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_RSI;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_CHEF;
import static utils.engine.data.enums.UserStatus.STATUS_CONJOINT_COLLABORATEUR;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;
import static utils.engine.data.enums.UserStatus.STATUS_SA;

import java.util.ArrayList;
import java.util.List;

import controllers.data.PostData;
import utils.engine.data.QuestionAndResponses;
import utils.engine.data.QuestionAndResponsesList;
import utils.engine.data.QuestionChoice;
import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.RenderData;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class DisplayerLiquidateurQuestions {

	private final QuestionSolverA questionSolverA;
	private final QuestionSolverB questionSolverB;
	private final QuestionSolverC questionSolverC;
	private final QuestionSolverD questionSolverD;
	private final QuestionSolverE questionSolverE;
	private final QuestionSolverF questionSolverF;

	public DisplayerLiquidateurQuestions(final QuestionSolverA questionSolverA, final QuestionSolverB questionSolverB, final QuestionSolverC questionSolverC,
			final QuestionSolverD questionSolverD, final QuestionSolverE questionSolverE, final QuestionSolverF questionSolverF) {
		this.questionSolverA = questionSolverA;
		this.questionSolverB = questionSolverB;
		this.questionSolverC = questionSolverC;
		this.questionSolverD = questionSolverD;
		this.questionSolverE = questionSolverE;
		this.questionSolverF = questionSolverF;
	}

	public void fillData(final PostData data, final RenderData renderData, final RegimeAligne[] regimesAlignes) {
		renderData.hidden_step = "displayLiquidateurQuestions";
		storeReponseHistory(data, renderData);
		processNextStep(data, renderData, regimesAlignes);
		if (renderData.questionLiquidateur.liquidateurQuestionDescriptor != null) {
			renderData.hidden_liquidateurStep = renderData.questionLiquidateur.liquidateurQuestionDescriptor;
		}
	}

	private void storeReponseHistory(final PostData data, final RenderData renderData) {
		if (data.hidden_liquidateurStep != null) {
			final QuestionAndResponsesList list = listFrom(renderData.hidden_liquidateurReponsesHistory);
			final List reponse = fromJson(data.liquidateurReponseJsonStr, List.class);
			list.add(new QuestionAndResponses(data.hidden_liquidateurStep.toString(), reponse));
			renderData.hidden_liquidateurReponsesHistory = toJson(list);
		}
	}

	private QuestionAndResponsesList listFrom(final String hidden_liquidateurReponsesHistory) {
		QuestionAndResponsesList list = new QuestionAndResponsesList();
		if (hidden_liquidateurReponsesHistory != null && !hidden_liquidateurReponsesHistory.isEmpty()) {
			list = fromJson(hidden_liquidateurReponsesHistory, list.getClass());
		}
		return list;
	}

	private void processNextStep(final PostData data, final RenderData renderData, final RegimeAligne[] regimesAlignes) {
		final LiquidateurQuestionDescriptor previousStep = data.hidden_liquidateurStep;

		callQuestionSolversAndStoreResult(data, renderData, regimesAlignes, previousStep);
		if (renderData.ecranSortie != null) {
			return;
		}

		if (isBefore(previousStep, QUESTION_A) && contains(regimesAlignes, MSA)) {
			renderData.questionLiquidateur.liquidateurQuestionDescriptor = QUESTION_A;
			return;
		}
		if (isBefore(previousStep, QUESTION_B) && isRegimeLiquidateurNotDefined(data, renderData)) {
			renderData.questionLiquidateur.liquidateurQuestionDescriptor = QUESTION_B;
			renderData.questionLiquidateur.choices = generateSpecificChoicesForQuestionB(regimesAlignes);
			return;
		}
		if (isBefore(previousStep, QUESTION_C) && contains(regimesAlignes, RSI)) {
			renderData.questionLiquidateur.liquidateurQuestionDescriptor = QUESTION_C;
			if (isLiquidateur(data, renderData, RSI)) {
				renderData.questionLiquidateur.choices = generateSpecificChoicesForQuestionC();
			}
			return;
		}
		if (isBefore(previousStep, QUESTION_D) && isRegimeLiquidateurNotDefined(data, renderData)) {
			renderData.questionLiquidateur.liquidateurQuestionDescriptor = QUESTION_D;
			renderData.questionLiquidateur.choices = generateSpecificChoicesForQuestionD(regimesAlignes);
			return;
		}
		if (isBefore(previousStep, QUESTION_E)
				&& isLiquidateur(data, renderData, MSA)
				&& !isOneOrMore(data, renderData, STATUS_NSA, STATUS_SA)) {
			renderData.questionLiquidateur.liquidateurQuestionDescriptor = QUESTION_E;
			return;
		}
		if (isBefore(previousStep, QUESTION_F)
				&& isLiquidateur(data, renderData, RSI)
				&& !isOneOrMore(data, renderData, STATUS_CHEF, STATUS_CONJOINT_COLLABORATEUR)) {
			renderData.questionLiquidateur.liquidateurQuestionDescriptor = QUESTION_F;
			return;
		}

		// Sinon, on ne fait rien, renderData.hidden_liquidateurStep=null indique qu'il n'y a plus de questions
	}

	private void callQuestionSolversAndStoreResult(final PostData data, final RenderData renderData, final RegimeAligne[] regimesAlignes,
			final LiquidateurQuestionDescriptor previousStep) {
		if (previousStep == null) {
			return;
		}
		switch (previousStep) {
		case QUESTION_A:
			callQuestionSolverAndStoreResult(data, renderData, regimesAlignes, questionSolverA);
			break;
		case QUESTION_B:
			callQuestionSolverAndStoreResult(data, renderData, regimesAlignes, questionSolverB);
			break;
		case QUESTION_C:
			callQuestionSolverAndStoreResult(data, renderData, regimesAlignes, questionSolverC);
			break;
		case QUESTION_D:
			callQuestionSolverAndStoreResult(data, renderData, regimesAlignes, questionSolverD);
			break;
		case QUESTION_E:
			callQuestionSolverAndStoreResult(data, renderData, regimesAlignes, questionSolverE);
			break;
		case QUESTION_F:
			callQuestionSolverAndStoreResult(data, renderData, regimesAlignes, questionSolverF);
			break;
		}
	}

	private boolean isRegimeLiquidateurNotDefined(final PostData data, final RenderData renderData) {
		return data.hidden_liquidateur == null && renderData.hidden_liquidateur == null;
	}

	private void callQuestionSolverAndStoreResult(final PostData data, final RenderData renderData, final RegimeAligne[] regimesAlignes,
			final QuestionSolver solver) {
		final RegimeLiquidateurAndUserStatus solved = solver.solve(regimesAlignes, data.liquidateurReponseJsonStr);
		final RegimeAligne newRegimeLiquidateur = solved.getRegimeLiquidateur();
		if (newRegimeLiquidateur != null) {
			renderData.hidden_liquidateur = newRegimeLiquidateur;
		}
		final UserStatus newStatus = solved.getStatus();
		if (newStatus != null) {
			renderData.hidden_userStatus = add(renderData.hidden_userStatus, newStatus);
		}
		renderData.ecranSortie = solved.getEcranSortie();
	}

	private List<UserStatus> add(final List<UserStatus> current, final UserStatus newStatus) {
		final List<UserStatus> news = new ArrayList<>();
		if (current != null) {
			news.addAll(current);
		}
		news.add(newStatus);
		return news;
	}

	private boolean isLiquidateur(final PostData data, final RenderData renderData, final RegimeAligne regimeAligne) {
		return regimeAligne == data.hidden_liquidateur || regimeAligne == renderData.hidden_liquidateur;
	}

	private boolean isOneOrMore(final PostData data, final RenderData renderData, final UserStatus... statuss) {
		for (final UserStatus status : statuss) {
			if (isStatus(data, renderData, status)) {
				return true;
			}
		}
		return false;
	}

	private boolean isStatus(final PostData data, final RenderData renderData, final UserStatus status) {
		return contains(data.hidden_userStatus, status) || contains(renderData.hidden_userStatus, status);
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

	private List<QuestionChoice> generateSpecificChoicesForQuestionD(final RegimeAligne[] regimesAlignes) {
		if (contains(regimesAlignes, MSA, RSI, CNAV)) {
			// Pas de filtre
			return null;
		}
		final List<QuestionChoice> choices = new ArrayList<>();
		if (contains(regimesAlignes, CNAV)) {
			choices.add(QUESTION_D.getChoice(SANTE_CPAM));
		}
		if (contains(regimesAlignes, RSI)) {
			choices.add(QUESTION_D.getChoice(SANTE_RSI));
		}
		if (contains(regimesAlignes, MSA)) {
			choices.add(QUESTION_D.getChoice(SANTE_MSA));
		}
		return choices;
	}

	private boolean isBefore(final LiquidateurQuestionDescriptor step1, final LiquidateurQuestionDescriptor step2) {
		return step1 == null || step1.isBefore(step2);
	}

}
