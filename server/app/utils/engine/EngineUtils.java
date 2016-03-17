package utils.engine;

import utils.engine.data.enums.RegimeAligne;

public class EngineUtils {

	public static boolean areRegimes(final RegimeAligne[] regimesAlignes, final RegimeAligne... regimesToSearch) {
		if (regimesAlignes.length != regimesToSearch.length) {
			return false;
		}
		for (final RegimeAligne regimeToSearch : regimesToSearch) {
			if (!contains(regimesAlignes, regimeToSearch)) {
				return false;
			}

		}
		return true;
	}

	public static boolean contains(final RegimeAligne[] regimesAlignes, final RegimeAligne regimeAligneToSearch) {
		if (regimesAlignes != null) {
			for (final RegimeAligne regimeAligne : regimesAlignes) {
				if (regimeAligne == regimeAligneToSearch) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean contains(final RegimeAligne[] regimesAlignes, final RegimeAligne... regimesAligneToSearch) {
		for (final RegimeAligne regimeAligneToSearch : regimesAligneToSearch) {
			if (!contains(regimesAlignes, regimeAligneToSearch)) {
				return false;
			}
		}
		return true;
	}

}
