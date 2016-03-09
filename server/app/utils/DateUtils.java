package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyy");

	public static long timestamp(final String dateNaissance) {
		return date(dateNaissance).getTime();
	}

	public static Date date(final String dateNaissance) {
		try {
			return DATE_FORMAT.parse(dateNaissance);
		} catch (final ParseException e) {
			throw new RetraiteException("Erreur d'interprétation de la date '" + dateNaissance + "'");
		}
	}

	public static String format(final Date date) {
		return DATE_FORMAT.format(date);
	}

}
