package utils.engine.intern;

import java.util.Calendar;
import java.util.GregorianCalendar;

import play.Logger;
import utils.RetraiteException;
import utils.engine.data.ConditionDelai;
import utils.engine.data.MonthAndYear;
import utils.engine.utils.DateProvider;

public class UserChecklistConditionDelaiEvaluator {

	private final DateProvider dateProvider;

	public UserChecklistConditionDelaiEvaluator(final DateProvider dateProvider) {
		this.dateProvider = dateProvider;
	}

	public boolean isVerified(final ConditionDelai conditionDelai, final MonthAndYear dateDepart) {
		try {
			// Conversions
			final int yearDateDepart = parseInt(dateDepart.year, "année de départ");
			final int monthDateDepart = parseInt(dateDepart.month, "mois de départ") - 1;
			final int nombreInCondition = parseInt(conditionDelai.getNombre(), "nombre de la condition delai");
			// Vérifications
			final String unite = conditionDelai.getUnite();
			checkUnite(unite);
			final String plusOuMoins = conditionDelai.getPlusOuMoins();
			checkPlusOuMoins(plusOuMoins);
			// Date départ - delai
			final GregorianCalendar calendar = new GregorianCalendar(yearDateDepart, monthDateDepart, 1);
			calendar.add(unite.equalsIgnoreCase("MOIS") ? Calendar.MONTH : Calendar.YEAR, -nombreInCondition);
			final long limitTime = calendar.getTime().getTime();
			// Comparaison / date courante
			final long currentTime = dateProvider.getCurrentDate().getTime();
			return plusOuMoins.equalsIgnoreCase("MOINS") ? currentTime >= limitTime : currentTime < limitTime;
		} catch (final NumberFormatException e) {
			Logger.error(e, "Erreur lors de la vérification de la condition");
			return false;
		}
	}

	private int parseInt(final String nombre, final String info) {
		try {
			return Integer.parseInt(nombre);
		} catch (final NumberFormatException e) {
			throw new RetraiteException("Impossible de convertir en Integer le texte '" + nombre + "' pour '" + info + "'", e);
		}
	}

	private void checkUnite(final String unite) {
		if (unite == null || (!unite.equalsIgnoreCase("MOIS") && !unite.equalsIgnoreCase("ANNEES"))) {
			throw new RetraiteException("Unité inconnue : '" + unite + "'");
		}
	}

	private void checkPlusOuMoins(final String plusOuMoins) {
		if (plusOuMoins == null || (!plusOuMoins.equalsIgnoreCase("PLUS") && !plusOuMoins.equalsIgnoreCase("MOINS"))) {
			throw new RetraiteException("plusOuMoins inconnu : '" + plusOuMoins + "'");
		}
	}
}
