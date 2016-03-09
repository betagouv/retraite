package utils.engine.intern;

import models.Chapitre;
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
		userChapitre.texteIntro = userChecklistParcoursComputer.compute(chapitre.texteIntro, userChecklistGenerationData);
		userChapitre.parcours = computeParcours(chapitre, userChecklistGenerationData);
		userChapitre.texteComplementaire = userChecklistParcoursComputer.compute(chapitre.texteComplementaire, userChecklistGenerationData);

		return userChapitre;
	}

	private String computeParcours(final Chapitre chapitre, final UserChecklistGenerationData userChecklistGenerationData) {
		return userChecklistParcoursComputer.compute(
				userChecklistGenerationData.isParcoursDematIfExist() && chapitre.parcoursDematDifferent ? chapitre.parcoursDemat : chapitre.parcours,
				userChecklistGenerationData);
	}

	private String computeDelai(final Chapitre chapitre, final UserChecklistGenerationData userChecklistGenerationData) {
		if (userChecklistDelaiComputer == null) {
			new Exception("userChecklistDelaiComputer null !").printStackTrace();
		}
		return userChecklistDelaiComputer.compute(
				userChecklistGenerationData.isSA ? chapitre.delaiSA : chapitre.delai,
				userChecklistGenerationData.getDateDepart());
	}

}
