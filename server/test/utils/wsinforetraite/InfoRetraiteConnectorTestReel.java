package utils.wsinforetraite;

import org.junit.Ignore;
import org.junit.Test;

import utils.RetraiteUnitTestBase;
import utils.WsUtils;

public class InfoRetraiteConnectorTestReel extends RetraiteUnitTestBase {

	@Test
	@Ignore
	public void test_reel() {
		final String result = new InfoRetraiteConnector(new WsUtils()).get("TOTO", "1223344555666", "07/04/2000");
		System.out.println("Resultat : " + result);
	}

}
