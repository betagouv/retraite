package controllers;

import java.util.List;

import models.Caisse;
import play.Logger;
import utils.dao.CaisseDao;
import utils.engine.data.enums.ChecklistName;

public class ApiRestCaisse extends SecuredRetraiteController {

	public static void getAllForChecklistName(final String name) {
		final List<Caisse> caisses = new CaisseDao().findCaissesList(ChecklistName.valueOf(name));
		renderJSON(caisses);
	}

	public static void getAll(final String name) {
		throw new UnsupportedOperationException("Not implemeted yet, just do it ! ;-)");
	}

	public static void get(final String id) {
		final Caisse caisse = new CaisseDao().findWithId(id);
		renderJSON(caisse);
	}

	public static void saveCaisse() {
		final String jsonstring = params.get("body");
		Logger.debug("saveCaisse(" + jsonstring + ")...");
		final Caisse caisse = new CaisseDao().update(jsonstring);
		renderJSON(caisse);
	}
}
