package utils.wsinforetraite;

import static utils.WsUtils.param;

import play.Logger;
import play.libs.WS.HttpResponse;
import utils.RetraiteException;
import utils.WsUtils;

public class InfoRetraiteConnector {

	// URL : "https://www.conseiller.info-retraite.fr/api/mesregimes?name=TOTO&nir=1223344555666&dtnai=07/04/2000";

	private static final String BASE_URL = "https://www.conseiller.info-retraite.fr/api/mesregimes";

	private final WsUtils wsUtils;

	public InfoRetraiteConnector(final WsUtils wsUtils) {
		this.wsUtils = wsUtils;
	}

	public String get(final String name, final String nir, final String dateNaissance) {
		try {
			final HttpResponse response = wsUtils.doGet(
					BASE_URL,
					param("name", name.trim()),
					param("nir", nir.replace(" ", "")),
					param("dtnai", dateNaissance));
			if (response.getStatus() == 404) {
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

}
