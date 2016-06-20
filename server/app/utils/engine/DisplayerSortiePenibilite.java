package utils.engine;

import java.util.HashMap;

import controllers.data.PostData;
import utils.engine.data.RenderData;
import utils.engine.data.enums.RegimeAligne;

public class DisplayerSortiePenibilite {

	public void fillData(final PostData postData, final RenderData renderData) {
		renderData.hidden_step = "displaySortiePenibilite";
		renderData.extras = new HashMap<>();
		final RegimeAligne regimeLiquidateur = postData.hidden_liquidateur;
		renderData.extras.put("urlInfosPenibilite", regimeLiquidateur.urlInfosPenibilite);
	}

}
