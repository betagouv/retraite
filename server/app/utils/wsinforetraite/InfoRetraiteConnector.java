package utils.wsinforetraite;

import static utils.WsUtils.cookie;
import static utils.WsUtils.param;
import static utils.wsinforetraite.InfoRetraiteConstants.BASE_API_URL;

import play.Logger;
import play.libs.WS.HttpResponse;
import utils.RetraiteException;
import utils.WsUtils;

public class InfoRetraiteConnector {

	// URL : "https://www.conseiller.info-retraite.fr/api/mesregimes?name=TOTO&nir=1223344555666&dtnai=07/04/2000";

	private final WsUtils wsUtils;
	private final InfoRetraiteTokenRecuperator infoRetraiteTokenRecuperator;

	public InfoRetraiteConnector(final WsUtils wsUtils, final InfoRetraiteTokenRecuperator infoRetraiteTokenRecuperator) {
		this.wsUtils = wsUtils;
		this.infoRetraiteTokenRecuperator = infoRetraiteTokenRecuperator;
	}

	public String get(final String name, final String nir, final String dateNaissance) {
		try {
			final InfoRetraiteTokens tokens = infoRetraiteTokenRecuperator.getToken();
			final HttpResponse response = wsUtils.doPost(
					BASE_API_URL,
					cookie(tokens.getCookie()),
					param("csrfToken", tokens.getCsrfToken()),
					param("name", name.trim()),
					param("nir", formatNir(nir)),
					param("dtnai", dateNaissance));
			if (isStatus4xx(response)) {
				// Pas de réponse trouvée avec les informations fournies
				return null;
			}
			if (response.getStatus() != 200) {
				throw new RetraiteException(
						"Error requesting WS conseiller.info-retraite.fr : code " + response.getStatus() + " = " + response.getStatusText());
			}
			final String result = response.getString();
			Logger.debug("Result from info retraite : " + result);
			return result;
		} catch (final RetraiteException e) {
			throw e;
		} catch (final Exception e) {
			throw new RetraiteException("Error requesting WS conseiller.info-retraite.fr", e);
		}
	}

	private boolean isStatus4xx(final HttpResponse response) {
		return response.getStatus() / 100 == 4;
	}

	private String formatNir(final String nir) {
		final String nirWithoutSpaces = nir.replace(" ", "");
		if (nirWithoutSpaces.length() > 13) {
			return nirWithoutSpaces.substring(0, 13);
		}
		return nirWithoutSpaces;
	}

}
