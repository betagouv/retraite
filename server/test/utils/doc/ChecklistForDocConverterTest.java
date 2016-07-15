package utils.doc;

import static models.Delai.Unite.ANNEES;
import static models.Delai.Unite.MOIS;
import static org.fest.assertions.Assertions.assertThat;
import static utils.TestsUtilsCondition.createConditionDelai;
import static utils.TestsUtilsCondition.createConditionRegimeDetecte;
import static utils.TestsUtilsCondition.createConditionStatut;
import static utils.TestsUtilsDelai.createDelaiAuPlus;
import static utils.TestsUtilsDelai.createDelaiDesQuePossible;
import static utils.TestsUtilsDelai.createDelaiEntre;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import models.Chapitre;
import models.Checklist;
import models.Condition;
import models.Delai;

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
		createAndAddChapitre(checklist, "chap1")
				.setDelai(createDelaiDesQuePossible())
				.addCondition(createConditionDelai("PLUS", "3", "ANNEES"))
				.addCondition(createConditionStatut("nsa"));
		createAndAddChapitre(checklist, "chap2")
				.setDelai(createDelaiAuPlus(4, MOIS))
				.addCondition(createConditionRegimeDetecte("agirc-arrco"))
				.addCondition(createConditionStatut("sa"));
		final Chapitre chapitre = createAndAddChapitre(checklist, "chap3")
				.setDelai(createDelaiEntre(7, 8, ANNEES))
				.addCondition(createConditionRegimeDetecte("regimes-base-hors-alignés"))
				.addCondition(createConditionStatut("sa"))
				.getChapitre();
		chapitre.texteActions = "<p></p>";
		chapitre.texteModalites = "<br/>";
		chapitre.texteInfos = "<p></p>";
		createAndAddChapitre(checklist, "chap4 sans delai");

		// Test
		final ChecklistForDoc checklistForDoc = converter.convert(checklist);

		assertThat(checklistForDoc.nom).isEqualTo("Ma checklist");
		assertThat(checklistForDoc.chapitres).hasSize(4);

		{
			final ChapitreForDoc chapitre1 = checklistForDoc.chapitres.get(0);
			assertThat(chapitre1.titre).isEqualTo("chap1");
			assertThat(chapitre1.delai).isEqualTo("Le plus tôt possible");
			assertThat(chapitre1.texteActions).isEqualTo("Actions chap1");
			assertThat(chapitre1.texteModalites).isEqualTo("Modalités chap1");
			assertThat(chapitre1.texteInfos).isEqualTo("Infos chap1");
			assertThat(chapitre1.notes).isEqualTo("Notes chap1");
			assertThat(chapitre1.conditions).contains(
					"Si le délai restant avant la date de départ prévue est supérieur à 3 années",
					"Si l’assuré appartient à la catégorie NSA");
		}
		{
			final ChapitreForDoc chapitre2 = checklistForDoc.chapitres.get(1);
			assertThat(chapitre2.titre).isEqualTo("chap2");
			assertThat(chapitre2.delai).isEqualTo("Au plus tard 4 mois avant la date de départ prévue");
			assertThat(chapitre2.texteActions).isEqualTo("Actions chap2");
			assertThat(chapitre2.texteModalites).isEqualTo("Modalités chap2");
			assertThat(chapitre2.texteInfos).isEqualTo("Infos chap2");
			assertThat(chapitre2.notes).isEqualTo("Notes chap2");
			assertThat(chapitre2.conditions).contains(
					"Si régimes AGIRC-ARRCO détectés",
					"Si l’assuré appartient à la catégorie SA");
		}
		{
			final ChapitreForDoc chapitre3 = checklistForDoc.chapitres.get(2);
			assertThat(chapitre3.titre).isEqualTo("chap3");
			assertThat(chapitre3.delai).isEqualTo("Entre 7 et 8 années avant la date de départ prévue");
			assertThat(chapitre3.texteActions).isNull(); // Texte vide --> null
			assertThat(chapitre3.texteModalites).isNull(); // Texte vide --> null
			assertThat(chapitre3.texteInfos).isNull(); // Texte vide --> null
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

	@Test
	public void should_generate_2_delais_msa() {

		final Checklist checklist = new Checklist();
		checklist.nom = "Ma checklist";
		checklist.type = "msa";
		checklist.chapitres = new ArrayList<Chapitre>();
		createAndAddChapitre(checklist, "chap1")
				.setDelai(createDelaiDesQuePossible())
				.setDelaiSA(createDelaiAuPlus(4, MOIS))
				.addCondition(createConditionDelai("PLUS", "3", "ANNEES"))
				.addCondition(createConditionStatut("nsa"));

		// Test
		final ChecklistForDoc checklistForDoc = converter.convert(checklist);
		assertThat(checklistForDoc.type).isEqualTo("msa");
		final ChapitreForDoc chapitre1 = checklistForDoc.chapitres.get(0);
		assertThat(chapitre1.delai).isEqualTo("Le plus tôt possible");
		assertThat(chapitre1.delaiSA).isEqualTo("Au plus tard 4 mois avant la date de départ prévue");
	}

	private ChapitreHelper createAndAddChapitre(final Checklist checklist, final String titre) {
		final Chapitre chapitre = new Chapitre();
		chapitre.titre = titre;
		chapitre.texteActions = "Actions " + titre;
		chapitre.texteModalites = "Modalités " + titre;
		chapitre.texteInfos = "Infos " + titre;
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

		public ChapitreHelper setDelaiSA(final Delai delaiSA) {
			chapitre.delaiSA = delaiSA;
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

		public Chapitre getChapitre() {
			return chapitre;
		}
	}

}
