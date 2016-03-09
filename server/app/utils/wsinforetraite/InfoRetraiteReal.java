package utils.wsinforetraite;

import static utils.wsinforetraite.InfoRetraiteResult.Status.ERROR;
import utils.RetraiteException;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;
import utils.wsinforetraite.InfoRetraiteResult.Status;

public class InfoRetraiteReal implements InfoRetraite {

	private final InfoRetraiteConnector infoRetraiteConnector;
	private final InfoRetraiteDecoder infoRetraiteDecoder;

	public InfoRetraiteReal(final InfoRetraiteDecoder cypeXmlDecoder, final InfoRetraiteConnector infoRetraiteConnector) {
		this.infoRetraiteDecoder = cypeXmlDecoder;
		this.infoRetraiteConnector = infoRetraiteConnector;
	}

	@Override
	public InfoRetraiteResult retrieveAllInformations(final String name, final String nir, final String dateNaissance) {
		try {
			final String response = infoRetraiteConnector.get(name, nir, dateNaissance);
			return infoRetraiteDecoder.decode(response);
		} catch (final RetraiteException e) {
			return new InfoRetraiteResult(ERROR, null);
		}
	}

	@Override
	public String retrieveRegimes(final String name, final String nir, final String dateNaissance) {
		final String response = infoRetraiteConnector.get(name, nir, dateNaissance);
		final InfoRetraiteResult result = infoRetraiteDecoder.decode(response);
		return result.status == Status.FOUND ? extractRegimes(result) : "";
	}

	private String extractRegimes(final InfoRetraiteResult result) {
		String regimes = "";
		for (final InfoRetraiteResultRegime infoRetraiteResultRegime : result.regimes) {
			if (regimes.length() > 0) {
				regimes += ",";
			}
			regimes += infoRetraiteResultRegime.nom.trim();
		}
		return regimes;
	}
}
