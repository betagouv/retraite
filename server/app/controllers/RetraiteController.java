package controllers;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import utils.JsonUtils;

public class RetraiteController extends Controller {

	@Before
	public static void setGacodeAndAppid() {
		renderArgs.put("gacode", Play.configuration.getProperty("gacode"));
		renderArgs.put("appid", Play.configuration.getProperty("appid"));
	}

	protected static void renderJSON(final Object o) {
		Controller.renderJSON(JsonUtils.toJson(o));
	}

}
