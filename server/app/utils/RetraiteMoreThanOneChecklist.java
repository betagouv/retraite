package utils;

public class RetraiteMoreThanOneChecklist extends RetraiteException {

	public RetraiteMoreThanOneChecklist(final String message) {
		super(message);
	}

	public RetraiteMoreThanOneChecklist(final String message, final Throwable cause) {
		super(message, cause);
	}

}
