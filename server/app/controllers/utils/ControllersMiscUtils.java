package controllers.utils;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Scope.Params;

public class ControllersMiscUtils {

	public static Look getLook(final Params params) {
		final String lookFromParams = params.get("look");
		return Look.valueFrom(lookFromParams);
	}

	public static String computeActionQueryParams(final Boolean test, final Boolean debug, final Look look, final Boolean force67) {
		final List<KeyAndValue> params = new ArrayList<>();
		if (test != null && test) {
			params.add(new KeyAndValue("test"));
		}
		if (debug != null && debug) {
			params.add(new KeyAndValue("debug"));
		}
		if (look != null && look.isNotGeneric()) {
			params.add(new KeyAndValue("look", look.toString().toLowerCase()));
		}
		if (force67 != null && force67) {
			params.add(new KeyAndValue("force67"));
		}
		String queryParams = "";
		for (int i = 0; i < params.size(); i++) {
			if (i == 0) {
				queryParams += "?";
			} else {
				queryParams += "&";
			}
			queryParams += params.get(i).formatForQuery();
		}
		return queryParams;
	}

	private static class KeyAndValue {

		private final String key;
		private final String value;

		public KeyAndValue(final String key) {
			this(key, null);
		}

		public String formatForQuery() {
			if (value == null) {
				return key;
			}
			return key + "=" + value;
		}

		public KeyAndValue(final String key, final String value) {
			this.key = key;
			this.value = value;
		}

	}

}
