package utils.wsinforetraite;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.wsinforetraite.InfoRetraiteResult.Status.ERROR;
import static utils.wsinforetraite.InfoRetraiteResult.Status.FOUND;

import org.junit.Before;
import org.junit.Test;

import utils.RetraiteException;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;
import utils.wsinforetraite.InfoRetraiteResult.Status;

public class InfoRetraiteWsUrTest {

	private static final String JSON_RESULT = "[{'image':'http://www.info-retraite.fr/sites/default/files/regime/logo/logo_carpimko.gif','regime':'Caisse autonome de retraite et de prévoyance des infirmiers, masseurs-kinésithérapeutes, pédicures-podologues, orthophonistes et orthoptistes','nom':'CARPIMKO','adresse':'6 Place Charles de Gaulle 78882 SAINT-QUENTIN EN YVELINES Cedex','internet':'http://www.carpimko.fr','tel1':'01 30 48 10 00','tel2':'00','fax':'01 30 48 10 77','email1':'x','email2':'y'}]";

	private InfoRetraite infoRetraite;

	private InfoRetraiteDecoder infoRetraiteDecoderMock;
	private InfoRetraiteConnector infoRetraiteConnectorMock;

	@Before
	public void setup() {
		infoRetraiteDecoderMock = mock(InfoRetraiteDecoder.class);
		infoRetraiteConnectorMock = mock(InfoRetraiteConnector.class);
		infoRetraite = new InfoRetraiteWsUr(infoRetraiteDecoderMock, infoRetraiteConnectorMock);
	}

	@Test
	public void retrieveAllInformations_should_do_request_decode_and_return_result() {

		when(infoRetraiteConnectorMock.get("DUPONT", "1223344555666", "07/04/2000")).thenReturn(JSON_RESULT);
		final InfoRetraiteResult resultExpected = new InfoRetraiteResult(FOUND, new InfoRetraiteResult.InfoRetraiteResultRegime[0]);
		when(infoRetraiteDecoderMock.decode(JSON_RESULT)).thenReturn(resultExpected);

		final InfoRetraiteResult result = infoRetraite.retrieveAllInformations("DUPONT", "1223344555666", "07/04/2000");

		assertThat(result).isSameAs(resultExpected);
	}

	@Test
	public void retrieveAllInformations_should_return_error_result_if_exception_during_request() {

		doThrow(RetraiteException.class).when(infoRetraiteConnectorMock).get("DUPONT", "12004", "07/04/2000");

		final InfoRetraiteResult result = infoRetraite.retrieveAllInformations("DUPONT", "12004", "07/04/2000");

		final InfoRetraiteResult resultExpected = new InfoRetraiteResult(ERROR, null);
		assertThat(result).isEqualTo(resultExpected);
	}

	@Test
	public void retrieveRegimes_should_do_request_decode_and_return_result() {

		when(infoRetraiteConnectorMock.get("DUPONT", "1223344555666", "07/04/2000")).thenReturn(JSON_RESULT);
		final InfoRetraiteResult resultExpected = new InfoRetraiteResult(FOUND, new InfoRetraiteResult.InfoRetraiteResultRegime[] {
				createInfoRetraiteResultRegime("a"),
				createInfoRetraiteResultRegime("b c ")
		});
		when(infoRetraiteDecoderMock.decode(JSON_RESULT)).thenReturn(resultExpected);

		final String regimes = infoRetraite.retrieveRegimes("DUPONT", "1223344555666", "07/04/2000");

		assertThat(regimes).isEqualTo("a,b c");
	}

	@Test
	public void retrieveRegimes_should_do_request_decode_and_return_empty_if_not_found() {

		when(infoRetraiteConnectorMock.get("DUPONT", "1223344555666", "07/04/2000")).thenReturn(JSON_RESULT);
		final InfoRetraiteResult resultExpected = new InfoRetraiteResult(Status.NOTFOUND, null);
		when(infoRetraiteDecoderMock.decode(JSON_RESULT)).thenReturn(resultExpected);

		final String regimes = infoRetraite.retrieveRegimes("DUPONT", "1223344555666", "07/04/2000");

		assertThat(regimes).isEqualTo("");
	}

	private InfoRetraiteResultRegime createInfoRetraiteResultRegime(final String regime) {
		final InfoRetraiteResultRegime infoRetraiteResultRegime = new InfoRetraiteResultRegime();
		infoRetraiteResultRegime.nom = regime;
		return infoRetraiteResultRegime;
	}
}
