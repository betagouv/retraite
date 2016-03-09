package utils.dao;

import static utils.JsonUtils.fromJson;

import java.util.ArrayList;
import java.util.List;

import models.Chapitre;
import models.Checklist;
import utils.RetraiteChecklistNotFound;
import utils.RetraiteMoreThanOneChecklist;
import utils.db.HtmlCleaner;

public class DaoChecklist {

	private final HtmlCleaner htmlCleaner;

	public DaoChecklist(final HtmlCleaner htmlCleaner) {
		this.htmlCleaner = htmlCleaner;
	}

	public List<Checklist> findAll(final boolean published) {
		return Checklist.find("byPublished", published).fetch();
	}

	public Checklist findById(final long id) {
		return Checklist.findById(id);
	}

	public Checklist find(final String name, final boolean published) {
		final List<Checklist> checklists = Checklist.find("byNomAndPublished", name, published).fetch();
		if (checklists == null || checklists.size() == 0) {
			throw new RetraiteChecklistNotFound(
					"Situation anormale : impossible de trouver la checklist '" + name + "-" + (published ? "published" : "not-published") + "'");
		}
		if (checklists.size() > 1) {
			throw new RetraiteMoreThanOneChecklist(
					"Situation anormale : " + checklists.size() + " checklists trouvées pour '" + name + "-" + (published ? "published" : "not-published")
							+ "'");
		}
		return checklists.get(0);
	}

	public Checklist find(final long id) {
		final Checklist checklist = Checklist.findById(id);
		if (checklist == null) {
			throw new RetraiteChecklistNotFound(
					"Situation anormale : impossible de trouver la checklist '" + id + "'");
		}
		return checklist;
	}

	public Checklist update(final String jsonstring) {
		final Checklist checklistToUpdate = updateHtml(fromJson(jsonstring, Checklist.class));
		addEmptyConditionsListIfNull(checklistToUpdate);
		checklistToUpdate.modifiedButNotPublished = true;
		final Checklist checklistMerged = checklistToUpdate.merge();
		final Checklist checklistSaved = checklistMerged.save();
		return checklistSaved;
	}

	// Méthodes privées

	private void addEmptyConditionsListIfNull(final Checklist checklist) {
		for (final Chapitre chapitre : checklist.chapitres) {
			if (chapitre.conditions == null) {
				chapitre.conditions = new ArrayList<>();
			}
		}
	}

	private Checklist updateHtml(final Checklist checklist) {
		for (final Chapitre chapitre : checklist.chapitres) {
			updateHtml(chapitre);
		}
		return checklist;
	}

	private void updateHtml(final Chapitre chapitre) {
		chapitre.texteIntro = htmlCleaner.clean(chapitre.texteIntro);
		chapitre.parcours = htmlCleaner.clean(chapitre.parcours);
		chapitre.parcoursDemat = htmlCleaner.clean(chapitre.parcoursDemat);
		chapitre.texteComplementaire = htmlCleaner.clean(chapitre.texteComplementaire);
	}

}
