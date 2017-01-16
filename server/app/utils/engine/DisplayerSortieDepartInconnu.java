package utils.engine;

import java.util.HashMap;

import controllers.data.PostData;
import utils.engine.data.RenderData;
import utils.engine.data.enums.RegimeAligne;

public class DisplayerSortieDepartInconnu {

	public void fillData(final PostData postData, final RenderData renderData) {
		renderData.hidden_step = "displaySortieDepartInconnu";

		if (renderData.extras == null) {
			renderData.extras = new HashMap<>();
		}
		final RegimeAligne regimeLiquidateur = postData.hidden_liquidateur;
		renderData.extras.put("urlAgeDepart", regimeLiquidateur.urlAgeDepart);
		renderData.extras.put("urlDroits", regimeLiquidateur.urlDroits);
		renderData.extras.put("urlSimulMontant", regimeLiquidateur.urlSimulMontant);
	}

}
