package utils.wsinforetraite;

import static utils.wsinforetraite.InfoRetraiteResult.Status.ERROR;

import utils.RetraiteException;

public class InfoRetraiteWsUr implements InfoRetraite {

	private final InfoRetraiteConnector infoRetraiteConnector;
	private final InfoRetraiteDecoder infoRetraiteDecoder;

	public InfoRetraiteWsUr(final InfoRetraiteDecoder cypeXmlDecoder, final InfoRetraiteConnector infoRetraiteConnector) {
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

}
