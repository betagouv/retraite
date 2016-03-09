package controllers;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import utils.JsonUtils;

public class RetraiteController extends Controller {

	@Before
	public static void setGacode() {
		renderArgs.put("gacode", Play.configuration.getProperty("gacode"));
	}

	protected static void renderJSON(final Object o) {
		Controller.renderJSON(JsonUtils.toJson(o));
	}

}
