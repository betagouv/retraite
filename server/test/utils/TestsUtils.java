package utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import play.libs.WS.HttpResponse;
import play.mvc.Http.Header;
import utils.engine.data.enums.Regime;
import utils.wsinforetraite.InfoRetraiteResult;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;
import utils.wsinforetraite.InfoRetraiteResult.Status;

public class TestsUtils {

	public static String quote(final String jsonString) {
		return jsonString.replaceAll("''", "££").replaceAll("'", "\"").replaceAll("££", "'");
	}

	public static FakeHttpResponse createResponse() {
		return new FakeHttpResponse();
	}

	public static InfoRetraiteResult createInfoRetraiteResult(final Regime... regimes) {
		final InfoRetraiteResultRegime[] regimesInfos = new InfoRetraiteResultRegime[regimes.length];
		for (int i = 0; i < regimes.length; i++) {
			regimesInfos[i] = createInfoRetraiteResultRegime(regimes[i]);
		}
		return new InfoRetraiteResult(Status.FOUND, regimesInfos);
	}

	public static InfoRetraiteResultRegime createInfoRetraiteResultRegime(final Regime regime) {
		return TestsUtils.createInfoRetraiteResultRegime(regimeNameAsFromWs(regime.getNom()));
	}

	public static InfoRetraiteResultRegime createInfoRetraiteResultRegime(final String nom) {
		final InfoRetraiteResultRegime infoRetraiteResultRegime = new InfoRetraiteResultRegime();
		infoRetraiteResultRegime.nom = nom;
		infoRetraiteResultRegime.adresse = "addr " + nom;
		infoRetraiteResultRegime.email1 = "mail " + nom;
		infoRetraiteResultRegime.tel1 = "tel " + nom;
		return infoRetraiteResultRegime;
	}

	// Méthodes privées

	public static class FakeHttpResponse extends HttpResponse {

		private int status;
		private String statusText;
		private String strResponse;

		public FakeHttpResponse withStatus(final int status, final String statusText) {
			this.status = status;
			this.statusText = statusText;
			return this;
		}

		public FakeHttpResponse withReponse(final String strResponse) {
			this.strResponse = strResponse;
			return this;
		}

		@Override
		public InputStream getStream() {
			return null;
		}

		@Override
		public String getStatusText() {
			return statusText;
		}

		@Override
		public Integer getStatus() {
			return status;
		}

		@Override
		public List<Header> getHeaders() {
			final List<Header> headers = new ArrayList<>();
			return headers;
		}

		@Override
		public String getHeader(final String key) {
			return null;
		}

		@Override
		public String getString() {
			return strResponse;
		}

		@Override
		public String getString(final String encoding) {
			return null;
		}

	};

	private static String regimeNameAsFromWs(final String nom) {
		if (nom == "MSA") {
			return "CCMSA";
		}
		return nom;
	}

}
