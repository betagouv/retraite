package utils.dao;

import static utils.JsonUtils.fromJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Caisse;
import models.CaisseDepartementale;
import utils.RetraiteException;
import utils.data.CaisseForEdition;
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

	public List<CaisseForEdition> findCaissesListForEdition(final ChecklistName checklistName) {
		final List<CaisseDepartementale> list = CaisseDepartementale.find("byChecklistName", checklistName).fetch();
		final List<CaisseForEdition> caissesForEdition = new ArrayList<>();
		final Map<Long, CaisseForEdition> mapCaisseIdToCaisseForEdition = new HashMap<>();
		for (final CaisseDepartementale caisseDepartementale : list) {
			CaisseForEdition caisseForEdition = mapCaisseIdToCaisseForEdition.get(caisseDepartementale.caisse.id);
			if (caisseForEdition == null) {
				caisseForEdition = createCaisseForEdition(caisseDepartementale);
				mapCaisseIdToCaisseForEdition.put(caisseDepartementale.caisse.id, caisseForEdition);
				caissesForEdition.add(caisseForEdition);
			}
			if (caisseForEdition.departements == null) {
				caisseForEdition.departements = new ArrayList<>();
			}
			caisseForEdition.departements.add(caisseDepartementale.departement);
		}
		return caissesForEdition;
	}

	public Caisse update(final String jsonstring) {
		final Caisse caisseToUpdate = fromJson(jsonstring, Caisse.class);
		final Caisse caisseMerged = caisseToUpdate.merge();
		final Caisse caisseSaved = caisseMerged.save();
		return caisseSaved;
	}

	private CaisseForEdition createCaisseForEdition(final CaisseDepartementale caisseDepartementale) {
		final CaisseForEdition caisseForEdition = new CaisseForEdition();
		caisseForEdition.nom = caisseDepartementale.caisse.nom;
		caisseForEdition.adresse1 = caisseDepartementale.caisse.adresse1;
		caisseForEdition.adresse2 = caisseDepartementale.caisse.adresse2;
		caisseForEdition.adresse3 = caisseDepartementale.caisse.adresse3;
		caisseForEdition.adresse4 = caisseDepartementale.caisse.adresse4;
		caisseForEdition.telephone = caisseDepartementale.caisse.telephone;
		caisseForEdition.fax = caisseDepartementale.caisse.fax;
		caisseForEdition.site = caisseDepartementale.caisse.site;
		return caisseForEdition;
	}
}
