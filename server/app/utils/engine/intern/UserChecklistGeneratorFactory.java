package utils.engine.intern;

import static utils.dao.DaoChecklistFactory.createDaoChecklist;
import static utils.engine.intern.UserChecklistComputerFactory.createUserChecklistComputer;

import utils.engine.utils.DateProvider;

public class UserChecklistGeneratorFactory {

	public static UserChecklistGenerator createUserChecklistGenerator() {
		final DateProvider dateProvider = new DateProvider();
		return new UserChecklistGenerator(
				new ChecklistNameSelector(),
				createDaoChecklist(),
				createUserChecklistComputer(dateProvider));
	}
}
