package utils.engine;

import static java.util.Arrays.asList;
import static utils.engine.EngineUtils.firstNotNull;
import static utils.engine.data.enums.UserStatus.STATUS_CHEF;

import java.util.HashMap;
import java.util.List;

import controllers.data.PostData;
import play.Logger;
import utils.DateUtils;
import utils.RetraiteException;
import utils.engine.data.MonthAndYear;
import utils.engine.data.RenderData;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;
import utils.engine.utils.DateProvider;

public class DisplayerChecklist {

	private final UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder;
	private final UserChecklistGenerator userChecklistGenerator;
	private final DateProvider dateProvider;
	private final CalculateurRegimeAlignes calculateurRegimeAlignes;

	public DisplayerChecklist(final UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder,
			final UserChecklistGenerator userChecklistGenerator, final DateProvider dateProvider,
			final CalculateurRegimeAlignes calculateurRegimeAlignes) {
		this.userChecklistGenerationDataBuilder = userChecklistGenerationDataBuilder;
		this.userChecklistGenerator = userChecklistGenerator;
		this.dateProvider = dateProvider;
		this.calculateurRegimeAlignes = calculateurRegimeAlignes;
	}

	public void fillData(final PostData data, final RenderData renderData) {
		final RegimeAligne[] regimesAlignes = calculateurRegimeAlignes.getRegimesAlignes(data.hidden_regimes);
		final RegimeAligne regimeLiquidateur = data.hidden_liquidateur;
		final List<UserStatus> userStatus = asList(STATUS_CHEF);
		final Regime[] regimes = Regime.fromStringList(data.hidden_regimes);
		// [XN-29/03/2016-En attendant de remettre les questions complémentaires, on force l'affichage du parcours démat]
		final boolean isParcoursDemat = true;// questionComplementairesEvaluator.isParcoursDemat(complementReponses);
		// Temp
		final String departMois = firstNotNull(data.departMois, data.hidden_departMois);
		final String departAnnee = firstNotNull(data.departAnnee, data.hidden_departAnnee);
		final MonthAndYear dateDepart = new MonthAndYear(departMois, departAnnee);
		final String departement = firstNotNull(data.departement, data.hidden_departement);
		final UserChecklistGenerationData userChecklistGenerationData = userChecklistGenerationDataBuilder.build(dateDepart, departement,
				regimes, regimesAlignes, regimeLiquidateur, isParcoursDemat, true, data.hidden_attestationCarriereLongue, userStatus);
		renderData.hidden_step = "displayCheckList";
		try {
			renderData.userChecklist = userChecklistGenerator.generate(userChecklistGenerationData, regimeLiquidateur);
		} catch (final RetraiteException e) {
			Logger.error(e, "Impossible de déterminer le régime liquidateur");
			renderData.errorMessage = "Désolé, impossible de déterminer le régime liquidateur...";
		}
		renderData.userInfos = new HashMap<>();
		renderData.userInfos.put("Document produit le", DateUtils.format(dateProvider.getCurrentDate()));
		renderData.userInfos.put("Nom de naissance", data.hidden_nom);
		if (departMois != null && departAnnee != null) {
			renderData.userInfos.put("Date de départ envisagée", "01/" + (departMois.length() == 1 ? "0" : "") + departMois + "/" + departAnnee);
		}
	}

}
