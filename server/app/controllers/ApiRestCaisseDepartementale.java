package controllers;

import java.util.List;

import models.CaisseDepartementale;
import utils.dao.CaisseDepartementaleDao;
import utils.engine.data.enums.ChecklistName;

public class ApiRestCaisseDepartementale extends SecuredRetraiteController {

	public static void getAll(final String name) {
		final List<CaisseDepartementale> caissesDepartementales = new CaisseDepartementaleDao().getAll(ChecklistName.valueOf(name));
		renderJSON(caissesDepartementales);
	}

}
