package controllers;

import java.util.HashMap;
import java.util.Map;

import models.CaisseDepartementale;
import utils.dao.CaisseDepartementaleDao;
import utils.engine.data.enums.ChecklistName;

public class WsCaisseDepartement extends SecuredRetraiteController {

	public static void deleteDepartement(final Long caisseId, final String departement) {
		new CaisseDepartementaleDao().deleteDepartement(caisseId, departement);
	}

	public static void addDepartement(final ChecklistName checklistName, final Long caisseId, final String departement) {
		new CaisseDepartementaleDao().addDepartement(checklistName, caisseId, departement);
	}

	public static void addCaisse(final ChecklistName checklistName, final String departement) {
		final CaisseDepartementale caisseDepartementale = new CaisseDepartementaleDao().addCaisse(checklistName, departement);
		final Map<String, Object> result = new HashMap<String, Object>() {
			{
				put("caisseId", caisseDepartementale.caisse.id);
			}
		};
		renderJSON(result);
	}
}
