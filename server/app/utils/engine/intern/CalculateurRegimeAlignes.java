package utils.engine.intern;

import java.util.ArrayList;
import java.util.List;

import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;

public class CalculateurRegimeAlignes {

	public RegimeAligne[] getRegimesAlignes(final String regimes) {
		final List<RegimeAligne> regimesAlignes = new ArrayList<>();
		if (regimes.contains("CNAV")) {
			regimesAlignes.add(RegimeAligne.CNAV);
		}
		if (regimes.contains("CCMSA")) {
			regimesAlignes.add(RegimeAligne.MSA);
		}
		if (regimes.contains("CANCAVA") || regimes.contains("ORGANIC") || regimes.contains("RSI")) {
			regimesAlignes.add(RegimeAligne.RSI);
		}
		return regimesAlignes.toArray(new RegimeAligne[regimesAlignes.size()]);
	}

	public RegimeAligne[] getRegimesAlignes(final Regime[] regimesArray) {
		String regimes = "";
		for (final Regime regime : regimesArray) {
			regimes += regime.toString() + ",";
		}
		return getRegimesAlignes(regimes);

		// final List<Regime> regimes = Arrays.asList(regimesArray);
		// final List<RegimeAligne> regimesAlignes = new ArrayList<>();
		// if (regimes.contains(Regime.CNAV)) {
		// regimesAlignes.add(RegimeAligne.CNAV);
		// }
		// if (regimes.contains(Regime.MSA)) {
		// regimesAlignes.add(RegimeAligne.MSA);
		// }
		// if (regimes.contains(Regime.RSI)) {
		// regimesAlignes.add(RegimeAligne.RSI);
		// }
		// return regimesAlignes.toArray(new RegimeAligne[regimesAlignes.size()]);
	}

}
