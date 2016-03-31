package utils.wsinforetraite;

import play.libs.WS.HttpResponse;
import utils.RetraiteException;
import utils.WsUtils;

public class InfoRetraiteTokenRecuperator {

	private final WsUtils wsUtils;

	public InfoRetraiteTokenRecuperator(final WsUtils wsUtils) {
		this.wsUtils = wsUtils;
	}

	public InfoRetraiteTokens getToken() {
		final HttpResponse response = wsUtils.doGet(InfoRetraiteConstants.SITE_URL);
		if (response.getStatus() != 200) {
			throw new RetraiteException(
					"Error getting conseiller.info-retraite.fr home page : code " + response.getStatus() + " = " + response.getStatusText());
		}
		final String siteContent = response.getString();
		final String csrfToken = extractCsrfTokenValue(siteContent);
		final String cookie = response.getHeader("Set-Cookie");
		return new InfoRetraiteTokens(cookie, csrfToken);
	}

	private String extractCsrfTokenValue(final String content) {
		final int indexName = content.indexOf("csrfToken");
		final int indexValue = content.indexOf("value", indexName);
		final int indexBegin = content.indexOf("\"", indexValue) + 1;
		final int indexEnd = content.indexOf("\"", indexBegin);
		return content.substring(indexBegin, indexEnd);
	}

}
