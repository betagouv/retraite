package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_A;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_B;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_C;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_D;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionChoiceValue.SALARIE;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_CPAM;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_MSA;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_RSI;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.engine.data.QuestionChoice;
import utils.engine.data.RenderData;
import utils.engine.data.enums.RegimeAligne;

public class LiquidateurQuestionDescriptorHelperTest {
		
	@Test
	public void getSpecificsChoices_QuestionA_sould_return_no_choices() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };	
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_A);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes);
		
		assertThat(questions).isNull();
	}
	
	@Test
	public void getSpecificsChoices_QuestionB_with_all_regimes_sould_return_no_choices() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV , RSI,  MSA};	
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_B);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes);
		
		assertThat(questions).isNull();
	}
	
	@Test
	public void getSpecificsChoices_QuestionB_with_CNAV_only_sould_return_choice_SALARIE() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };	
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_B);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes);
		
		assertThat(questions.size()).isEqualTo(1);
		assertThat(questions).contains(QUESTION_B.getChoice(SALARIE));
	}
	
	@Test
	public void getSpecificsChoices_QuestionB_with_RSI_only_sould_return_choices_INDEP() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };	
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_B);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes);
		
		assertThat(questions.size()).isEqualTo(2);
		assertThat(questions).contains(QUESTION_B.getChoice(INDEP));
		assertThat(questions).contains(QUESTION_B.getChoice(CONJOINT_INDEP));
	}
	
	@Test
	public void getSpecificsChoices_QuestionB_with_MSA_only_sould_return_choices_NSA_SA() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };	
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_B);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes);

		assertThat(questions.size()).isEqualTo(3);
		assertThat(questions).contains(QUESTION_B.getChoice(NSA));
		assertThat(questions).contains(QUESTION_B.getChoice(SA));
		assertThat(questions).contains(QUESTION_B.getChoice(DEUX_ACTIVITES));
	}	
	
	@Test
	public void getSpecificsChoices_QuestionB_with_2_regimes_sould_return_choices_DEUX_ACTIVITES() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV, MSA };	
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_B);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes);

		assertThat(questions.size()).isEqualTo(4);
		assertThat(questions).contains(QUESTION_B.getChoice(NSA));
		assertThat(questions).contains(QUESTION_B.getChoice(SA));
		assertThat(questions).contains(QUESTION_B.getChoice(SALARIE));
		assertThat(questions).contains(QUESTION_B.getChoice(DEUX_ACTIVITES));
	}
	
	@Test
	public void getSpecificsChoices_QuestionC_no_liquidateur_sould_return_no_choices() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };
		RegimeAligne liquidateur = null;
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_C);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes, liquidateur);
		
		assertThat(questions).isNull();
	}
	
	@Test
	public void getSpecificsChoices_QuestionC_liquidateur_CNAV_sould_return_no_choices() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };
		RegimeAligne liquidateur = CNAV;
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_C);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes, liquidateur);
		
		assertThat(questions).isNull();
	}
	
	@Test
	public void getSpecificsChoices_QuestionC_liquidateur_RSI_sould_return_choices() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };	
		RegimeAligne liquidateur = RSI; 
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_C);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes, liquidateur);

		assertThat(questions.size()).isEqualTo(2);
		assertThat(questions).contains(QUESTION_C.getChoice(PENIBILITE));
		assertThat(questions).contains(QUESTION_C.getChoice(INVALIDITE_RSI));
	}
	
	@Test
	public void getSpecificsChoices_QuestionD_with_all_regimes_sould_return_no_choices() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV , RSI,  MSA};	
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_D);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes);
		
		assertThat(questions).isNull();
	}
	
	@Test
	public void getSpecificsChoices_QuestionD_with_CNAV_sould_return_choice_CPAM() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };	
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_D);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes);
		
		assertThat(questions.size()).isEqualTo(1);
		assertThat(questions).contains(QUESTION_D.getChoice(SANTE_CPAM));
	}
	
	@Test
	public void getSpecificsChoices_QuestionD_with_RSI_sould_return_choice_SANTE_RSI() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV, RSI };
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_D);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes);
		
		assertThat(questions.size()).isEqualTo(2);
		assertThat(questions).contains(QUESTION_D.getChoice(SANTE_CPAM));
		assertThat(questions).contains(QUESTION_D.getChoice(SANTE_RSI));
	}
	
	@Test
	public void getSpecificsChoices_QuestionD_with_MSA_sould_return_choice_SANTE_RSI() {
		RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA, RSI };	
		
		LiquidateurQuestionDescriptorHelper helper = new LiquidateurQuestionDescriptorHelper(QUESTION_D);
		List<QuestionChoice> questions =  helper.getSpecificsChoices(regimesAlignes);
		
		assertThat(questions.size()).isEqualTo(2);
		assertThat(questions).contains(QUESTION_D.getChoice(SANTE_MSA));
		assertThat(questions).contains(QUESTION_D.getChoice(SANTE_RSI));
	}
}
