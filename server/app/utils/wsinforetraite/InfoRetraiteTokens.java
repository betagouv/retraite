package utils.wsinforetraite;

public class InfoRetraiteTokens {

	private final String cookie;
	private final String csrfToken;

	public InfoRetraiteTokens(final String cookie, final String csrfToken) {
		this.cookie = cookie;
		this.csrfToken = csrfToken;
	}

	public String getCookie() {
		return cookie;
	}

	public String getCsrfToken() {
		return csrfToken;
	}

}
