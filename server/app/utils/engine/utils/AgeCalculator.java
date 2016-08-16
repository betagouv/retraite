package utils.engine.utils;

import org.joda.time.DateMidnight;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.Years;

import utils.RetraiteBadNaissanceFormatException;

public class AgeCalculator {

	private final DateProvider dateProvider;

	public AgeCalculator(final DateProvider dateProvider) {
		this.dateProvider = dateProvider;
	}

	public int getAge(final String dateNaissance) {
		final String[] parts = dateNaissance.split("/");
		if (parts.length != 3) {
			throw new RetraiteBadNaissanceFormatException("Mauvais format jj/mm/aaaa pour la date de naissance (" + dateNaissance + ")");
		}
		final int day = Integer.parseInt(parts[0]);
		final int month = Integer.parseInt(parts[1]);
		final int year = Integer.parseInt(parts[2]);
		if (year < 1916) {
			throw new RetraiteBadNaissanceFormatException("Mauvaise années pour la date de naissance (" + dateNaissance + ")");
		}
		final DateMidnight today = new DateMidnight(dateProvider.getCurrentDate());
		DateMidnight birthday;
		try {
			birthday = new DateMidnight(year, month, day);
		} catch (final IllegalFieldValueException e) {
			throw new RetraiteBadNaissanceFormatException("Mauvaise date de naissance (" + dateNaissance + ")", e);
		}
		return Years.yearsBetween(birthday, today).getYears();
	}

}
