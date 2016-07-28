package utils.wsinforetraite;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;
import utils.wsinforetraite.InfoRetraiteResult.Status;

public class InfoRetraiteResultTest {

	@Test
	public void test_extractRegimes() {

		final InfoRetraiteResultRegime[] regimes = {
				createInfoRetraiteResultRegime("regime1"),
				createInfoRetraiteResultRegime("regime2")
		};
		final InfoRetraiteResult infoRetraiteResult = new InfoRetraiteResult(Status.FOUND, regimes);

		final String regimesStr = infoRetraiteResult.extractRegimes();

		assertThat(regimesStr).isEqualTo("regime1,regime2");
	}

	private InfoRetraiteResultRegime createInfoRetraiteResultRegime(final String nom) {
		final InfoRetraiteResultRegime infoRetraiteResultRegime = new InfoRetraiteResultRegime();
		infoRetraiteResultRegime.nom = nom;
		return infoRetraiteResultRegime;
	}
}
