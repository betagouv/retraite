package utils.wsinforetraite;

import org.junit.Ignore;
import org.junit.Test;

import utils.RetraiteUnitTestBase;
import utils.WsUtils;

public class InfoRetraiteWsUrRealTest extends RetraiteUnitTestBase {

	@Test
	@Ignore
	public void retrieveAllInformations() {
		final WsUtils wsUtils = new WsUtils();
		final InfoRetraiteWsUr infoRetraite = new InfoRetraiteWsUr(
				new InfoRetraiteDecoder(),
				new InfoRetraiteConnector(wsUtils,
						new InfoRetraiteTokenRecuperator(wsUtils)));
		final InfoRetraiteResult result = infoRetraite.retrieveAllInformations("NOPRE", "1700714118025", "01/07/1970");
		System.out.println(result);
	}

}
