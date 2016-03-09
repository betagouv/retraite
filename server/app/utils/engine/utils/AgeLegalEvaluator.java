package utils.engine.utils;

import static java.lang.Integer.parseInt;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Collections.sort;
import static utils.DateUtils.date;
import static utils.DateUtils.timestamp;

import java.util.GregorianCalendar;
import java.util.List;

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
		final GregorianCalendar gregorianCalendarDepartLegal = createCalendarPourDepartLegal(dateNaissance);
		final GregorianCalendar gregorianCalendarDepartSouhaite = createGregorianCalendar(departMois, departAnnee);
		return gregorianCalendarDepartSouhaite.getTimeInMillis() > gregorianCalendarDepartLegal.getTimeInMillis();
	}

	// Méthodes privées

	private GregorianCalendar createCalendarPourDepartLegal(final String dateNaissance) {
		final GregorianCalendar gregorianCalendarDepartLegal = new GregorianCalendar();
		gregorianCalendarDepartLegal.setTime(date(dateNaissance));
		final PeriodeDepartLegal periodeDepartLegal = findPeriodeDepartLegal(dateNaissance);
		gregorianCalendarDepartLegal.add(YEAR, periodeDepartLegal.ageDepartLegal.annees);
		gregorianCalendarDepartLegal.add(MONTH, periodeDepartLegal.ageDepartLegal.mois);
		return gregorianCalendarDepartLegal;
	}

	private GregorianCalendar createGregorianCalendar(final String departMois, final String departAnnee) {
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
