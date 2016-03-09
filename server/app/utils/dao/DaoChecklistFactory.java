package utils.dao;

import utils.db.HtmlCleaner;

public class DaoChecklistFactory {

	public static DaoChecklist createDaoChecklist() {
		return new DaoChecklist(new HtmlCleaner());
	}
}
