package utils.engine.utils;

import static java.lang.Integer.parseInt;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Collections.sort;
import static utils.DateUtils.date;
import static utils.DateUtils.format;
import static utils.DateUtils.timestamp;

import java.util.GregorianCalendar;
import java.util.List;

import models.AnneesEtMois;
import models.PeriodeDepartLegal;
import utils.dao.PeriodeDepartLegalDao;

public class AgeLegalEvaluator {

	private final PeriodeDepartLegalDao periodeDepartLegalDao;

	public AgeLegalEvaluator(final PeriodeDepartLegalDao periodeDepartLegalDao) {
		this.periodeDepartLegalDao = periodeDepartLegalDao;
	}

	public PeriodeDepartLegal findPeriodeDepartLegal(final String dateNaissance) {
		final List<PeriodeDepartLegal> list = periodeDepartLegalDao.findAll();
		sort(list, new PeriodeDepartLegalComparator());
		for (final PeriodeDepartLegal periodeDepartLegal : list) {
			if (isDefined(periodeDepartLegal.dateNaissanceDebut) && isBornAfter(dateNaissance, periodeDepartLegal)) {
				return periodeDepartLegal;
			}
		}
		return last(list);
	}

	public boolean isAgeLegal(final String dateNaissance, final String departMois, final String departAnnee) {
		final GregorianCalendar calDepartLegal = createCalendarPourDepartLegal(dateNaissance);
		final GregorianCalendar calDepartSouhaite = createCalendar(departMois, departAnnee);
		return calDepartSouhaite.getTimeInMillis() > calDepartLegal.getTimeInMillis();
	}

	public AnneesEtMois getAgeLegalPourPartir(final String dateNaissance) {
		return findPeriodeDepartLegal(dateNaissance).ageDepartLegal;
	}

	public AnneesEtMois calculeAgeADateDonnee(final String dateNaissance, final String departMois, final String departAnnee) {
		final GregorianCalendar calNaissance = createCalendar(dateNaissance);
		final GregorianCalendar calDepartSouhaite = createCalendar(departMois, departAnnee);
		return getDiffInYearsAndMonths(calNaissance, calDepartSouhaite);
	}

	public String calculeDateEnAjoutant(final String dateNaissance, final AnneesEtMois age) {
		final GregorianCalendar calNaissance = createCalendar(dateNaissance);
		calNaissance.add(YEAR, age.annees);
		calNaissance.add(MONTH, age.mois);
		if (calNaissance.get(DAY_OF_MONTH) > 1) {
			calNaissance.set(DAY_OF_MONTH, 1);
			calNaissance.set(MONTH, calNaissance.get(MONTH) + 1);
		}
		return format(calNaissance.getTime());
	}

	// Méthodes privées

	private AnneesEtMois getDiffInYearsAndMonths(final GregorianCalendar calNaissance, final GregorianCalendar calDepartSouhaite) {
		int diffYear = diffYear(calNaissance, calDepartSouhaite);
		int diffMonth = diffMonth(calNaissance, calDepartSouhaite);
		final int diffDays = diffDays(calNaissance, calDepartSouhaite);
		if (diffDays < 0) {
			diffMonth--;
		}
		if (diffMonth < 0) {
			diffYear--;
			diffMonth = 12 + diffMonth;
		}
		return new AnneesEtMois(diffYear, diffMonth);
	}

	private int diffDays(final GregorianCalendar calNaissance, final GregorianCalendar calDepartSouhaite) {
		return diff(calNaissance, calDepartSouhaite, DAY_OF_MONTH);
	}

	private int diffMonth(final GregorianCalendar calNaissance, final GregorianCalendar calDepartSouhaite) {
		return diff(calNaissance, calDepartSouhaite, MONTH);
	}

	private int diffYear(final GregorianCalendar calNaissance, final GregorianCalendar calDepartSouhaite) {
		return diff(calNaissance, calDepartSouhaite, YEAR);
	}

	private int diff(final GregorianCalendar calNaissance, final GregorianCalendar calDepartSouhaite, final int field) {
		return calDepartSouhaite.get(field) - calNaissance.get(field);
	}

	private GregorianCalendar createCalendarPourDepartLegal(final String dateNaissance) {
		final GregorianCalendar calDepartLegal = createCalendar(dateNaissance);
		final PeriodeDepartLegal periodeDepartLegal = findPeriodeDepartLegal(dateNaissance);
		calDepartLegal.add(YEAR, periodeDepartLegal.ageDepartLegal.annees);
		calDepartLegal.add(MONTH, periodeDepartLegal.ageDepartLegal.mois);
		return calDepartLegal;
	}

	private GregorianCalendar createCalendar(final String dateNaissance) {
		final GregorianCalendar calDepartLegal = new GregorianCalendar();
		calDepartLegal.setTime(date(dateNaissance));
		return calDepartLegal;
	}

	private GregorianCalendar createCalendar(final String departMois, final String departAnnee) {
		return new GregorianCalendar(parseInt(departAnnee), parseInt(departMois) - 1, 1);
	}

	private static boolean isDefined(final String str) {
		return str != null && !str.isEmpty();
	}

	private boolean isBornAfter(final String dateNaissance, final PeriodeDepartLegal periodeDepartLegal) {
		final long naissance = timestamp(dateNaissance);
		final long debut = timestamp(periodeDepartLegal.dateNaissanceDebut);
		return naissance >= debut;
	}

	private PeriodeDepartLegal last(final List<PeriodeDepartLegal> list) {
		return list.get(list.size() - 1);
	}

}
