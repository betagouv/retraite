package controllers;

import play.Logger;
import utils.WsUtils;
import utils.wsinforetraite.InfoRetraiteReal;
import utils.wsinforetraite.InfoRetraiteConnector;
import utils.wsinforetraite.InfoRetraiteDecoder;
import utils.wsinforetraite.InfoRetraiteResult;

public class ApiInfoRetraite extends RetraiteController {

	public static void get(final String name, final String nir, final String dateNaissance) {
		Logger.debug("ApiInfoRetraite.get(" + name + "," + nir + "," + dateNaissance + ")...");
		final InfoRetraiteResult result = new InfoRetraiteReal(new InfoRetraiteDecoder(), new InfoRetraiteConnector(new WsUtils())).retrieveAllInformations(name, nir, dateNaissance);
		renderJSON(result);
	}

}
