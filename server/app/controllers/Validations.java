package controllers;

import static java.util.Calendar.MONTH;
import static utils.engine.data.enums.ChecklistName.CNAV;
import static utils.engine.data.enums.ChecklistName.MSA;
import static utils.engine.data.enums.ChecklistName.RSI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.utils.Look;
import models.Caisse;
import models.PeriodeDepartLegal;
import play.templates.Template;
import play.templates.TemplateLoader;
import utils.DateUtils;
import utils.dao.CaisseDao;
import utils.dao.PeriodeDepartLegalDao;
import utils.engine.data.enums.ChecklistName;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.utils.AgeLegalEvaluator;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;

public class Validations extends RetraiteController {

	public static void caisses() {

		final CaisseDao caisseDao = new CaisseDao();

		// Données générales
		final List<RenderDataParDepartement> data = new ArrayList<>();
		final List<RenderDepartement> departements = createListeDepartements();
		for (final RenderDepartement departement : departements) {
			final RenderDataParDepartement renderDataParDepartement = new RenderDataParDepartement(departement);
			data.add(renderDataParDepartement);
			for (final ChecklistName checklistName : ChecklistName.values()) {
				final Caisse caisse = caisseDao.find(checklistName, departement.numero);
				renderDataParDepartement.set(checklistName, caisse);
			}
		}

		// Lsite des caisses
		final List<Caisse> caissesCNAV = caisseDao.findCaissesList(CNAV);
		final List<Caisse> caissesMSA = caisseDao.findCaissesList(MSA);
		final List<Caisse> caissesRSI = caisseDao.findCaissesList(RSI);

		// Rendu
		final Look look = Look.GENERIC;
		render(data, look, caissesCNAV, caissesMSA, caissesRSI);
	}

	public static void testsAgeDepart() {
		final List<PeriodeDepartLegal> listPeriodesDepartLegal = PeriodeDepartLegal.findAll();
		final List<TestAgeDepart> listTestsAgeDepart = createTestsData();
		render(listPeriodesDepartLegal, listTestsAgeDepart);
	}

	public static void sorties() {

		final Look look = Look.GENERIC;
		final List<InfoRetraiteResultRegime> regimesInfos = Arrays.asList(
				createInfoRetraiteResultRegime(1), createInfoRetraiteResultRegime(2));
		final Map<String, Object> age = new HashMap<String, Object>() {
			{
				put("annees", 60);
				put("mois", 2);
			}
		};
		final Map<String, Object> ageLegalPourPartir = new HashMap<String, Object>() {
			{
				put("annees", 61);
				put("mois", 4);
			}
		};
		final Map<String, Object> extras = new HashMap<String, Object>() {
			{
				put("urlAgeDepart", RegimeAligne.CNAV.urlAgeDepart);
				put("urlDispositifDepartAvantAgeLegal", RegimeAligne.CNAV.urlDispositifDepartAvantAgeLegal);
				put("urlDroits", RegimeAligne.CNAV.urlDroits);
				put("urlSimulMontant", RegimeAligne.CNAV.urlSimulMontant);
				put("urlInfosDepartRetraite", RegimeAligne.CNAV.urlInfosDepartRetraite);
				put("urlInfosPenibilite", RegimeAligne.CNAV.urlInfosPenibilite);
				put("urlSimulMontant", RegimeAligne.CNAV.urlSimulMontant);
				put("age", age);
				put("ageLegalPourPartir", ageLegalPourPartir);
			}
		};
		final Map<String, Object> data = new HashMap<String, Object>() {
			{
				put("regimesInfos", regimesInfos);
				put("extras", extras);
				put("hidden_departMois", "4");
				put("hidden_departAnnee", "2020");
			}
		};
		final Map<String, Object> args = new HashMap<String, Object>() {
			{
				put("data", data);
				put("look", look);
				put("withoutPageLayout", true);
			}
		};

		String content = "";
		content += renderEcranSortie("Aucune régime de base aligné", "Application/steps/displaySortieAucunRegimeDeBaseAligne.html", args);
		content += renderEcranSortie("Date de départ inconnu", "Application/steps/displaySortieDepartInconnu.html", args);
		content += renderEcranSortie("Pénibilité", "Application/steps/displaySortiePenibilite.html", args);
		content += renderEcranSortie("Trop jeune", "Application/steps/displaySortieTropJeune.html", args);
		content += renderEcranSortie("Age < légal - Carrière longue", "Application/steps/displayQuestionCarriereLongue.html", args);

		render(content, look);
	}

	// Méthodes privées

	private static InfoRetraiteResultRegime createInfoRetraiteResultRegime(final int i) {
		final InfoRetraiteResultRegime infoRetraiteResultRegime = new InfoRetraiteResultRegime();
		infoRetraiteResultRegime.regime = "regime " + i;
		infoRetraiteResultRegime.nom = "nom " + i;
		infoRetraiteResultRegime.adresse = "adresse " + i;
		infoRetraiteResultRegime.tel1 = "tel " + i;
		infoRetraiteResultRegime.email1 = "email " + i;
		return infoRetraiteResultRegime;
	}

	private static List<RenderDepartement> createListeDepartements() {
		final ArrayList<RenderDepartement> departements = new ArrayList<>();
		for (int i = 1; i < 95; i++) {
			if (i == 20) {
				departements.add(new RenderDepartement("2A", "Corse-du-Sud"));
				departements.add(new RenderDepartement("2B", "Haute-Corse"));
			} else {
				final String departement = (i < 10 ? "0" : "") + String.valueOf(i);
				departements.add(new RenderDepartement(departement));
			}
		}
		departements.add(new RenderDepartement("971", "Guadeloupe"));
		departements.add(new RenderDepartement("972", "Martinique"));
		departements.add(new RenderDepartement("973", "Guyane"));
		departements.add(new RenderDepartement("974", "La Réunion"));
		departements.add(new RenderDepartement("976", "Mayotte"));
		return departements;
	}

	@SuppressWarnings("unused")
	private static class RenderDataParDepartement {

		private final RenderDepartement departement;
		private Caisse caisseCNAV;
		private Caisse caisseMSA;
		private Caisse caisseRSI;

		public RenderDataParDepartement(final RenderDepartement departement) {
			this.departement = departement;
		}

		public void set(final ChecklistName checklistName, final Caisse caisse) {
			switch (checklistName) {
			case CNAV:
				this.caisseCNAV = caisse;
				break;
			case MSA:
				this.caisseMSA = caisse;
				break;
			case RSI:
				this.caisseRSI = caisse;
				break;
			}
		}

	}

	@SuppressWarnings("unused")
	private static class RenderDepartement {

		private final String numero;
		private final String nom;

		public RenderDepartement(final String numero) {
			this.numero = numero;
			this.nom = numero;
		}

		public RenderDepartement(final String numero, final String nom) {
			this.numero = numero;
			this.nom = numero + " " + nom;
		}

	}

	private static List<TestAgeDepart> createTestsData() {
		final AgeLegalEvaluator ageLegalEvaluator = new AgeLegalEvaluator(new PeriodeDepartLegalDao());
		final List<TestAgeDepart> data = new ArrayList<>();
		final GregorianCalendar calendar = new GregorianCalendar(1948, 03, 21);
		for (int i = 0; i < 20; i++) {
			final String dateNaissance = DateUtils.format(calendar.getTime());
			final PeriodeDepartLegal periodeDepartLegal = ageLegalEvaluator.findPeriodeDepartLegal(dateNaissance);
			data.add(new TestAgeDepart(dateNaissance, periodeDepartLegal));
			calendar.add(MONTH, 6);
		}
		return data;
	}

	@SuppressWarnings("unused")
	private static class TestAgeDepart {

		private final String dateNaissance;
		private final PeriodeDepartLegal periodeDepartLegal;

		public TestAgeDepart(final String dateNaissance, final PeriodeDepartLegal periodeDepartLegal) {
			this.dateNaissance = dateNaissance;
			this.periodeDepartLegal = periodeDepartLegal;
		}

	}

	public static String renderEcranSortie(final String title, final String templateName, final Map<String, Object> args) {

		String content = "<h2>" + title + "</h2>";

		final Template template = TemplateLoader.load(template(templateName));
		content += template.render(args);

		return content;
	}

}
