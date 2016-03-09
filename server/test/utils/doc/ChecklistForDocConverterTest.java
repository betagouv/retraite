package utils.doc;

import static org.fest.assertions.Assertions.assertThat;
import static utils.TestsUtilsCondition.createConditionDelai;
import static utils.TestsUtilsCondition.createConditionRegimeDetecte;
import static utils.TestsUtilsCondition.createConditionStatut;
import static utils.TestsUtilsDelai.createDelaiAuPlus;
import static utils.TestsUtilsDelai.createDelaiDesQuePossible;
import static utils.TestsUtilsDelai.createDelaiEntre;

import java.util.ArrayList;

import models.Chapitre;
import models.Checklist;
import models.Condition;
import models.Delai;

import org.junit.Before;
import org.junit.Test;

public class ChecklistForDocConverterTest {

	private ChecklistForDocConverter converter;

	@Before
	public void setUp() throws Exception {
		converter = new ChecklistForDocConverter();
	}

	@Test
	public void should_generate() {

		final Checklist checklist = new Checklist();
		checklist.nom = "Ma checklist";
		checklist.chapitres = new ArrayList<Chapitre>();
		createAndAddChapitre(checklist, "chap1", false)
				.setDelai(createDelaiDesQuePossible())
				.addCondition(createConditionDelai("PLUS", "3", "ANNEES"))
				.addCondition(createConditionStatut("nsa"));
		createAndAddChapitre(checklist, "chap2", true)
				.setDelai(createDelaiAuPlus(4, Delai.Unite.MOIS))
				.addCondition(createConditionRegimeDetecte("agirc-arrco"))
				.addCondition(createConditionStatut("sa"));
		createAndAddChapitre(checklist, "chap3", false)
				.setDelai(createDelaiEntre(7, 8, Delai.Unite.ANNEES))
				.addCondition(createConditionRegimeDetecte("regimes-base-hors-alignés"))
				.addCondition(createConditionStatut("sa"));
		createAndAddChapitre(checklist, "chap4 sans delai", false);

		// Test
		final ChecklistForDoc checklistForDoc = converter.convert(checklist);

		assertThat(checklistForDoc.nom).isEqualTo("Ma checklist");
		assertThat(checklistForDoc.chapitres).hasSize(4);

		{
			final ChapitreForDoc chapitre1 = checklistForDoc.chapitres.get(0);
			assertThat(chapitre1.titre).isEqualTo("chap1");
			assertThat(chapitre1.delai).isEqualTo("Dès que possible");
			assertThat(chapitre1.texteIntro).isEqualTo("Intro chap1");
			assertThat(chapitre1.parcoursDematDifferent).isFalse();
			assertThat(chapitre1.parcours).isEqualTo("Parcours chap1");
			assertThat(chapitre1.parcoursDemat).isNull();
			assertThat(chapitre1.texteComplementaire).isEqualTo("Comp chap1");
			assertThat(chapitre1.notes).isEqualTo("Notes chap1");
			assertThat(chapitre1.conditions).contains(
					"Si le délai restant avant la date de départ prévue est supérieur à 3 années",
					"Si l’assuré appartient à la catégorie NSA");
		}
		{
			final ChapitreForDoc chapitre2 = checklistForDoc.chapitres.get(1);
			assertThat(chapitre2.titre).isEqualTo("chap2");
			assertThat(chapitre2.delai).isEqualTo("Au plus tard 4 mois avant la date de départ prévue");
			assertThat(chapitre2.texteIntro).isEqualTo("Intro chap2");
			assertThat(chapitre2.parcoursDematDifferent).isTrue();
			assertThat(chapitre2.parcours).isEqualTo("Parcours chap2");
			assertThat(chapitre2.parcoursDemat).isEqualTo("Parcours demat chap2");
			assertThat(chapitre2.texteComplementaire).isEqualTo("Comp chap2");
			assertThat(chapitre2.notes).isEqualTo("Notes chap2");
			assertThat(chapitre2.conditions).contains(
					"Si régimes AGIRC-ARRCO détectés",
					"Si l’assuré appartient à la catégorie SA");
		}
		{
			final ChapitreForDoc chapitre3 = checklistForDoc.chapitres.get(2);
			assertThat(chapitre3.titre).isEqualTo("chap3");
			assertThat(chapitre3.delai).isEqualTo("Entre 7 et 8 années avant la date de départ prévue");
			assertThat(chapitre3.conditions).contains(
					"Si régimes de base hors alignés détectés",
					"Si l’assuré appartient à la catégorie SA");
		}
		{
			final ChapitreForDoc chapitre4 = checklistForDoc.chapitres.get(3);
			assertThat(chapitre4.delai).isEqualTo("Non défini !");
			assertThat(chapitre4.conditions).contains();
		}
	}

	private ChapitreHelper createAndAddChapitre(final Checklist checklist, final String titre, final boolean parcoursDematDifferent) {
		final Chapitre chapitre = new Chapitre();
		chapitre.titre = titre;
		chapitre.texteIntro = "Intro " + titre;
		chapitre.parcoursDematDifferent = parcoursDematDifferent;
		chapitre.parcours = "Parcours " + titre;
		if (parcoursDematDifferent) {
			chapitre.parcoursDemat = "Parcours demat " + titre;
		}
		chapitre.texteComplementaire = "Comp " + titre;
		chapitre.notes = "Notes " + titre;
		chapitre.checklist = checklist;
		checklist.chapitres.add(chapitre);
		return new ChapitreHelper(chapitre);
	}

	private static class ChapitreHelper {

		private final Chapitre chapitre;

		public ChapitreHelper(final Chapitre chapitre) {
			this.chapitre = chapitre;
		}

		public ChapitreHelper setDelai(final Delai delai) {
			chapitre.delai = delai;
			return this;
		}

		public ChapitreHelper addCondition(final Condition condition) {
			if (chapitre.conditions == null) {
				chapitre.conditions = new ArrayList<Condition>();
			}
			chapitre.conditions.add(condition);
			condition.chapitre = chapitre;
			return this;
		}

	}

}
