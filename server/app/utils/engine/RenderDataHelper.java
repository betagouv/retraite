package utils.engine;

import static utils.JsonUtils.fromJson;

import java.util.ArrayList;
import java.util.List;

import utils.engine.data.QuestionAndResponses;
import utils.engine.data.QuestionAndResponsesList;
import utils.engine.data.QuestionChoice;
import utils.engine.data.QuestionLiquidateur;
import utils.engine.data.RenderData;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.RegimeAligne;

public class RenderDataHelper {

	private final RenderData renderData;

	public RenderDataHelper(RenderData renderData) {
		super();
		this.renderData = renderData;
	}
	
	public List<QuestionLiquidateur> getQuestionsList(final RegimeAligne[] regimesAlignes) {
		
		final List<QuestionLiquidateur> questionsList = new ArrayList<>();
		
		if (renderData.hidden_liquidateurReponsesHistory != null && !renderData.hidden_liquidateurReponsesHistory.isEmpty()) {
			final List<QuestionAndResponses> list = fromJson(renderData.hidden_liquidateurReponsesHistory, QuestionAndResponsesList.class);
			for (final QuestionAndResponses questionAndResponses : list) {
				
				final LiquidateurQuestionDescriptor liquidateurQuestionDescriptor = LiquidateurQuestionDescriptor.valueOf(questionAndResponses.question);
				
				QuestionLiquidateur question = new QuestionLiquidateur();
				question.liquidateurQuestionDescriptor = liquidateurQuestionDescriptor;
				question.choices = getLiquidateurQuestionDescriptorHelper(liquidateurQuestionDescriptor).getSpecificsChoices(regimesAlignes, questionAndResponses.liquidateur); 
				
				//check des questions associées aux réponses
				for (QuestionChoice questionChoice : liquidateurQuestionDescriptor.getQuestionChoices()) {
					questionChoice.setChecked(isChecked(questionChoice, questionAndResponses));
				}
				if (question.choices != null) {
					for (QuestionChoice questionChoice : question.choices) {
						questionChoice.setChecked(isChecked(questionChoice, questionAndResponses));
					} 
				}
				questionsList.add(question);
			}
		}
		
		return questionsList;
	}
	
	protected Boolean isChecked(QuestionChoice questionChoice, QuestionAndResponses questionAndResponses) {
		return questionAndResponses.responses.contains(questionChoice.getValue().name());
	}
	
	//Appel au constructeur déporté pour pouvoir mocker dans TU
	protected LiquidateurQuestionDescriptorHelper getLiquidateurQuestionDescriptorHelper(LiquidateurQuestionDescriptor liquidateurQuestionDescriptor) {
		return new LiquidateurQuestionDescriptorHelper(liquidateurQuestionDescriptor); 
	}
	
}
