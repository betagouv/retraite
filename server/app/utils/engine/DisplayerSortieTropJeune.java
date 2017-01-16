package utils.engine;

import java.util.HashMap;
import java.util.Properties;

import cnav.architech.usilog.socle.framework.base.properties.PropertiesLoader;
import controllers.data.PostData;
import controllers.utils.Look;
import utils.engine.data.RenderData;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.utils.RetraiteConstants;
import utils.engine.utils.RetraitePropertiesLoader;

public class DisplayerSortieTropJeune {

	public void fillData(final PostData postData, final RenderData renderData) {
		
		renderData.hidden_step = "displaySortieTropJeune";

		if (renderData.extras == null) {
			renderData.extras = new HashMap<>();
		}
		final RegimeAligne regime = getRegime(postData.look);
		if (regime == null) {
			renderData.extras.put("urlDroits", RetraitePropertiesLoader.getInstance().getProperty("GIP_UR.urlDroits"));
			renderData.extras.put("urlInfosDepartRetraite", RetraitePropertiesLoader.getInstance().getProperty("GIP_UR.urlInfosDepartRetraite"));
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
