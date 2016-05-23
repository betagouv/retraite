package controllers.utils;

import play.mvc.Scope.Params;

public class ControllersMiscUtils {

	public static String getLook(final Params params) {
		final String lookFromParams = params.get("look");
		if (lookFromParams != null) {
			switch (lookFromParams.toLowerCase()) {
			case "cnav":
				return "cnav";
			case "msa":
				return "msa";
			case "rsi":
				return "rsi";
			}
		}
		return "style";
	}

	public static String computeActionQueryParams(final boolean isTest, final String look) {
		String queryParams = "";
		if (isTest) {
			queryParams = "?test";
		}
		if (!look.equals("style")) {
			if (queryParams.isEmpty()) {
				queryParams += "?";
			} else {
				queryParams += "&";
			}
			queryParams += "look=" + look;
		}
		return queryParams;
	}

}
