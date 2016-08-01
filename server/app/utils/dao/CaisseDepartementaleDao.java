package utils.dao;

import java.util.List;

import models.CaisseDepartementale;
import utils.engine.data.enums.ChecklistName;

public class CaisseDepartementaleDao {

	public List<CaisseDepartementale> getAll(final ChecklistName checklistName) {
		return CaisseDepartementale.find("byChecklistName", checklistName).fetch();
	}

}
