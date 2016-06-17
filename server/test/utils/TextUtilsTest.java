package utils;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class TextUtilsTest {

	@Test
	public void should_return_null_for_empty() {

		assertThat(TextUtils.isLikeEmpty("<p></p>")).isTrue();
		assertThat(TextUtils.isLikeEmpty("<br/>")).isTrue();

		assertThat(TextUtils.isLikeEmpty("<p>pas vide</p>")).isFalse();
	}

}
