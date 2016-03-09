package utils.wsinforetraite;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.libs.WS.HttpResponse;
import play.mvc.Http.Header;
import utils.WsUtils;
import utils.WsUtils.WsParam;

public class InfoRetraiteConnectorTest {

	private WsUtils wsUtilsMock;
	private InfoRetraiteConnector infoRetraiteConnector;

	@Before
	public void setup() {
		wsUtilsMock = mock(WsUtils.class);
		infoRetraiteConnector = new InfoRetraiteConnector(wsUtilsMock);
	}

	@Test
	public void should_format_data_and_return_result_if_success() {

		final HttpResponse response = createResponse(200, "OK", "abc");
		when(wsUtilsMock.doGet(
				"https://www.conseiller.info-retraite.fr/api/mesregimes",
				new WsParam("name", "DU PONT"),
				new WsParam("nir", "1223344555666"),
				new WsParam("dtnai", "07/04/2000")))
						.thenReturn(response);

		final String result = infoRetraiteConnector.get(" DU PONT ", "1 22 33 44 555 666", "07/04/2000");

		assertThat(result).isEqualTo("abc");
	}

	@Test
	public void should_return_null_if_not_found() {

		final HttpResponse response = createResponse(404, "Not found", null);
		when(wsUtilsMock.doGet(
				"https://www.conseiller.info-retraite.fr/api/mesregimes",
				new WsParam("name", "MACHIN"),
				new WsParam("nir", "1223344555666"),
				new WsParam("dtnai", "07/04/2000")))
						.thenReturn(response);

		final String result = infoRetraiteConnector.get("MACHIN", "1223344555666", "07/04/2000");

		assertThat(result).isNull();
	}

	@Test
	public void should_throw_exception_if_bad_data() {

		final HttpResponse response = createResponse(503, "Service Unavailable", null);
		when(wsUtilsMock.doGet(anyString(), any(WsParam.class), any(WsParam.class), any(WsParam.class))).thenReturn(response);

		try {
			infoRetraiteConnector.get("DUPONT", "120043", "07/04/2000");
			fail("De devrait pas arriver ici");
		} catch (final Exception e) {
			assertThat(e.getMessage()).isEqualTo("Error requesting WS conseiller.info-retraite.fr : code 503 = Service Unavailable");
		}

	}

	private HttpResponse createResponse(final int status, final String statusText, final String strResponse) {
		final HttpResponse response = new HttpResponse() {

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
				return null;
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
		return response;
	}
}
