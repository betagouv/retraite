package utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import play.libs.WS.HttpResponse;
import play.mvc.Http.Header;

public class TestsUtils {

	public static String quote(final String jsonString) {
		return jsonString.replaceAll("''", "££").replaceAll("'", "\"").replaceAll("££", "'");
	}

	public static FakeHttpResponse createResponse() {
		return new FakeHttpResponse();
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

	};

}
