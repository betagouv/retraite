package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.EngineUtils.firstNotNull;

import org.junit.Test;

public class EngineUtilsTest {

	@Test
	public void test_firstNotNull() {

		assertThat(firstNotNull("a")).isEqualTo("a");
		assertThat(firstNotNull("a", "b")).isEqualTo("a");
		assertThat(firstNotNull(null, "b")).isEqualTo("b");
		assertThat(firstNotNull("", "b")).isEqualTo("b");
		assertThat(firstNotNull(null, "")).isEqualTo("");
		assertThat(firstNotNull("", null)).isEqualTo("");

		assertThat(firstNotNull(1)).isEqualTo(1);
		assertThat(firstNotNull(1, 2)).isEqualTo(1);
		assertThat(firstNotNull(null, 2)).isEqualTo(2);
	}

}
