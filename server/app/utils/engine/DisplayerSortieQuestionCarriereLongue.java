package utils.engine;

import java.util.HashMap;

import controllers.data.PostData;
import models.AnneesEtMois;
import utils.engine.data.RenderData;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.utils.AgeLegalEvaluator;

public class DisplayerSortieQuestionCarriereLongue {

	private final AgeLegalEvaluator ageLegalEvaluator;

	public DisplayerSortieQuestionCarriereLongue(final AgeLegalEvaluator ageLegalEvaluator) {
		this.ageLegalEvaluator = ageLegalEvaluator;
	}

	public void fillData(final PostData data, final RenderData renderData) {
		renderData.hidden_step = "displaySortieQuestionCarriereLongue";
		renderData.hidden_departMois = data.departMois != null ? data.departMois : data.hidden_departMois;
		renderData.hidden_departAnnee = data.departAnnee != null ? data.departAnnee : data.hidden_departAnnee;
		renderData.extras = new HashMap<>();
		renderData.extras.put("age",
				ageLegalEvaluator.calculeAgeADateDonnee(data.hidden_naissance, renderData.hidden_departMois, renderData.hidden_departAnnee));
		final AnneesEtMois ageLegalPourPartir = ageLegalEvaluator.getAgeLegalPourPartir(data.hidden_naissance);
		renderData.extras.put("ageLegalPourPartir", ageLegalPourPartir);
		renderData.extras.put("dateDepartPossible", ageLegalEvaluator.calculeDateEnAjoutant(data.hidden_naissance, ageLegalPourPartir));
		final RegimeAligne regimeLiquidateur = data.hidden_liquidateur;
		renderData.extras.put("urlAgeDepart", regimeLiquidateur.urlAgeDepart);
		renderData.extras.put("urlDispositifDepartAvantAgeLegal", regimeLiquidateur.urlDispositifDepartAvantAgeLegal);
	}

}
