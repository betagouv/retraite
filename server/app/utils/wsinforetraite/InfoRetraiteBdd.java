package utils.wsinforetraite;

import models.FakeData;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;
import utils.wsinforetraite.InfoRetraiteResult.Status;

public class InfoRetraiteBdd implements InfoRetraite {

	@Override
	public InfoRetraiteResult retrieveAllInformations(final String name, final String nir, final String dateNaissance) {
		final FakeData fakeData = find(name, nir);
		if (fakeData == null) {
			return new InfoRetraiteResult(Status.NOTFOUND, null);
		}
		final String[] regimes = fakeData.regimes.trim().split(",");
		final InfoRetraiteResultRegime[] regimesInfos = new FakeRegimeDataProvider().create(regimes);
		return new InfoRetraiteResult(Status.FOUND, regimesInfos);
	}

	private FakeData find(final String name, final String nir) {
		return FakeData.find(name.trim(), nir.replace(" ", ""));
	}

}
