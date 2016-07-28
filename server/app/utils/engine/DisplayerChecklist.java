package utils.engine;

import static utils.engine.EngineUtils.firstNotNull;

import controllers.data.PostData;
import play.Logger;
import utils.DateUtils;
import utils.RetraiteException;
import utils.engine.data.MonthAndYear;
import utils.engine.data.RenderData;
import utils.engine.data.StringPairsList;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.StepFormsDataProvider;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;
import utils.engine.utils.DateProvider;

public class DisplayerChecklist {

	private final UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder;
	private final UserChecklistGenerator userChecklistGenerator;
	private final DateProvider dateProvider;
	private final CalculateurRegimeAlignes calculateurRegimeAlignes;
	private final StepFormsDataProvider stepFormsDataProvider;
	private final ResponsesHistoryToStringsConverter responsesHistoryToStringsConverter;

	public DisplayerChecklist(final UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder,
			final UserChecklistGenerator userChecklistGenerator, final DateProvider dateProvider,
			final CalculateurRegimeAlignes calculateurRegimeAlignes, final StepFormsDataProvider stepFormsDataProvider,
			final ResponsesHistoryToStringsConverter responsesHistoryToStringsConverter) {
		this.userChecklistGenerationDataBuilder = userChecklistGenerationDataBuilder;
		this.userChecklistGenerator = userChecklistGenerator;
		this.dateProvider = dateProvider;
		this.calculateurRegimeAlignes = calculateurRegimeAlignes;
		this.stepFormsDataProvider = stepFormsDataProvider;
		this.responsesHistoryToStringsConverter = responsesHistoryToStringsConverter;
	}

	public void fillData(final PostData data, final RenderData renderData) {
		final RegimeAligne[] regimesAlignes = calculateurRegimeAlignes.getRegimesAlignes(data.hidden_regimes);
		final RegimeAligne regimeLiquidateur = data.hidden_liquidateur;
		final Regime[] regimes = Regime.fromStringList(data.hidden_regimes);
		// Temp
		final String departMois = firstNotNull(data.departMois, data.hidden_departMois);
		final String departAnnee = firstNotNull(data.departAnnee, data.hidden_departAnnee);
		final MonthAndYear dateDepart = new MonthAndYear(departMois, departAnnee);
		final String departement = firstNotNull(data.departement, data.hidden_departement);
		final UserChecklistGenerationData userChecklistGenerationData = userChecklistGenerationDataBuilder.build(dateDepart, departement,
				regimes, regimesAlignes, regimeLiquidateur, true, data.hidden_attestationCarriereLongue, data.hidden_userStatus, data.isPDF,
				data.hidden_regimesInfosJsonStr);
		renderData.hidden_step = "displayCheckList";
		try {
			renderData.userChecklist = userChecklistGenerator.generate(userChecklistGenerationData, regimeLiquidateur);
		} catch (final RetraiteException e) {
			Logger.error(e, "Impossible de déterminer le régime liquidateur");
			renderData.errorMessage = "Désolé, impossible de déterminer le régime liquidateur...";
		}
		renderData.userInfos = new StringPairsList();
		renderData.userInfos.add("Document produit le", DateUtils.format(dateProvider.getCurrentDate()));
		renderData.userInfos.add("Nom de naissance", data.hidden_nom);
		if (departMois != null && departAnnee != null) {
			renderData.userInfos.add("Date de départ envisagée", "01/" + (departMois.length() == 1 ? "0" : "") + departMois + "/" + departAnnee);
		}
		renderData.userInfos.add("Département de résidence", stepFormsDataProvider.getDepartementName(departement));

		renderData.questionsAndResponses = responsesHistoryToStringsConverter.convert(data.hidden_liquidateurReponsesHistory);
	}

}
