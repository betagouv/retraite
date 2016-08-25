package controllers;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
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

	protected static void renderJSON(final Object o) {
		Controller.renderJSON(JsonUtils.toJson(o));
	}

	protected static void withoutGoogleAnalytics() {
		withoutGoogleAnalytics = true;
		renderArgs.put("gacode", null);
	}

}
