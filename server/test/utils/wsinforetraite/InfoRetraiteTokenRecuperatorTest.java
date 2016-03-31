package utils.wsinforetraite;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.TestsUtils.createResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import play.libs.WS.HttpResponse;
import utils.WsUtils;

public class InfoRetraiteTokenRecuperatorTest {

	private InfoRetraiteTokenRecuperator infoRetraiteTokenRecuperator;
	private WsUtils wsUtilsMock;

	@Before
	public void setup() {
		wsUtilsMock = mock(WsUtils.class);
		infoRetraiteTokenRecuperator = new InfoRetraiteTokenRecuperator(wsUtilsMock);
	}

	@Test
	public void test() throws IOException {

		final HttpResponse response = createResponse()
				.withStatus(200, "OK")
				.withReponse(getContentFromResFile("res/conseiller-info-retraite.html"));
		when(wsUtilsMock.doGet("https://www.conseiller.info-retraite.fr/"))
				.thenReturn(response);

		final InfoRetraiteTokens token = infoRetraiteTokenRecuperator.getToken();

		assertThat(token.getCsrfToken()).isEqualTo("74f3e4ba861b78ba85d4fbe13606ba4a66c0ec9f-1459418070015-7aa25501f34992df53296849");
	}

	private String getContentFromResFile(final String resFilePath) throws IOException {
		final InputStream input = getClass().getResourceAsStream(resFilePath);
		String content = "";
		final List<String> lines = IOUtils.readLines(input);
		for (final String line : lines) {
			content += line;
		}
		return content;
	}
}
