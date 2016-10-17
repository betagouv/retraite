package controllers;

import controllers.utils.ChecklistPublisher;
import play.Logger;
import utils.dao.DaoChecklist;
import utils.db.HtmlCleaner;

public class ApiChecklistPublish extends RetraiteController {

	public static void publish(final Long id) {
		Logger.debug("ApiChecklistPublish.publish(" + id + ")...");
		new ChecklistPublisher(new DaoChecklist(new HtmlCleaner())).publish(id);
		ok();
	}

}
