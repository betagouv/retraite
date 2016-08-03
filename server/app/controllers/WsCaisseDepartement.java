package controllers;

import utils.dao.CaisseDepartementaleDao;
import utils.engine.data.enums.ChecklistName;

public class WsCaisseDepartement extends SecuredRetraiteController {

	public static void deleteDepartement(final Long caisseId, final String departement) {
		new CaisseDepartementaleDao().deleteDepartement(caisseId, departement);
	}

	public static void addDepartement(final ChecklistName checklistName, final Long caisseId, final String departement) {
		new CaisseDepartementaleDao().addDepartement(checklistName, caisseId, departement);
	}
}
