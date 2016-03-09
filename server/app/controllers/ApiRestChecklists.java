package controllers;

import static java.util.Arrays.asList;
import static utils.dao.DaoChecklistFactory.createDaoChecklist;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import controllers.utils.ChecklistFilter;
import models.Checklist;
import play.Logger;

public class ApiRestChecklists extends SecuredRetraiteController {

	public static void getChecklists() {
		Logger.debug("getChecklists() for user '" + retrieveConnectedUser().login + "' ...");
		final List<Checklist> checklists = createDaoChecklist().findAll(false);
		if (checklists == null) {
			Logger.error("Error retrieving Checklists");
		}
		final List<Checklist> checklistsAuthorized = new ChecklistFilter().filter(checklists, retrieveConnectedUser().authorizedChecklists);
		renderJSON(checklistsAuthorized);
	}

	public static void getChecklist(final String name) {
		Logger.debug("getChecklist(" + name + ") for user '" + retrieveConnectedUser().login + "' ...");
		final Checklist checklist = createDaoChecklist().find(name, false);
		if (checklist == null) {
			Logger.error("Error retrieving Checklist with name=" + name);
			error("Error retrieving Checklist with name=" + name);
		}
		if (isNotAuthorizedForCurrentUser(checklist)) {
			error("Unauthorized to access this checklist");
		}
		renderJSON(checklist);
	}

	private static boolean isNotAuthorizedForCurrentUser(final Checklist checklist) {
		final List<String> authorizedChecklists = asList(StringUtils.split(retrieveConnectedUser().authorizedChecklists, ","));
		return !authorizedChecklists.contains(checklist.nom);
	}

	public static void updateChecklist() {
		final String jsonstring = params.get("body");
		Logger.debug("updateChecklist(" + jsonstring + ")...");
		final Checklist checklist = createDaoChecklist().update(jsonstring);
		renderJSON(checklist);
	}
}
