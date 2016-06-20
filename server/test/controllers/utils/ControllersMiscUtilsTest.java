package controllers.utils;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import play.mvc.Scope.Params;

public class ControllersMiscUtilsTest {

	@Test
	public void test_getLook() {
		{
			final Params params = new MyParams("look", null);
			assertThat(ControllersMiscUtils.getLook(params)).isEqualTo(Look.GENERIC);
		}
		{
			final Params params = new MyParams("look", "cnav");
			assertThat(ControllersMiscUtils.getLook(params)).isEqualTo(Look.CNAV);
		}
		{
			final Params params = new MyParams("look", "msa");
			assertThat(ControllersMiscUtils.getLook(params)).isEqualTo(Look.MSA);
		}
		{
			final Params params = new MyParams("look", "rsi");
			assertThat(ControllersMiscUtils.getLook(params)).isEqualTo(Look.RSI);
		}
		{
			final Params params = new MyParams("look", "xxx");
			assertThat(ControllersMiscUtils.getLook(params)).isEqualTo(Look.GENERIC);
		}
	}

	@Test
	public void test_computeActionQueryParams() {
		assertThat(ControllersMiscUtils.computeActionQueryParams(false, false, Look.GENERIC)).isEqualTo("");
		assertThat(ControllersMiscUtils.computeActionQueryParams(true, false, Look.GENERIC)).isEqualTo("?test");
		assertThat(ControllersMiscUtils.computeActionQueryParams(false, true, Look.GENERIC)).isEqualTo("?debug");
		assertThat(ControllersMiscUtils.computeActionQueryParams(false, false, Look.CNAV)).isEqualTo("?look=cnav");
		assertThat(ControllersMiscUtils.computeActionQueryParams(true, true, Look.RSI)).isEqualTo("?test&debug&look=rsi");
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
