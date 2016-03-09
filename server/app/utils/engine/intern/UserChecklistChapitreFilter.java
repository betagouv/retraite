package utils.engine.intern;

import models.Chapitre;
import models.Condition;
import utils.engine.data.UserChecklistGenerationData;

public class UserChecklistChapitreFilter {

	private final UserChecklistConditionEvaluator userChecklistConditionEvaluator;

	public UserChecklistChapitreFilter(final UserChecklistConditionEvaluator userChecklistConditionEvaluator) {
		this.userChecklistConditionEvaluator = userChecklistConditionEvaluator;
	}

	public boolean isToBeDisplayed(final Chapitre chapitre, final UserChecklistGenerationData userChecklistGenerationData) {
		if (chapitre.conditions == null) {
			return true;
		}
		return allConditionsAreVerified(chapitre, userChecklistGenerationData);
	}

	private boolean allConditionsAreVerified(final Chapitre chapitre, final UserChecklistGenerationData userChecklistGenerationData) {
		for (final Condition condition : chapitre.conditions) {
			if (!userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData)) {
				return false;
			}
		}
		return true;
	}

}
