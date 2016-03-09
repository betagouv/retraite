package utils.engine.intern;

import static java.lang.Integer.parseInt;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Locale.FRENCH;
import static models.Delai.Type.APARTIR;
import static models.Delai.Type.AUCUN;
import static models.Delai.Type.AUPLUS;
import static models.Delai.Type.ENTRE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import models.Delai;
import models.Delai.Unite;
import utils.engine.data.MonthAndYear;
import utils.engine.utils.DateProvider;

public class UserChecklistDelaiComputer {

	private static final SimpleDateFormat FORMAT_MONTH_NAME = new SimpleDateFormat("MMMM", FRENCH);
	private final DateProvider dateProvider;

	public UserChecklistDelaiComputer(final DateProvider dateProvider) {
		this.dateProvider = dateProvider;
	}

	public String compute(final Delai delai, final MonthAndYear dateDepart) {
		if (delai == null) {
			return "";
		}
		if (delai.type == AUCUN) {
			return "";
		}
		if (delai.type == AUPLUS) {
			return auPlus(delai, dateDepart);
		}
		if (delai.type == ENTRE) {
			return entre(delai, dateDepart);
		}
		if (delai.type == APARTIR) {
			return aPartir(delai, dateDepart);
		}
		return "Dès que possible";
	}

	private String auPlus(final Delai delai, final MonthAndYear dateDepart) {
		final GregorianCalendar calendar = createCalendarForDateDepart(dateDepart);
		setCalendarToEndOfPreviousMonth(calendar);
		calendar.add(getCalendarField(delai.unite), -delai.min);
		if (calendar.getTime().getTime() <= dateProvider.getCurrentDate().getTime()) {
			return "Dès que possible";
		}
		return "Au plus tard fin " + FORMAT_MONTH_NAME.format(calendar.getTime()) + " " + calendar.get(Calendar.YEAR);
	}

	private String entre(final Delai delai, final MonthAndYear dateDepart) {
		int delaiMin = -delai.min;
		int delaiMax = -delai.max;
		if (delaiMin < delaiMax) {
			delaiMin = -delai.max;
			delaiMax = -delai.min;
		}

		final GregorianCalendar calendarMin = createCalendarForDateDepart(dateDepart);
		setCalendarToEndOfPreviousMonth(calendarMin);
		calendarMin.add(getCalendarField(delai.unite), delaiMin);
		final GregorianCalendar calendarMax = createCalendarForDateDepart(dateDepart);
		calendarMax.add(getCalendarField(delai.unite), delaiMax);

		final int monthMin = calendarMin.get(Calendar.MONTH);
		final int monthMax = calendarMax.get(Calendar.MONTH);
		final int yearMin = calendarMin.get(YEAR);
		final int yearMax = calendarMax.get(YEAR);

		final String monthMaxStr = FORMAT_MONTH_NAME.format(calendarMax.getTime());
		final String monthMinStr = FORMAT_MONTH_NAME.format(calendarMin.getTime());
		final String yearMaxStr = yearMin == yearMax ? "" : " " + yearMax;
		if (calendarMin.getTime().getTime() <= dateProvider.getCurrentDate().getTime()) {
			return "Dès que possible";
		}
		if (calendarMax.getTime().getTime() <= dateProvider.getCurrentDate().getTime()) {
			return "Au plus tard fin " + monthMinStr + " " + yearMin;
		}

		if (sameMinAndMax(delai) || sameMonthAndSameYear(monthMin, monthMax, yearMin, yearMax)) {
			final String monthStr = FORMAT_MONTH_NAME.format(calendarMax.getTime());
			return "En " + monthStr + " " + yearMin;
		}

		return "De " + monthMaxStr + yearMaxStr + " à fin " + monthMinStr + " " + yearMin;
	}

	private String aPartir(final Delai delai, final MonthAndYear dateDepart) {
		final GregorianCalendar calendar = createCalendarForDateDepart(dateDepart);
		calendar.add(getCalendarField(delai.unite), -delai.min);
		if (calendar.getTime().getTime() <= dateProvider.getCurrentDate().getTime()) {
			return "Dès que possible";
		}
		return "A partir de " + FORMAT_MONTH_NAME.format(calendar.getTime()) + " " + calendar.get(Calendar.YEAR);
	}

	private boolean sameMinAndMax(final Delai delai) {
		return delai.min == delai.max;
	}

	private boolean sameMonthAndSameYear(final int monthMin, final int monthMax, final int yearMin, final int yearMax) {
		return monthMin == monthMax && yearMin == yearMax;
	}

	private GregorianCalendar createCalendarForDateDepart(final MonthAndYear dateDepart) {
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(parseInt(dateDepart.year), parseInt(dateDepart.month) - 1, 1);
		return calendar;
	}

	private void setCalendarToEndOfPreviousMonth(final GregorianCalendar calendar) {
		calendar.add(DAY_OF_MONTH, -1);
	}

	private int getCalendarField(final Unite unite) {
		return unite == Unite.ANNEES ? Calendar.YEAR : Calendar.MONTH;
	}

}
