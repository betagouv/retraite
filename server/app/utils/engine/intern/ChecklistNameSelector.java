package utils.engine.intern;

import static java.util.Arrays.asList;
import static utils.JsonUtils.toJson;
import static utils.engine.data.enums.ChecklistName.CNAV;
import static utils.engine.data.enums.ChecklistName.MSA;
import static utils.engine.data.enums.ChecklistName.RSI;
import static utils.engine.data.enums.ChecklistSelector.SELECT_CNAV;
import static utils.engine.data.enums.ChecklistSelector.SELECT_CNAV_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE;
import static utils.engine.data.enums.ChecklistSelector.SELECT_MSA;
import static utils.engine.data.enums.ChecklistSelector.SELECT_MSA_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE;
import static utils.engine.data.enums.ChecklistSelector.SELECT_RSI_AVANT_73;
import static utils.engine.data.enums.ChecklistSelector.SELECT_RSI_INVALIDITE;

import java.util.ArrayList;
import java.util.List;

import utils.RetraiteException;
import utils.engine.data.LiquidateurReponses;
import utils.engine.data.QuestionChoice;
import utils.engine.data.enums.ChecklistName;
import utils.engine.data.enums.ChecklistSelector;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;

public class ChecklistNameSelector {

	public ChecklistName select(final RegimeAligne[] regimesAlignes, final LiquidateurReponses liquidateurReponses) {

		if (liquidateurReponses == null || liquidateurReponses.isEmpty()) {
			// Normalement, il n'y a pas eu de questions, on est en régime unique
			if (regimesAlignes.length != 1) {
				throw new IllegalStateException(
						"Situation anormale : pas de question liquidateur mais pas uniquement 1 seul régime aligné (regimesAlignes=" + asList(regimesAlignes)
								+ ")");
			}
			return regimesAlignes[0].getChecklistName();
		}

		final List<ChecklistSelector> selectors = new ArrayList<>();
		for (final LiquidateurQuestionDescriptor liquidateurQuestionDescriptor : liquidateurReponses.getReponses().keySet()) {
			final List<QuestionChoiceValue> reponses = liquidateurReponses.getReponses().get(liquidateurQuestionDescriptor);
			for (final QuestionChoiceValue questionChoiceValue : reponses) {
				final QuestionChoice questionChoice = retrieveQuestionChoice(liquidateurQuestionDescriptor, questionChoiceValue);
				final ChecklistSelector selector = questionChoice.getSelector();
				if (selector != null) {
					selectors.add(selector);
				}
			}
		}

		if (selectors.size() == 0) {
			throw new RetraiteException("Situation anormale : impossible de déterminer la checklist : regimesAlignes=" + asList(regimesAlignes)
					+ ", liquidateurReponses=" + toJson(liquidateurReponses) + " --> aucun ChecklistSelector");
		}
		if (selectors.size() == 1) {
			return selectors.get(0).getChecklistName();
		}
		if (selectors.contains(SELECT_RSI_AVANT_73)) {
			return RSI;
		}
		if (selectors.contains(SELECT_RSI_INVALIDITE)) {
			return RSI;
		}
		if (selectors.size() == 2) {
			if (selectors.contains(SELECT_CNAV_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE) && selectors.contains(SELECT_CNAV)) {
				return CNAV;
			}
			if (selectors.contains(SELECT_MSA_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE) && selectors.contains(SELECT_MSA)) {
				return MSA;
			}
		}

		throw new RetraiteException("Situation anormale : impossible de déterminer la checklist : regimesAlignes=" + asList(regimesAlignes)
				+ ", liquidateurReponses=" + toJson(liquidateurReponses) + " --> ChecklistSelectors=" + selectors);

	}

	private QuestionChoice retrieveQuestionChoice(final LiquidateurQuestionDescriptor liquidateurQuestionDescriptor,
			final QuestionChoiceValue questionChoiceValue) {
		for (final QuestionChoice questionChoice : liquidateurQuestionDescriptor.getQuestionChoices()) {
			if (questionChoice.getValue() == questionChoiceValue) {
				return questionChoice;
			}
		}
		return null;
	}

}
