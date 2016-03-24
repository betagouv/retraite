package utils.engine.intern;

import static utils.engine.data.enums.UserStatus.STATUS_CONJOINT_COLLABORATEUR;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;
import static utils.engine.data.enums.UserStatus.STATUS_SA;

import java.util.List;

import utils.engine.data.ComplementReponses;
import utils.engine.data.MonthAndYear;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class UserChecklistGenerationDataBuilder {

	private final LiquidateurReponsesEvaluator liquidateurReponsesEvaluator;

	public UserChecklistGenerationDataBuilder(final LiquidateurReponsesEvaluator liquidateurReponsesEvaluator) {
		this.liquidateurReponsesEvaluator = liquidateurReponsesEvaluator;
	}

	public UserChecklistGenerationData build(final MonthAndYear dateDepart, final String departement, final Regime[] regimes,
			final RegimeAligne[] regimesAlignes, final RegimeAligne regimeLiquidateur, final ComplementReponses complementReponses,
			final boolean parcoursDematIfExist, final boolean published, final boolean isCarriereLongue, final List<UserStatus> userStatus) {
		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(dateDepart, departement, regimes, regimesAlignes,
				parcoursDematIfExist, published);
		userChecklistGenerationData.isConjointCollaborateur = userStatus.contains(STATUS_CONJOINT_COLLABORATEUR);
		userChecklistGenerationData.isNSA = userStatus.contains(STATUS_NSA);
		userChecklistGenerationData.isSA = userStatus.contains(STATUS_SA);
		userChecklistGenerationData.isCarriereAReconstituer = liquidateurReponsesEvaluator.isCarriereAReconstituer(complementReponses);
		userChecklistGenerationData.isCarriereLongue = isCarriereLongue;
		return userChecklistGenerationData;
	}

}
