package utils.engine;

import static utils.engine.EngineUtils.firstNotNull;

import java.util.HashMap;

import controllers.data.PostData;
import models.AnneesEtMois;
import utils.engine.data.RenderData;
import utils.engine.intern.StepFormsDataProvider;
import utils.engine.utils.AgeLegalEvaluator;

public class DisplayerDepartureDate {

	private final StepFormsDataProvider stepFormsDataProvider;
	private final AgeLegalEvaluator ageLegalEvaluator;

	public DisplayerDepartureDate(final StepFormsDataProvider stepFormsDataProvider, final AgeLegalEvaluator ageLegalEvaluator) {
		this.stepFormsDataProvider = stepFormsDataProvider;
		this.ageLegalEvaluator = ageLegalEvaluator;
	}

	public void fillData(final PostData data, final RenderData renderData, final String regimes) {
		renderData.hidden_step = "displayDepartureDate";
		if (regimes != null) {
			renderData.hidden_regimes = regimes;
		}
		renderData.listeMoisAvecPremier = stepFormsDataProvider.getListMoisAvecPremier();
		renderData.listeAnneesDepart = stepFormsDataProvider.getListAnneesDepart();

		if (renderData.extras == null) {
			renderData.extras = new HashMap<>();
		}
		final String naissance = firstNotNull(data.hidden_naissance, data.naissance);
		final AnneesEtMois ageLegalPourPartir = ageLegalEvaluator.getAgeLegalPourPartir(naissance);
		renderData.extras.put("ageLegalPourPartir", ageLegalPourPartir);
		renderData.extras.put("dateDepartPossible", ageLegalEvaluator.calculeDateEnAjoutant(naissance, ageLegalPourPartir));
	}

}
