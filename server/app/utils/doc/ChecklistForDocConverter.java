package utils.doc;

import java.util.ArrayList;

import models.Chapitre;
import models.Checklist;
import models.Condition;
import utils.TextUtils;

public class ChecklistForDocConverter {

	private final ConverterDelai converterDelai = new ConverterDelai();
	private final ConverterCondition converterCondition = new ConverterCondition();

	public ChecklistForDoc convert(final Checklist checklist) {
		final ChecklistForDoc checklistForDoc = new ChecklistForDoc();
		checklistForDoc.nom = checklist.nom;
		checklistForDoc.type = checklist.type;
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
		chapitreForDoc.delaiSA = converterDelai.convert(chapitre.delaiSA);
		chapitreForDoc.texteActions = nullIfEmpty(chapitre.texteActions);
		chapitreForDoc.texteModalites = nullIfEmpty(chapitre.texteModalites);
		chapitreForDoc.texteInfos = nullIfEmpty(chapitre.texteInfos);
		chapitreForDoc.notes = chapitre.notes;
		chapitreForDoc.conditions = new ArrayList<String>();
		if (chapitre.conditions != null) {
			for (final Condition condition : chapitre.conditions) {
				chapitreForDoc.conditions.add(converterCondition.convert(condition));
			}
		}
		return chapitreForDoc;
	}

	private String nullIfEmpty(final String text) {
		return TextUtils.isLikeEmpty(text) ? null : text;
	}

}
