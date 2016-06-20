package utils.engine;

import java.util.HashMap;

import controllers.data.PostData;
import controllers.utils.Look;
import utils.engine.data.RenderData;
import utils.engine.data.RetraiteConstants;
import utils.engine.data.enums.RegimeAligne;

public class DisplayerSortieTropJeune {

	public void fillData(final PostData postData, final RenderData renderData) {
		renderData.hidden_step = "displaySortieTropJeune";
		renderData.extras = new HashMap<>();
		final RegimeAligne regime = getRegime(postData.look);
		if (regime == null) {
			renderData.extras.put("urlDroits", RetraiteConstants.URL_DROITS_GIP_UR);
			renderData.extras.put("urlInfosDepartRetraite", RetraiteConstants.URL_INFOS_DEPART_RETRAITE_GIP_UR);
		} else {
			renderData.extras.put("urlDroits", regime.urlDroits);
			renderData.extras.put("urlInfosDepartRetraite", regime.urlInfosDepartRetraite);
		}
	}

	private RegimeAligne getRegime(final Look look) {
		if (look == Look.CNAV) {
			return RegimeAligne.CNAV;
		}
		if (look == Look.MSA) {
			return RegimeAligne.MSA;
		}
		if (look == Look.RSI) {
			return RegimeAligne.RSI;
		}
		return null;
	}

}
