package utils.engine.intern;

import models.Chapitre;
import models.Delai;
import utils.engine.data.UserChapitre;
import utils.engine.data.UserChecklistGenerationData;

public class UserChecklistChapitreComputer {

	private final UserChecklistDelaiComputer userChecklistDelaiComputer;
	private final UserChecklistParcoursComputer userChecklistParcoursComputer;

	public UserChecklistChapitreComputer(final UserChecklistDelaiComputer userChecklistDelaiComputer,
			final UserChecklistParcoursComputer userChecklistParcoursComputer) {
		this.userChecklistDelaiComputer = userChecklistDelaiComputer;
		this.userChecklistParcoursComputer = userChecklistParcoursComputer;
	}

	public UserChapitre compute(final Chapitre chapitre, final UserChecklistGenerationData userChecklistGenerationData) {
		final UserChapitre userChapitre = new UserChapitre();
		userChapitre.titre = chapitre.titre;
		userChapitre.delai = computeDelai(chapitre, userChecklistGenerationData);
		userChapitre.texteActions = userChecklistParcoursComputer.compute(chapitre.texteActions, userChecklistGenerationData);
		userChapitre.texteModalites = userChecklistParcoursComputer.compute(chapitre.texteModalites, userChecklistGenerationData);
		userChapitre.texteInfos = userChecklistParcoursComputer.compute(chapitre.texteInfos, userChecklistGenerationData);

		return userChapitre;
	}

	private String computeDelai(final Chapitre chapitre, final UserChecklistGenerationData userChecklistGenerationData) {
		if (userChecklistDelaiComputer == null) {
			new Exception("userChecklistDelaiComputer null !").printStackTrace();
		}
		final Delai delai = selectDelai(chapitre, userChecklistGenerationData);
		return userChecklistDelaiComputer.compute(delai, userChecklistGenerationData.getDateDepart());
	}

	private Delai selectDelai(final Chapitre chapitre, final UserChecklistGenerationData userChecklistGenerationData) {
		return userChecklistGenerationData.isSA && chapitre.delaiSA != null ? chapitre.delaiSA : chapitre.delai;
	}

}
