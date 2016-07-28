package utils.engine.intern;

import utils.JsonUtils;
import utils.engine.data.InfoRetraiteResultRegimeList;
import utils.engine.data.UserChecklist;
import utils.engine.data.enums.Regime;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;

public class AutreRegimeComputer {

	public void compute(final UserChecklist userChecklist, final String regimesInfosJsonStr) {
		userChecklist.autresRegimesDeBase = new InfoRetraiteResultRegimeList();
		userChecklist.regimesComplementaires = new InfoRetraiteResultRegimeList();

		final InfoRetraiteResultRegimeList list = JsonUtils.fromJson(regimesInfosJsonStr, InfoRetraiteResultRegimeList.class);
		for (final InfoRetraiteResultRegime infos : list) {
			final Regime regime = Regime.valueOfNom(infos.nom);
			if (regime == Regime.AGIRC_ARRCO) {
				continue;
			}
			switch (regime.getType()) {
			case BASE_ALIGNE:
				break;
			case BASE_AUTRE:
				userChecklist.autresRegimesDeBase.add(infos);
				break;
			case COMPLEMENTAIRE:
				userChecklist.regimesComplementaires.add(infos);
				break;
			}
		}
	}

}
