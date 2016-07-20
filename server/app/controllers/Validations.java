package controllers;

import static java.util.Calendar.MONTH;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import controllers.utils.Look;
import models.Caisse;
import models.PeriodeDepartLegal;
import utils.DateUtils;
import utils.dao.CaisseDao;
import utils.dao.PeriodeDepartLegalDao;
import utils.engine.data.enums.ChecklistName;
import utils.engine.utils.AgeLegalEvaluator;

public class Validations extends RetraiteController {

	public static void caisses() {
		final List<RenderDataParDepartement> data = new ArrayList<>();
		final List<RenderDepartement> departements = createListeDepartements();
		for (final RenderDepartement departement : departements) {
			final RenderDataParDepartement renderDataParDepartement = new RenderDataParDepartement(departement);
			data.add(renderDataParDepartement);
			for (final ChecklistName checklistName : ChecklistName.values()) {
				final Caisse caisse = new CaisseDao().find(checklistName, departement.numero);
				renderDataParDepartement.set(checklistName, caisse);
			}
		}
		final Look look = Look.GENERIC;
		render(data, look);
	}

	public static void testsAgeDepart() {
		final List<PeriodeDepartLegal> listPeriodesDepartLegal = PeriodeDepartLegal.findAll();
		final List<TestAgeDepart> listTestsAgeDepart = createTestsData();
		render(listPeriodesDepartLegal, listTestsAgeDepart);
	}

	// Méthodes privées

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
}
