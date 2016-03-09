package utils.wsinforetraite;

import static utils.wsinforetraite.InfoRetraiteResult.Status.FOUND;
import static utils.wsinforetraite.InfoRetraiteResult.Status.NOTFOUND;
import utils.JsonUtils;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;

public class InfoRetraiteDecoder {

	public InfoRetraiteResult decode(final String json) {
		if (json == null) {
			return new InfoRetraiteResult(NOTFOUND, null);
		}
		final InfoRetraiteResultRegime[] regimes = JsonUtils.fromJson(json, InfoRetraiteResultRegime[].class);
		return new InfoRetraiteResult(FOUND, regimes);
	}
}
