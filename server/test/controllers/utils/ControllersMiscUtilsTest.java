package controllers.utils;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import play.mvc.Scope.Params;

public class ControllersMiscUtilsTest {

	@Test
	public void test_getLook() {
		{
			final Params params = new MyParams("look", null);
			assertThat(ControllersMiscUtils.getLook(params)).isEqualTo("style");
		}
		{
			final Params params = new MyParams("look", "cnav");
			assertThat(ControllersMiscUtils.getLook(params)).isEqualTo("cnav");
		}
		{
			final Params params = new MyParams("look", "msa");
			assertThat(ControllersMiscUtils.getLook(params)).isEqualTo("msa");
		}
		{
			final Params params = new MyParams("look", "rsi");
			assertThat(ControllersMiscUtils.getLook(params)).isEqualTo("rsi");
		}
		{
			final Params params = new MyParams("look", "xxx");
			assertThat(ControllersMiscUtils.getLook(params)).isEqualTo("style");
		}
	}

	@Test
	public void test_computeActionQueryParams() {
		assertThat(ControllersMiscUtils.computeActionQueryParams(false, "style")).isEqualTo("");
		assertThat(ControllersMiscUtils.computeActionQueryParams(true, "style")).isEqualTo("?test");
		assertThat(ControllersMiscUtils.computeActionQueryParams(false, "cnav")).isEqualTo("?look=cnav");
		assertThat(ControllersMiscUtils.computeActionQueryParams(true, "rsi")).isEqualTo("?test&look=rsi");
	}

	private static class MyParams extends Params {
		private final String key;
		private final String value;

		public MyParams(final String key, final String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String get(final String key) {
			if (key.equals(this.key)) {
				return this.value;
			}
			return super.get(key);
		}
	}
}
