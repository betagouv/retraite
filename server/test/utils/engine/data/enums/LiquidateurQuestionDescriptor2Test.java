package utils.engine.data.enums;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_A;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_B;

import org.junit.Test;

public class LiquidateurQuestionDescriptor2Test {

	@Test
	public void test_isLast() {
		final LiquidateurQuestionDescriptor2[] values = LiquidateurQuestionDescriptor2.values();
		for (int i = 0; i < values.length - 1; i++) {
			final LiquidateurQuestionDescriptor2 desc = values[i];
			assertThat(desc.isLast()).isFalse();
		}
		final LiquidateurQuestionDescriptor2 desc = values[values.length - 1];
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
