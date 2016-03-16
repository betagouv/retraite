package utils.engine;

import controllers.data.PostData;
import utils.engine.data.RenderData;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.intern.QuestionsLiquidateurBuilder;

public class DisplayerLiquidateurQuestions {

	private final QuestionsLiquidateurBuilder questionsLiquidateurBuilder;

	public DisplayerLiquidateurQuestions(final QuestionsLiquidateurBuilder questionsLiquidateurBuilder) {
		this.questionsLiquidateurBuilder = questionsLiquidateurBuilder;
	}

	public void fillData(final PostData data, final RenderData renderData, final String regimes, final RegimeAligne[] regimesAlignes) {
		renderData.hidden_nom = data.nom;
		renderData.hidden_naissance = data.naissance;
		renderData.hidden_nir = data.nir;
		renderData.hidden_step = "displayLiquidateurQuestions";
		renderData.hidden_regimes = regimes;
		renderData.questionsLiquidateur = questionsLiquidateurBuilder.buildQuestions(regimesAlignes);
	}

}
