package utils.wsinforetraite;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.enums.Regime;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;

public class FakeRegimeDataProviderTest {

	private FakeRegimeDataProvider fakeRegimeDataProvider;

	@Before
	public void setUp() throws Exception {
		fakeRegimeDataProvider = new FakeRegimeDataProvider();
	}

	@Test
	public void should_provide_infos_from_strings() {

		final String[] regimes = { "CNAV", "xxx" };

		final InfoRetraiteResultRegime[] infos = fakeRegimeDataProvider.create(regimes);

		assertThat(infos).hasSize(2);
		final InfoRetraiteResultRegime info1 = infos[1];
		assertThat(info1.adresse).isEqualTo("xxx adresse");
	}

	@Test
	public void should_provide_infos_from_enums() {

		final Regime[] regimes = { Regime.CNAV, Regime.BFSP, Regime.CAVP };

		final InfoRetraiteResultRegime[] infos = fakeRegimeDataProvider.create(regimes);

		assertThat(infos).hasSize(3);
		final InfoRetraiteResultRegime info1 = infos[1];
		assertThat(info1.adresse).isEqualTo("BFSP adresse");
	}

}
