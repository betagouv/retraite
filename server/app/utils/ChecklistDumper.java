package utils;

import models.Chapitre;
import models.Checklist;
import models.Condition;

public class ChecklistDumper {

	public void dump(final Checklist checklist) {
		log("Checklist:");
		log(". nom : " + checklist.nom);
		log(". published : " + checklist.published);
		log(". type : " + checklist.type);
		log(". chapitres :");
		for (int i = 0; i < checklist.chapitres.size(); i++) {
			log("  " + i);
			final Chapitre chapitre = checklist.chapitres.get(i);
			log("    . titre : " + chapitre.titre);
			log("    . delai : " + chapitre.delai);
			log("    . delaiSA : " + chapitre.delaiSA);
			log("    . conditions :");
			for (int j = 0; j < chapitre.conditions.size(); j++) {
				final Condition condition = chapitre.conditions.get(i);
				log("        . " + condition);
			}
		}
	}

	private void log(final String string) {
		System.out.println(string);
	}

}
