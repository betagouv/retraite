package utils.engine.intern;

import static utils.engine.data.enums.Regime.RegimeType.BASE_AUTRE;
import static utils.engine.data.enums.Regime.RegimeType.COMPLEMENTAIRE;

import java.util.ArrayList;
import java.util.List;

import models.Chapitre;
import models.Checklist;
import utils.dao.CaisseDao;
import utils.engine.data.UserChapitre;
import utils.engine.data.UserChecklist;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.ChecklistName;
import utils.engine.data.enums.Regime;

public class UserChecklistComputer {

	private final UserChecklistChapitreFilter userChecklistChapitreFilter;
	private final UserChecklistChapitreComputer userChecklistChapitreComputer;
	private final CaisseDao caisseDao;

	public UserChecklistComputer(final UserChecklistChapitreFilter userChecklistChapitreFilter,
			final UserChecklistChapitreComputer userChecklistChapitreComputer, final CaisseDao caisseDao) {
		this.userChecklistChapitreFilter = userChecklistChapitreFilter;
		this.userChecklistChapitreComputer = userChecklistChapitreComputer;
		this.caisseDao = caisseDao;
	}

	public UserChecklist compute(final Checklist checklist, final UserChecklistGenerationData userChecklistGenerationData) {
		final UserChecklist userChecklist = new UserChecklist();
		userChecklist.nom = checklist.nom;
		final ChecklistName checklistName = ChecklistName.valueOf(checklist.nom);
		userChecklist.caisse = caisseDao.find(checklistName, userChecklistGenerationData.getDepartement());
		userChecklist.autresRegimesDeBase = findAutresRegimesDeBase(userChecklistGenerationData.getRegimes());
		userChecklist.regimesComplementaires = findRegimesComplementaires(userChecklistGenerationData.getRegimes());
		userChecklist.chapitres = compute(checklist.chapitres, userChecklistGenerationData);
		return userChecklist;
	}

	private Regime[] findAutresRegimesDeBase(final Regime[] regimes) {
		final List<Regime> regimesComplementaires = new ArrayList<>();
		for (final Regime regime : regimes) {
			if (regime.getType() == BASE_AUTRE) {
				regimesComplementaires.add(regime);
			}
		}
		return regimesComplementaires.toArray(new Regime[regimesComplementaires.size()]);
	}

	private Regime[] findRegimesComplementaires(final Regime[] regimes) {
		final List<Regime> regimesComplementaires = new ArrayList<>();
		for (final Regime regime : regimes) {
			if (regime.getType() == COMPLEMENTAIRE) {
				regimesComplementaires.add(regime);
			}
		}
		return regimesComplementaires.toArray(new Regime[regimesComplementaires.size()]);
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
