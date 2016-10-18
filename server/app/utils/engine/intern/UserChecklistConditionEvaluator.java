package utils.engine.intern;

import static java.util.Arrays.asList;
import static utils.engine.data.enums.Regime.AGIRC_ARRCO;
import static utils.engine.data.enums.Regime.RegimeType.BASE_ALIGNE;
import static utils.engine.data.enums.Regime.RegimeType.BASE_AUTRE;
import static utils.engine.data.enums.Regime.RegimeType.COMPLEMENTAIRE;

import models.Condition;
import play.Logger;
import utils.RetraiteException;
import utils.engine.data.ConditionDelai;
import utils.engine.data.MonthAndYear;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;

public class UserChecklistConditionEvaluator {

	private final UserChecklistConditionDelaiEvaluator userChecklistConditionDelaiEvaluator;

	public UserChecklistConditionEvaluator(final UserChecklistConditionDelaiEvaluator userChecklistConditionDelaiEvaluator) {
		this.userChecklistConditionDelaiEvaluator = userChecklistConditionDelaiEvaluator;
	}

	public boolean isVerified(final Condition condition, final UserChecklistGenerationData userChecklistGenerationData) {
		final String type = condition.props.get("type");
		switch (type) {
		case "delai":
			return isVerified_delai(condition, userChecklistGenerationData.getDateDepart());
		case "regimeDetecte":
			return isVerified_regimeDetecte(condition, userChecklistGenerationData.getRegimes());
		case "statut":
			return isVerified_statut(condition, userChecklistGenerationData);
		case "carriere-longue-non":
			return !userChecklistGenerationData.isCarriereLongue;
		case "carriere-longue-oui":
			return userChecklistGenerationData.isCarriereLongue;
		}
		Logger.error(new RetraiteException(""), "Le type de condition '" + type + "' n'est pas géré !");
		return true;
	}

	private boolean isVerified_delai(final Condition condition, final MonthAndYear dateDepart) {
		final String plusOuMoins = condition.props.get("plusOuMoins");
		final String nombre = condition.props.get("nombre");
		final String unite = condition.props.get("unite");
		return userChecklistConditionDelaiEvaluator.isVerified(new ConditionDelai(plusOuMoins, nombre, unite), dateDepart);
	}

	private boolean isVerified_regimeDetecte(final Condition condition, final Regime[] regimes) {
		final String regime = condition.props.get("regime");
		switch (regime) {
		case "agirc-arrco":
			return contains(regimes, AGIRC_ARRCO);
		case "regimes-base-hors-alignés":
			for (final Regime aRegime : regimes) {
				if (aRegime.getType() == BASE_AUTRE) {
					return true;
				}
			}
			return false;
		case "regimes-complémentaires-hors-agirc-arrco":
			for (final Regime aRegime : regimes) {
				if (aRegime.getType() == COMPLEMENTAIRE && aRegime != AGIRC_ARRCO) {
					return true;
				}
			}
			return false;
		case "regimes-hors-alignés-et-hors-agirc-arrco":
			for (final Regime aRegime : regimes) {
				if (aRegime.getType() != BASE_ALIGNE && aRegime != AGIRC_ARRCO) {
					return true;
				}
			}
			return false;
		case "regimes-hors-alignes-ou-regimes-compl":
			for (final Regime aRegime : regimes) {
				if (aRegime.getType() == BASE_AUTRE || aRegime.getType() == COMPLEMENTAIRE) {
					return true;
				}
			}
			return false;
		}
		Logger.info("Pour la condition 'regimeDetecte', le regime '" + regime + "' n'est pas géré !");
		return true;
	}

	private boolean contains(final Regime[] regimes, final Regime regime) {
		return asList(regimes).contains(regime);
	}

	private boolean isVerified_statut(final Condition condition, final UserChecklistGenerationData userChecklistGenerationData) {
		final String statut = condition.props.get("statut");
		switch (statut) {
		case "nsa":
			return userChecklistGenerationData.isNSA;
		case "sa":
			return userChecklistGenerationData.isSA;
		case "conjoint-collaborateur":
			return userChecklistGenerationData.isConjointCollaborateur;
		}
		Logger.info("Pour la condition 'statut', le statut '" + statut + "' n'est pas géré !");
		return true;
	}
}
