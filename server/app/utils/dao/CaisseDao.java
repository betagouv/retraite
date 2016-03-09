package utils.dao;

import java.util.List;

import models.Caisse;
import models.CaisseDepartementale;
import utils.RetraiteException;
import utils.engine.data.enums.ChecklistName;

public class CaisseDao {

	public Caisse find(final ChecklistName checklistName, final String departement) {
		final List<CaisseDepartementale> list = CaisseDepartementale.find("byChecklistNameAndDepartement", checklistName, departement).fetch();
		if (list.size() > 1) {
			throw new RetraiteException("Erreur : trouvé " + list.size() + " caisses pour " + checklistName + " et departement " + departement);
		}
		return list.size() == 0 ? null : list.get(0).caisse;
	}

}
