package utils.engine.data.enums;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_A;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_B;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_C;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_D;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_E;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_F;
import static utils.engine.data.enums.QuestionChoiceValue.SALARIE;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_MSA;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_CPAM;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_RSI;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import java.util.List;

import org.junit.Test;

import controllers.data.PostData;
import utils.engine.data.QuestionChoice;
import utils.engine.data.RenderData;

public class LiquidateurQuestionDescriptorTest {

	@Test
	public void test_isLast() {
		final LiquidateurQuestionDescriptor[] values = LiquidateurQuestionDescriptor.values();
		for (int i = 0; i < values.length - 1; i++) {
			final LiquidateurQuestionDescriptor desc = values[i];
			assertThat(desc.isLast()).isFalse();
		}
		final LiquidateurQuestionDescriptor desc = values[values.length - 1];
		assertThat(desc.isLast()).isTrue();
	}

	@Test
	public void test_isBefore() {
		assertThat(QUESTION_A.isBefore(QUESTION_A)).isFalse();
		assertThat(QUESTION_B.isBefore(QUESTION_B)).isFalse();
		assertThat(QUESTION_B.isBefore(QUESTION_A)).isFalse();

		assertThat(QUESTION_A.isBefore(QUESTION_B)).isTrue();
	}
}
