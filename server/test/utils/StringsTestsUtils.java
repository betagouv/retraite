package utils;

import static org.fest.assertions.Assertions.assertThat;

public class StringsTestsUtils {

	public static MyStringMatchers assertThatString(final String str) {
		return new MyStringMatchers(str);
	}

	public static final class MyStringMatchers {

		private String str;

		public MyStringMatchers(final String str) {
			this.str = str;
		}

		public MyStringMatchers contains(final String substr) {
			final int i = str.indexOf(substr);
			final String message = "String \"" + str + "\" does not contain \"" + substr + "\"";
			assertThat(i).overridingErrorMessage(message).isGreaterThanOrEqualTo(0);
			str = str.substring(0, i) + str.substring(i + substr.length());
			return this;
		}

		public MyStringMatchers andContains(final String substr) {
			return contains(substr);
		}

		public void andFinallyContains(final String substr) {
			assertThat(str).isEqualTo(substr);
		}

	}

}
