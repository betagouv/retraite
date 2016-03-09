package utils.engine;

import java.util.Date;
import java.util.GregorianCalendar;

import utils.engine.utils.DateProvider;

public class DateProviderFake extends DateProvider {

	private int currentYear;
	private Date currentDate;

	public DateProviderFake(final int dayOfMonth, final int month, final int year) {
		setCurrentDate(dayOfMonth, month, year);
	}

	public void setCurrentDate(final int dayOfMonth, final int month, final int year) {
		this.currentYear = year;
		this.currentDate = new GregorianCalendar(year, month - 1, dayOfMonth).getTime();
	}

	@Override
	public Date getCurrentDate() {
		return currentDate;
	}

	@Override
	public int getCurrentYear() {
		return currentYear;
	}

}
