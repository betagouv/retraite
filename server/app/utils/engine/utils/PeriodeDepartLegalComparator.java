package utils.engine.utils;

import static utils.DateUtils.date;

import java.util.Comparator;
import java.util.Date;

import models.PeriodeDepartLegal;

final class PeriodeDepartLegalComparator implements Comparator<PeriodeDepartLegal> {

	@Override
	public int compare(final PeriodeDepartLegal p1, final PeriodeDepartLegal p2) {
		if (!isDefined(p1.dateNaissanceDebut)) {
			return 1;
		}
		if (!isDefined(p2.dateNaissanceDebut)) {
			return -1;
		}
		final Date date1 = date(p1.dateNaissanceDebut);
		final Date date2 = date(p2.dateNaissanceDebut);
		return -date1.compareTo(date2);
	}

	private static boolean isDefined(final String str) {
		return str != null && !str.isEmpty();
	}

}