package utils.wsinforetraite;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.TestsUtils.createResponse;

import org.junit.Before;
import org.junit.Test;

import play.libs.WS.HttpResponse;
import utils.WsUtils;
import utils.WsUtils.WsArg;
import utils.WsUtils.WsCookie;
import utils.WsUtils.WsParam;

public class InfoRetraiteConnectorTest {

	final String CSRF_TOKEN = "74f3e4ba861b78ba85d4fbe13606ba4a66c0ec9f-1111111111111111111111111";
	final String PLAY_SESSION = "4c9c83c0b18554e4509f88964750a5d3e327f395-csrfToken=06fc49e49dc5db8c7f0bdba37b278277d8eb3e7d-1455531609170-7aa25501f34992df53296849";
	final InfoRetraiteTokens TOKENS = new InfoRetraiteTokens(PLAY_SESSION, CSRF_TOKEN);

	private WsUtils wsUtilsMock;
	private InfoRetraiteTokenRecuperator infoRetraiteTokenRecuperatorMock;

	private InfoRetraiteConnector infoRetraiteConnector;

	@Before
	public void setup() {
		wsUtilsMock = mock(WsUtils.class);

		infoRetraiteTokenRecuperatorMock = mock(InfoRetraiteTokenRecuperator.class);
		when(infoRetraiteTokenRecuperatorMock.getToken()).thenReturn(TOKENS);

		infoRetraiteConnector = new InfoRetraiteConnector(wsUtilsMock, infoRetraiteTokenRecuperatorMock);
	}

	@Test
	public void should_format_data_and_return_result_if_success() {

		final HttpResponse response = createResponse()
				.withStatus(200, "OK")
				.withReponse("abc");
		when(wsUtilsMock.doPost(
				"https://www.conseiller.info-retraite.fr/api/mesregimes",
				new WsCookie(PLAY_SESSION),
				new WsParam("csrfToken", CSRF_TOKEN),
				new WsParam("name", "DU PONT"),
				new WsParam("nir", "1223344555666"),
				new WsParam("dtnai", "07/04/2000")))
						.thenReturn(response);

		final String result = infoRetraiteConnector.get(" DU PONT ", "1 22 33 44 555 666 99", "07/04/2000");

		assertThat(result).isEqualTo("abc");
	}

	@Test
	public void should_return_null_if_not_found_404() {

		final HttpResponse response = createResponse()
				.withStatus(404, "Not found");
		when(wsUtilsMock.doPost(
				"https://www.conseiller.info-retraite.fr/api/mesregimes",
				new WsCookie(PLAY_SESSION),
				new WsParam("csrfToken", CSRF_TOKEN),
				new WsParam("name", "MACHIN"),
				new WsParam("nir", "1223344555666"),
				new WsParam("dtnai", "07/04/2000")))
						.thenReturn(response);

		final String result = infoRetraiteConnector.get("MACHIN", "1223344555666", "07/04/2000");

		assertThat(result).isNull();
	}

	@Test
	public void should_return_null_if_not_found_400() {

		final HttpResponse response = createResponse()
				.withStatus(400, "Not found");
		when(wsUtilsMock.doPost(
				"https://www.conseiller.info-retraite.fr/api/mesregimes",
				new WsCookie(PLAY_SESSION),
				new WsParam("csrfToken", CSRF_TOKEN),
				new WsParam("name", "MACHIN"),
				new WsParam("nir", "1223344555666"),
				new WsParam("dtnai", "07/04/2000")))
						.thenReturn(response);

		final String result = infoRetraiteConnector.get("MACHIN", "1223344555666", "07/04/2000");

		assertThat(result).isNull();
	}

	@Test
	public void should_throw_exception_if_bad_data() {

		final HttpResponse response = createResponse()
				.withStatus(503, "Service Unavailable");
		when(wsUtilsMock.doPost(anyString(), any(WsArg.class), any(WsArg.class), any(WsArg.class), any(WsArg.class), any(WsArg.class))).thenReturn(response);

		try {
			infoRetraiteConnector.get("DUPONT", "120043", "07/04/2000");
			fail("De devrait pas arriver ici");
		} catch (final Exception e) {
			assertThat(e.getMessage()).isEqualTo("Error requesting WS conseiller.info-retraite.fr : code 503 = Service Unavailable");
		}

	}
}
