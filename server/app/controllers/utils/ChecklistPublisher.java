package controllers.utils;

import java.lang.reflect.Field;
import java.util.List;

import models.Checklist;
import play.db.jpa.Model;
import utils.JsonUtils;
import utils.RetraiteAlreayPublishedChecklistException;
import utils.RetraiteChecklistNotFound;
import utils.RetraiteException;
import utils.dao.DaoChecklist;

public class ChecklistPublisher {

	private final DaoChecklist daoChecklist;

	public ChecklistPublisher(final DaoChecklist daoChecklist) {
		this.daoChecklist = daoChecklist;
	}

	public long publish(final long checklistId) {

		final Checklist checklist = daoChecklist.find(checklistId);
		if (checklist == null) {
			throw new RetraiteException("Error publishing checklist with ID=" + checklistId + " : checklist not found !");
		}
		if (checklist.published) {
			throw new RetraiteAlreayPublishedChecklistException("Error, checklist ID=" + checklist.id + " nom=" + checklist.nom + " is alreadu published !");
		}

		checklist.modifiedButNotPublished = false;
		checklist.save();

		final Checklist checklistToPublish = prepareToPublish(checklist);
		deleteExistingPublishedChecklistIfExist(checklist.nom);
		final Checklist publishedChecklist = checklistToPublish.save();
		return publishedChecklist.getId();
	}

	// Méthodes privées

	private void deleteExistingPublishedChecklistIfExist(final String nom) {
		try {
			final Checklist oldPublishedChecklist = daoChecklist.find(nom, true);
			if (oldPublishedChecklist != null) {
				oldPublishedChecklist.delete();
			}
		} catch (final RetraiteChecklistNotFound e) {
		}
	}

	private Checklist prepareToPublish(final Checklist checklist) {
		final String jsonString = JsonUtils.toJson(checklist);
		final Checklist checklistNew = JsonUtils.fromJson(jsonString, Checklist.class);
		checklistNew.published = true;
		setAllIdsToNull(checklistNew);
		return checklistNew;
	}

	private void setAllIdsToNull(final Object obj) {
		if (!(obj instanceof Model)) {
			return;
		}
		try {
			final Field[] fields = obj.getClass().getFields();
			for (final Field field : fields) {
				final Object value = field.get(obj);
				if (value == null) {
					continue;
				}
				if (field.getName().equalsIgnoreCase("id")) {
					field.set(obj, null);
				} else if (field.getType().isAssignableFrom(List.class)) {
					final List list = (List) value;
					for (final Object subobj : list) {
						setAllIdsToNull(subobj);
					}
				} else if (field.getType().isArray()) {
					final Object[] list = (Object[]) value;
					for (final Object subobj : list) {
						setAllIdsToNull(subobj);
					}
				} else if (!field.getType().isPrimitive()) {
					setAllIdsToNull(value);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
