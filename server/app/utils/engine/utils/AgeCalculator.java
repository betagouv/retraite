package utils.engine.utils;

import org.joda.time.DateMidnight;
import org.joda.time.Years;

public class AgeCalculator {

	private final DateProvider dateProvider;

	public AgeCalculator(final DateProvider dateProvider) {
		this.dateProvider = dateProvider;
	}

	public int getAge(final String dateNaissance) {
		final String[] parts = dateNaissance.split("/");
		final int day = Integer.parseInt(parts[0]);
		final int month = Integer.parseInt(parts[1]);
		final int year = Integer.parseInt(parts[2]);
		final DateMidnight today = new DateMidnight(dateProvider.getCurrentDate());
		final DateMidnight birthday = new DateMidnight(year, month, day);
		return Years.yearsBetween(birthday, today).getYears();
	}

}
