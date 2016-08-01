package controllers;

import java.util.List;

import models.Caisse;
import play.Logger;
import utils.dao.CaisseDao;
import utils.data.CaisseForEdition;
import utils.engine.data.enums.ChecklistName;

public class ApiRestCaisse extends SecuredRetraiteController {

	public static void getAllForChecklistName(final String name) {
		final List<CaisseForEdition> caissesForEdition = new CaisseDao().findCaissesListForEdition(ChecklistName.valueOf(name));
		renderJSON(caissesForEdition);
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
