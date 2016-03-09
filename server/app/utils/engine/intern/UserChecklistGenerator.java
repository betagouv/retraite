package utils.engine.intern;

import models.Checklist;
import utils.dao.DaoChecklist;
import utils.engine.data.LiquidateurReponses;
import utils.engine.data.UserChecklist;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.ChecklistName;

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

	public UserChecklist generate(final UserChecklistGenerationData userChecklistGenerationData, final LiquidateurReponses liquidateurReponses) {
		final ChecklistName checklistName = checklistNameSelector.select(userChecklistGenerationData.getRegimesAlignes(), liquidateurReponses);
		return generate(checklistName, userChecklistGenerationData, liquidateurReponses);
	}

	public UserChecklist generate(final ChecklistName checklistName, final UserChecklistGenerationData userChecklistGenerationData,
			final LiquidateurReponses liquidateurReponses) {
		final Checklist checklist = daoChecklist.find(checklistName.toString(), userChecklistGenerationData.published);
		return userChecklistComputer.compute(checklist, userChecklistGenerationData);
	}

}
