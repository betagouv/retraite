package utils.doc;

import java.util.ArrayList;

import models.Chapitre;
import models.Checklist;
import models.Condition;

public class ChecklistForDocConverter {

	private final ConverterDelai converterDelai = new ConverterDelai();
	private final ConverterCondition converterConditionStatut = new ConverterCondition();

	public ChecklistForDoc convert(final Checklist checklist) {
		final ChecklistForDoc checklistForDoc = new ChecklistForDoc();
		checklistForDoc.nom = checklist.nom;
		checklistForDoc.chapitres = new ArrayList<ChapitreForDoc>();
		for (final Chapitre chapitre : checklist.chapitres) {
			checklistForDoc.chapitres.add(convert(chapitre));
		}
		return checklistForDoc;
	}

	private ChapitreForDoc convert(final Chapitre chapitre) {
		final ChapitreForDoc chapitreForDoc = new ChapitreForDoc();
		chapitreForDoc.titre = chapitre.titre;
		chapitreForDoc.delai = converterDelai.convert(chapitre.delai);
		chapitreForDoc.texteIntro = chapitre.texteIntro;
		chapitreForDoc.parcoursDematDifferent = chapitre.parcoursDematDifferent;
		chapitreForDoc.parcours = chapitre.parcours;
		chapitreForDoc.parcoursDemat = chapitre.parcoursDemat;
		chapitreForDoc.texteComplementaire = chapitre.texteComplementaire;
		chapitreForDoc.notes = chapitre.notes;
		chapitreForDoc.conditions = new ArrayList<String>();
		if (chapitre.conditions != null) {
			for (final Condition condition : chapitre.conditions) {
				chapitreForDoc.conditions.add(converterConditionStatut.convert(condition));
			}
		}
		return chapitreForDoc;
	}

}
