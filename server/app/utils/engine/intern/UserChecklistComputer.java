package utils.engine.intern;

import java.util.ArrayList;
import java.util.List;

import models.Chapitre;
import models.Checklist;
import utils.dao.CaisseDao;
import utils.engine.data.UserChapitre;
import utils.engine.data.UserChecklist;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.ChecklistName;

public class UserChecklistComputer {

	private final UserChecklistChapitreFilter userChecklistChapitreFilter;
	private final UserChecklistChapitreComputer userChecklistChapitreComputer;
	private final CaisseDao caisseDao;
	private final AutreRegimeComputer autreRegimeComputer;
	private final UserCaisseComputer userCaisseComputer;

	public UserChecklistComputer(final UserChecklistChapitreFilter userChecklistChapitreFilter,
			final UserChecklistChapitreComputer userChecklistChapitreComputer, final CaisseDao caisseDao, final AutreRegimeComputer autreRegimeComputer, final UserCaisseComputer userCaisseComputer) {
		this.userChecklistChapitreFilter = userChecklistChapitreFilter;
		this.userChecklistChapitreComputer = userChecklistChapitreComputer;
		this.caisseDao = caisseDao;
		this.autreRegimeComputer = autreRegimeComputer;
		this.userCaisseComputer = userCaisseComputer;
	}

	public UserChecklist compute(final Checklist checklist, final UserChecklistGenerationData userChecklistGenerationData) {
		final UserChecklist userChecklist = new UserChecklist();
		userChecklist.nom = checklist.nom;
		final ChecklistName checklistName = ChecklistName.valueOf(checklist.nom);
		userChecklist.caisse = userCaisseComputer.compute(caisseDao.findForDepartment(checklistName, userChecklistGenerationData.getDepartement()));
		userChecklist.chapitres = compute(checklist.chapitres, userChecklistGenerationData);
		autreRegimeComputer.compute(userChecklist, userChecklistGenerationData.regimesInfosJsonStr);
		return userChecklist;
	}

	private List<UserChapitre> compute(final List<Chapitre> chapitres, final UserChecklistGenerationData userChecklistGenerationData) {
		final ArrayList<UserChapitre> userChapitres = new ArrayList<>();
		for (final Chapitre chapitre : chapitres) {
			if (userChecklistChapitreFilter.isToBeDisplayed(chapitre, userChecklistGenerationData)) {
				userChapitres.add(userChecklistChapitreComputer.compute(chapitre, userChecklistGenerationData));
			}
		}
		return userChapitres;
	}

}
