package controllers;

import play.Logger;
import utils.WsUtils;
import utils.wsinforetraite.InfoRetraiteConnector;
import utils.wsinforetraite.InfoRetraiteDecoder;
import utils.wsinforetraite.InfoRetraiteResult;
import utils.wsinforetraite.InfoRetraiteTokenRecuperator;
import utils.wsinforetraite.InfoRetraiteWsUr;

public class ApiInfoRetraite extends RetraiteController {

	public static void get(final String name, final String nir, final String dateNaissance) {
		Logger.debug("ApiInfoRetraite.get(" + name + "," + nir + "," + dateNaissance + ")...");
		final WsUtils wsUtils = new WsUtils();
		final InfoRetraiteWsUr infoRetraiteWsUr = new InfoRetraiteWsUr(
				new InfoRetraiteDecoder(),
				new InfoRetraiteConnector(wsUtils,
						new InfoRetraiteTokenRecuperator(wsUtils)));
		final InfoRetraiteResult result = infoRetraiteWsUr.retrieveAllInformations(name, nir, dateNaissance);
		renderJSON(result);
	}

}
