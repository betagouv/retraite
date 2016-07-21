package utils.dao;

import java.util.ArrayList;
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

	public List<Caisse> findCaissesList(final ChecklistName checklistName) {
		final List<CaisseDepartementale> list = CaisseDepartementale.find("byChecklistName", checklistName).fetch();
		final List<Caisse> caisses = new ArrayList<>();
		for (final CaisseDepartementale caisseDepartementale : list) {
			final Caisse caisse = caisseDepartementale.caisse;
			if (!caisses.contains(caisse)) {
				caisses.add(caisse);
			}
		}
		return caisses;
	}

}
