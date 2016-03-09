package utils.wsinforetraite;

import static org.fest.assertions.Assertions.assertThat;
import static utils.wsinforetraite.InfoRetraiteResult.Status.FOUND;
import static utils.wsinforetraite.InfoRetraiteResult.Status.NOTFOUND;

import org.junit.Before;
import org.junit.Test;

import utils.JsonUtils;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;

public class InfoRetraiteDecoderTest {

	private InfoRetraiteDecoder infoRetraiteDecoder;

	@Before
	public void setup() {
		infoRetraiteDecoder = new InfoRetraiteDecoder();
	}

	@Test
	public void should_decode_data() {

		final String json = JsonUtils
				.convertQuotesForJson("[{'image':'http://www.info-retraite.fr/sites/default/files/regime/logo/logo_carpimko.gif','regime':'Caisse autonome de retraite et de prévoyance des infirmiers, masseurs-kinésithérapeutes, pédicures-podologues, orthophonistes et orthoptistes','nom':'CARPIMKO','adresse':'6 Place Charles de Gaulle 78882 SAINT-QUENTIN EN YVELINES Cedex','internet':'http://www.carpimko.fr','tel1':'01 30 48 10 00','tel2':'00','fax':'01 30 48 10 77','email1':'x','email2':'y'}]");

		final InfoRetraiteResult result = infoRetraiteDecoder.decode(json);

		final InfoRetraiteResultRegime item = new InfoRetraiteResultRegime();
		item.image = "http://www.info-retraite.fr/sites/default/files/regime/logo/logo_carpimko.gif";
		item.regime = "Caisse autonome de retraite et de prévoyance des infirmiers, masseurs-kinésithérapeutes, pédicures-podologues, orthophonistes et orthoptistes";
		item.nom = "CARPIMKO";
		item.adresse = "6 Place Charles de Gaulle 78882 SAINT-QUENTIN EN YVELINES Cedex";
		item.internet = "http://www.carpimko.fr";
		item.tel1 = "01 30 48 10 00";
		item.tel2 = "00";
		item.fax = "01 30 48 10 77";
		item.email1 = "x";
		item.email2 = "y";

		final InfoRetraiteResultRegime[] items = { item };
		final InfoRetraiteResult expected = new InfoRetraiteResult(FOUND, items);
		assertThat(result).isEqualTo(expected);
	}

	@Test
	public void should_return_NOTFOUND_if_null() {

		final InfoRetraiteResult result = infoRetraiteDecoder.decode(null);

		final InfoRetraiteResult expected = new InfoRetraiteResult(NOTFOUND, null);
		assertThat(result).isEqualTo(expected);
	}
}
