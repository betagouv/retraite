package utils.engine.intern;

import static utils.dao.DaoChecklistFactory.createDaoChecklist;

import utils.dao.CaisseDao;
import utils.engine.utils.DateProvider;
import utils.engine.utils.VariablesReplacerMustache;

public class UserChecklistGeneratorFactory {

	public static UserChecklistGenerator createUserChecklistGenerator() {
		final DateProvider dateProvider = new DateProvider();
		return new UserChecklistGenerator(
				new ChecklistNameSelector(),
				createDaoChecklist(),
				new UserChecklistComputer(
						new UserChecklistChapitreFilter(
								new UserChecklistConditionEvaluator(
										new UserChecklistConditionDelaiEvaluator(
												dateProvider))),
						new UserChecklistChapitreComputer(
								new UserChecklistDelaiComputer(dateProvider),
								new UserChecklistParcoursComputer(
										new UserChecklistVarsProvider(),
										new VariablesReplacerMustache())),
						new CaisseDao()));
	}
}
