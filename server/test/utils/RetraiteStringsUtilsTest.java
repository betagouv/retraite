package utils;

import static org.fest.assertions.Assertions.assertThat;
import static utils.RetraiteStringsUtils.getMinIndex;

import org.junit.Test;

public class RetraiteStringsUtilsTest {

	@Test
	public void test_getMinIndex() {

		assertThat(getMinIndex(-1)).isEqualTo(-1);
		assertThat(getMinIndex(3)).isEqualTo(3);
		assertThat(getMinIndex(5, 3)).isEqualTo(3);
		assertThat(getMinIndex(5, -1, 3)).isEqualTo(3);
		assertThat(getMinIndex(-1, -1, -1)).isEqualTo(-1);

	}
}
