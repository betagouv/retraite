package utils.engine.intern;

import models.Checklist;
import utils.dao.DaoChecklist;
import utils.engine.data.UserChecklist;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.ChecklistName;
import utils.engine.data.enums.RegimeAligne;

public class UserChecklistGenerator {

	private final ChecklistNameSelector checklistNameSelector;
	private final DaoChecklist daoChecklist;
	private final UserChecklistComputer userChecklistComputer;

	public UserChecklistGenerator(final ChecklistNameSelector checklistNameSelector, final DaoChecklist daoChecklist,
			final UserChecklistComputer userChecklistComputer) {
		this.checklistNameSelector = checklistNameSelector;
		this.daoChecklist = daoChecklist;
		this.userChecklistComputer = userChecklistComputer;
	}

	public UserChecklist generate(final UserChecklistGenerationData userChecklistGenerationData, final RegimeAligne regimeLiquidateur) {
		final ChecklistName checklistName = checklistNameSelector.select(userChecklistGenerationData.getRegimesAlignes(), regimeLiquidateur);
		return generate(checklistName, userChecklistGenerationData);
	}

	public UserChecklist generate(final ChecklistName checklistName, final UserChecklistGenerationData userChecklistGenerationData) {
		final Checklist checklist = daoChecklist.find(checklistName.toString(), userChecklistGenerationData.published);
		return userChecklistComputer.compute(checklist, userChecklistGenerationData);
	}

}
