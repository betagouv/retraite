package controllers;

import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.Http.Header;
import utils.JsonUtils;

public class RetraiteController extends Controller {

	private static boolean withoutGoogleAnalytics;

	@Before
	public static void setGacodeAndAppid() {
		if (!withoutGoogleAnalytics) {
			renderArgs.put("gacode", Play.configuration.getProperty("gacode"));
		}
		renderArgs.put("appid", Play.configuration.getProperty("appid"));
	}

	@Before
	public static void setSecuredRequestIfNecessary() {
		Logger.info("request = " + JsonUtils.toJson(request));
		final Header xForwardedProtoHeader = request.headers.get("x-forwarded-proto");
		Logger.info("xForwardedProtoHeader = " + JsonUtils.toJson(xForwardedProtoHeader));
		if (xForwardedProtoHeader != null && "https".equals(xForwardedProtoHeader.value())) {
			request.secure = true;
			request.port = 443;
		}
	}

	protected static void renderJSON(final Object o) {
		Controller.renderJSON(JsonUtils.toJson(o));
	}

	protected static void withoutGoogleAnalytics() {
		withoutGoogleAnalytics = true;
		renderArgs.put("gacode", null);
	}

}
