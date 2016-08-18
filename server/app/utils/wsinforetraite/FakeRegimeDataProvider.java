package utils.wsinforetraite;

import utils.engine.data.enums.Regime;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;

public class FakeRegimeDataProvider {

	public InfoRetraiteResultRegime[] create(final String[] regimes) {
		final InfoRetraiteResultRegime[] regimesInfos = new InfoRetraiteResultRegime[regimes.length];
		for (int i = 0; i < regimes.length; i++) {
			final String regime = regimes[i];
			regimesInfos[i] = new InfoRetraiteResultRegime();
			regimesInfos[i].regime = regime + " regime";
			regimesInfos[i].nom = regime;
			regimesInfos[i].adresse = regime + " adresse";
			regimesInfos[i].internet = regime + " internet";
			regimesInfos[i].tel1 = regime + " tel1";
			regimesInfos[i].tel2 = regime + " tel2";
			regimesInfos[i].fax = regime + " fax";
			regimesInfos[i].email1 = regime + " email1";
			regimesInfos[i].email2 = regime + " email2";
		}
		return regimesInfos;
	}

	public InfoRetraiteResultRegime[] create(final Regime[] regimes) {
		final String[] regimesStr = new String[regimes.length];
		for (int i = 0; i < regimes.length; i++) {
			regimesStr[i] = regimes[i].toString();
		}
		return create(regimesStr);
	}

}
