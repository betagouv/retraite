package utils.engine.utils;

import static java.util.Calendar.YEAR;

import java.util.Date;
import java.util.GregorianCalendar;

public class DateProvider {

	public int getCurrentYear() {
		return new GregorianCalendar().get(YEAR);
	}

	public Date getCurrentDate() {
		return new Date();
	}

}
