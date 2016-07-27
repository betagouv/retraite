package utils.engine.data.enums;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_A;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.QUESTION_B;

import org.junit.Test;

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
