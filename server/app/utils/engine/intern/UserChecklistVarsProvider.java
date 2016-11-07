package utils.engine.intern;

import static utils.engine.data.enums.Regime.AGIRC_ARRCO;
import static utils.engine.data.enums.Regime.RegimeType.BASE_AUTRE;

import java.util.Arrays;

import play.Logger;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.UserChecklistVars;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.Regime.RegimeType;

public class UserChecklistVarsProvider {

	public UserChecklistVars provideVars(final UserChecklistGenerationData userChecklistGenerationData) {
		final UserChecklistVars vars = new UserChecklistVars();
		vars.put("regimes_base_hors_alignes", getRegimeBaseHorsAlignes(userChecklistGenerationData));
		vars.put("regimes_compl_hors_agirc_arrco", getRegimeComplHorsAgircArrco(userChecklistGenerationData));
		vars.put("regimes_hors_alignes_et_hors_agirc_arrco", getRegimeHorsAlignesEtHorsAgircArrco(userChecklistGenerationData));
		vars.put("regimes_hors_alignes_ou_regimes_compl", getRegimeHorsAlignesOuCompl(userChecklistGenerationData));
		vars.put("agirc_arrco", containsRegime(userChecklistGenerationData, AGIRC_ARRCO));
		vars.put("status_nsa", userChecklistGenerationData.isNSA);
		vars.put("status_sa", userChecklistGenerationData.isSA);
		vars.put("status_chef", userChecklistGenerationData.isChef);
		vars.put("status_conjoint", userChecklistGenerationData.isConjointCollaborateur);
		vars.put("carriere_longue_oui", userChecklistGenerationData.isCarriereLongue);
		vars.put("carriere_longue_non", !userChecklistGenerationData.isCarriereLongue);
		return vars;
	}

	private boolean containsRegime(final UserChecklistGenerationData userChecklistGenerationData, final Regime regime) {
		return Arrays.asList(userChecklistGenerationData.getRegimes()).contains(regime);
	}

	private String getRegimeBaseHorsAlignes(final UserChecklistGenerationData userChecklistGenerationData) {
		String result = "";
		for (final Regime regime : userChecklistGenerationData.getRegimes()) {
			if (regime.getType() == BASE_AUTRE) {
				if (!result.isEmpty()) {
					result += ", ";
				}
				result += getNameFor(regime);
			}
		}
		return result;
	}

	private String getNameFor(final Regime regime) {
		if (regime.toString().equals(regime.getNom())) {
			return regime.toString();
		}
		return regime.toString() + " (" + regime.getNom() + ")";
	}

	private String getRegimeComplHorsAgircArrco(final UserChecklistGenerationData userChecklistGenerationData) {
		String result = "";
		for (final Regime regime : userChecklistGenerationData.getRegimes()) {
			if (regime.getType() == RegimeType.COMPLEMENTAIRE && regime != regime.AGIRC_ARRCO) {
				if (!result.isEmpty()) {
					result += ", ";
				}
				result += getNameFor(regime);
			}
		}
		return result;
	}

	private String getRegimeHorsAlignesEtHorsAgircArrco(final UserChecklistGenerationData userChecklistGenerationData) {
		String result = "";
		for (final Regime regime : userChecklistGenerationData.getRegimes()) {
			if (regime.getType() == BASE_AUTRE || (regime.getType() == RegimeType.COMPLEMENTAIRE && regime != regime.AGIRC_ARRCO)) {
				if (!result.isEmpty()) {
					result += ", ";
				}
				result += getNameFor(regime);
			}
		}
		return result;
	}
	

	private String getRegimeHorsAlignesOuCompl(final UserChecklistGenerationData userChecklistGenerationData) {
		
		Logger.debug("getRegimeHorsAlignesOuCompl");
		
		
		String result = "";
		for (final Regime regime : userChecklistGenerationData.getRegimes()) {
			
			Logger.debug("regime : " + regime);
			
			if (regime.getType() == BASE_AUTRE || regime.getType() == RegimeType.COMPLEMENTAIRE) {
				if (!result.isEmpty()) {
					result += ", ";
				}
				result += getNameFor(regime);
			}
		}
		
		Logger.debug("result : " + result);
		
		return result;
	}
}
