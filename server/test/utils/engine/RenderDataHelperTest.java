package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static utils.JsonUtils.fromJson;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_A;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import utils.engine.data.CommonExchangeData;
import utils.engine.data.QuestionAndResponses;
import utils.engine.data.QuestionAndResponsesList;
import utils.engine.data.QuestionChoice;
import utils.engine.data.QuestionLiquidateur;
import utils.engine.data.RenderData;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;

public class RenderDataHelperTest {

	@Mock
	private LiquidateurQuestionDescriptorHelper liquidateurQuestionChoiceHelperMock;
	
	@Before
	public void setUp() {
		liquidateurQuestionChoiceHelperMock = Mockito.mock(LiquidateurQuestionDescriptorHelper.class);
	}
	
	@Test
	public void getQuestionsList_with_null_liquidateurReponsesHistory_sould_return_empty_list() {
		
		RenderDataHelper renderDataHelper = new RenderDataHelper(new RenderData());
		
		List<QuestionLiquidateur> questionsList = renderDataHelper.getQuestionsList(null);
		
		assertThat(questionsList).isEmpty();
	}
	
	@Test
	public void getQuestionsList_with_empty_liquidateurReponsesHistory_sould_return_empty_list() {
		
		RenderData renderData = new RenderData();
		renderData.hidden_liquidateurReponsesHistory = "";
		RenderDataHelper renderDataHelper = new RenderDataHelper(renderData);
		
		List<QuestionLiquidateur> questionsList = renderDataHelper.getQuestionsList(null);
		
		assertThat(questionsList).isEmpty();
	}
	
	@Test
	public void getQuestionsList_with_liquidateurReponsesHistory_sould_return_list() {
		
		RenderData renderData = new RenderData();
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV , RSI};	
		renderData.hidden_liquidateur = RSI;
		renderData.hidden_liquidateurReponsesHistory = "[{'question':'QUESTION_A','responses':['DEUX_ACTIVITES']},{'question':'QUESTION_C','responses':['INDEP_AVANT_73','INVALIDITE_RSI']},{'question':'QUESTION_E','responses':['OUI']},{'question':'QUESTION_F','responses':['NON']}]";
				
		RenderDataHelper renderDataHelper = new RenderDataHelper(renderData) {
			@Override
			protected LiquidateurQuestionDescriptorHelper getLiquidateurQuestionDescriptorHelper(LiquidateurQuestionDescriptor liquidateurQuestionDescriptor) {
				return liquidateurQuestionChoiceHelperMock; 
			}
		};
		
		List<QuestionChoice> listeQuestions = new ArrayList<>();
		listeQuestions.add(new QuestionChoice("choix1", QuestionChoiceValue.INDEP));
		listeQuestions.add(new QuestionChoice("choix2", QuestionChoiceValue.DEUX_ACTIVITES));
		Mockito.when(liquidateurQuestionChoiceHelperMock.getSpecificsChoices(Mockito.any(RegimeAligne[].class), Mockito.any(CommonExchangeData.class))).thenReturn(listeQuestions);
		
		List<QuestionLiquidateur> questionsList = renderDataHelper.getQuestionsList(regimesAlignes);
		
		assertThat(questionsList).isNotEmpty();
		assertThat(questionsList.size()).isEqualTo(4);
		for (QuestionLiquidateur questionLiquidateur : questionsList) {
			assertThat(questionLiquidateur.choices).isNotEmpty();
			assertThat(questionLiquidateur.choices.size()).isEqualTo(2);
		}
		
	}
	

	
	@Test
	public void getIsChecked_sould_return_false() {
		
		List<String> reponses = new ArrayList<>();
		reponses.add("reponse 1");
		reponses.add("reponse 2");
		QuestionAndResponses questionAndResponses = new QuestionAndResponses("QUESTION_A", reponses);
		QuestionChoice questionChoice = new QuestionChoice("QUESTION_A", QuestionChoiceValue.CONJOINT);
		
		RenderDataHelper renderDataHelper = new RenderDataHelper(new RenderData());
		Boolean result = renderDataHelper.isChecked(questionChoice, questionAndResponses);
		
		assertThat(result).isFalse();
	}
	
	@Test
	public void getIsChecked_sould_return_true() {
		
		List<String> reponses = new ArrayList<>();
		reponses.add("reponse 1");
		reponses.add("CONJOINT");
		QuestionAndResponses questionAndResponses = new QuestionAndResponses("QUESTION_A", reponses);
		QuestionChoice questionChoice = new QuestionChoice("QUESTION_A", QuestionChoiceValue.CONJOINT);
		
		RenderDataHelper renderDataHelper = new RenderDataHelper(new RenderData());
		Boolean result = renderDataHelper.isChecked(questionChoice, questionAndResponses);
		
		assertThat(result).isTrue();
	}
}
