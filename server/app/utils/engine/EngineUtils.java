package utils.engine;

import utils.engine.data.enums.RegimeAligne;

public class EngineUtils {

	public static boolean areRegimes(final RegimeAligne[] regimesAlignes, final RegimeAligne... regimesToSearch) {
		if (regimesAlignes.length != regimesToSearch.length) {
			return false;
		}
		for (final RegimeAligne regimeToSearch : regimesToSearch) {
			if (!isRegime(regimesAlignes, regimeToSearch)) {
				return false;
			}

		}
		return true;
	}

	private static boolean isRegime(final RegimeAligne[] regimesAlignes, final RegimeAligne regimeToSearch) {
		for (final RegimeAligne regimeAligne : regimesAlignes) {
			if (regimeAligne == regimeToSearch) {
				return true;
			}
		}
		return false;
	}

}
