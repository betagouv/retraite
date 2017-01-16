package utils.engine;

import java.util.HashMap;
import java.util.Properties;

import cnav.architech.usilog.socle.framework.base.properties.PropertiesLoader;
import controllers.data.PostData;
import utils.engine.data.RenderData;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.utils.RetraiteConstants;
import utils.engine.utils.RetraitePropertiesLoader;

public class DisplayerSortiePenibilite {

	public void fillData(final PostData postData, final RenderData renderData) {
		
		renderData.hidden_step = "displaySortiePenibilite";

		if (renderData.extras == null) {
			renderData.extras = new HashMap<>();
		}
		final RegimeAligne regimeLiquidateur = postData.hidden_liquidateur;
		renderData.extras.put("urlInfosPenibilite", regimeLiquidateur == null ? RetraitePropertiesLoader.getInstance().getProperty("GIP_UR.urlInfosPenibilite") : regimeLiquidateur.urlInfosPenibilite);
	}

}
