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
		final InfoRetraiteResultRegime[] regimesInfos = new InfoRetraiteResultRegime[regimes.length];
		for (int i = 0; i < regimes.length; i++) {
			final String regime = regimes[i];
			regimesInfos[i] = new InfoRetraiteResultRegime();
			regimesInfos[i].regime = regime;
			regimesInfos[i].nom = regime + ":nom";
			regimesInfos[i].adresse = regime + ":adresse";
			regimesInfos[i].internet = regime + ":internet";
			regimesInfos[i].tel1 = regime + ":tel1";
			regimesInfos[i].tel2 = regime + ":tel2";
			regimesInfos[i].fax = regime + ":fax";
			regimesInfos[i].email1 = regime + ":email1";
			regimesInfos[i].email2 = regime + ":email2";

		}
		return new InfoRetraiteResult(Status.FOUND, regimesInfos);
	}

	@Override
	public String retrieveRegimes(final String name, final String nir, final String dateNaissance) {
		final FakeData fakeData = find(name, nir);
		return fakeData == null ? "" : fakeData.regimes;
	}

	private FakeData find(final String name, final String nir) {
		final FakeData fakeData = FakeData.find(name.trim(), nir.replace(" ", ""));
		// Temporaire : si pas de données, on répond : CNAV + RSI
		return fakeData == null ? createFakeData(name, nir, "CNAV,RSI") : fakeData;
	}

	private FakeData createFakeData(final String name, final String nir, final String regimes) {
		final FakeData fakeData = new FakeData();
		fakeData.nom = name;
		fakeData.nir = nir;
		fakeData.regimes = regimes;
		return fakeData;
	}

}
