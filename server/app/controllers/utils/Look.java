package controllers.utils;

public enum Look {

	GENERIC("style.css"), CNAV("cnav.css"), MSA("msa.css"), RSI("rsi.css");

	private final String cssFilename;

	public static Look valueFrom(final String look) {
		try {
			return valueOf(look.toUpperCase());
		} catch (final Exception e) {
			return GENERIC;
		}
	}

	private Look(final String cssFilename) {
		this.cssFilename = cssFilename;

	}

	public boolean isNotGeneric() {
		return this != GENERIC;
	}

	public String getCssFilename() {
		return cssFilename;
	}

}
