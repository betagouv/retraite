package utils.engine.intern;

import utils.engine.data.ComplementReponses;
import utils.engine.data.LiquidateurReponses;
import utils.engine.data.MonthAndYear;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;

public class UserChecklistGenerationDataBuilder {

	private final LiquidateurReponsesEvaluator liquidateurReponsesEvaluator;

	public UserChecklistGenerationDataBuilder(final LiquidateurReponsesEvaluator liquidateurReponsesEvaluator) {
		this.liquidateurReponsesEvaluator = liquidateurReponsesEvaluator;
	}

	public UserChecklistGenerationData build(final MonthAndYear dateDepart, final String departement, final Regime[] regimes,
			final RegimeAligne[] regimesAlignes, final LiquidateurReponses liquidateurReponses, final ComplementReponses complementReponses,
			final boolean parcoursDematIfExist, final boolean published, final boolean isCarriereLongue) {
		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(dateDepart, departement, regimes, regimesAlignes,
				parcoursDematIfExist, published);
		userChecklistGenerationData.isConjointCollaborateur = liquidateurReponsesEvaluator.isConjointCollaborateur(liquidateurReponses);
		userChecklistGenerationData.isNSA = liquidateurReponsesEvaluator.isNSA(liquidateurReponses);
		userChecklistGenerationData.isSA = liquidateurReponsesEvaluator.isSA(liquidateurReponses);
		userChecklistGenerationData.isCarriereAReconstituer = liquidateurReponsesEvaluator.isCarriereAReconstituer(complementReponses);
		userChecklistGenerationData.isCarriereLongue = isCarriereLongue;
		return userChecklistGenerationData;
	}

}
