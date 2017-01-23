package utils.wsinforetraite;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import utils.dao.RegimeDao;
import utils.engine.data.enums.Regime;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;

public class FakeRegimeDataProvider {

	public InfoRetraiteResultRegime[] create(final String[] regimes) {
		final InfoRetraiteResultRegime[] regimesInfos = new InfoRetraiteResultRegime[regimes.length];
		for (int i = 0; i < regimes.length; i++) {

			regimesInfos[i] = new InfoRetraiteResultRegime();
			regimesInfos[i].nom = regimes[i];
			
			String code = Regime.valueOfNom(regimes[i]).getCode();
			RegimeDao regimeDao = new RegimeDao();
			models.Regime regime = regimeDao.findWithCode(code);
			
			if (regime != null) {
				regimesInfos[i].regime = regime.nom;
				regimesInfos[i].adresse = regime.adresse;
				regimesInfos[i].internet = regime.site;
				regimesInfos[i].tel1 = regime.tel;
			}
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
