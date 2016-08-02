package controllers;

import utils.dao.CaisseDepartementaleDao;

public class WsCaisseDepartement extends SecuredRetraiteController {

	public static void deleteDepartement(final Long caisseId, final String departement) {
		new CaisseDepartementaleDao().deleteDepartement(caisseId, departement);
	}
}
