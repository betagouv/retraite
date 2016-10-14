package controllers;

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
	public static void setSecuredAndPortInRequestIfNecessary() {

		// On récupère l'entête perso 'x-forwarded-proto' s'il existe, et s'il vaut 'https'
		// on modifie les propriétés de 'request' pour que les redirections gérées par
		// Play! soient bien faites en HTTPS

		final Header xForwardedProtoHeader = request.headers.get("x-forwarded-proto");
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
