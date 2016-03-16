package utils.engine;

import controllers.data.PostData;
import utils.engine.data.RenderData;
import utils.engine.intern.StepFormsDataProvider;

public class DisplayerDepartureDate {

	private final StepFormsDataProvider stepFormsDataProvider;

	public DisplayerDepartureDate(final StepFormsDataProvider stepFormsDataProvider) {
		this.stepFormsDataProvider = stepFormsDataProvider;
	}

	public void fillData(final PostData data, final RenderData renderData, final String regimes) {
		renderData.hidden_step = "displayDepartureDate";
		if (regimes != null) {
			renderData.hidden_regimes = regimes;
		}
		renderData.listeMoisAvecPremier = stepFormsDataProvider.getListMoisAvecPremier();
		renderData.listeAnneesDepart = stepFormsDataProvider.getListAnneesDepart();
	}

}
