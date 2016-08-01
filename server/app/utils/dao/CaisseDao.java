package utils.dao;

import static utils.JsonUtils.fromJson;

import java.util.ArrayList;
import java.util.List;

import models.Caisse;
import models.CaisseDepartementale;
import utils.RetraiteException;
import utils.engine.data.enums.ChecklistName;

public class CaisseDao {

	public Caisse findForDepartment(final ChecklistName checklistName, final String departement) {
		final List<CaisseDepartementale> list = CaisseDepartementale.find("byChecklistNameAndDepartement", checklistName, departement).fetch();
		if (list.size() > 1) {
			throw new RetraiteException("Erreur : trouvé " + list.size() + " caisses pour " + checklistName + " et departement " + departement);
		}
		return list.size() == 0 ? null : list.get(0).caisse;
	}

	public Caisse findWithId(final String id) {
		return Caisse.findById(Long.valueOf(id));
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

	public Caisse update(final String jsonstring) {
		final Caisse caisseToUpdate = fromJson(jsonstring, Caisse.class);
		final Caisse caisseMerged = caisseToUpdate.merge();
		final Caisse caisseSaved = caisseMerged.save();
		return caisseSaved;
	}

}
