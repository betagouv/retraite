package utils.engine;

import controllers.data.PostData;
import utils.engine.data.RenderData;
import utils.engine.intern.QuestionsComplementairesBuilder;

public class DisplayerAdditionalQuestions {

	private final QuestionsComplementairesBuilder questionsComplementairesBuilder;

	public DisplayerAdditionalQuestions(final QuestionsComplementairesBuilder questionsComplementairesBuilder) {
		this.questionsComplementairesBuilder = questionsComplementairesBuilder;
	}

	public void fillData(final PostData data, final RenderData renderData) {
		renderData.hidden_step = "displayAdditionalQuestions";
		renderData.hidden_departMois = data.departMois != null ? data.departMois : data.hidden_departMois;
		renderData.hidden_departAnnee = data.departAnnee != null ? data.departAnnee : data.hidden_departAnnee;
		renderData.questionsComplementaires = questionsComplementairesBuilder.buildQuestions();
	}

}
