package utils.engine.intern;

import utils.dao.CaisseDao;
import utils.engine.utils.DateProvider;
import utils.engine.utils.VariablesReplacerMustache;

public class UserChecklistComputerFactory {

	public static UserChecklistComputer createUserChecklistComputer(final DateProvider dateProvider) {
		return new UserChecklistComputer(
				new UserChecklistChapitreFilter(
						new UserChecklistConditionEvaluator(
								new UserChecklistConditionDelaiEvaluator(
										dateProvider))),
				new UserChecklistChapitreComputer(
						new UserChecklistDelaiComputer(dateProvider),
						new UserChecklistParcoursComputer(
								new UserChecklistVarsProvider(),
								new VariablesReplacerMustache())),
				new CaisseDao(),
				new AutreRegimeComputer());
	}
}
