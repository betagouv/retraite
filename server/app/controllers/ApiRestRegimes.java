package controllers;

import java.util.ArrayList;
import java.util.List;

import utils.engine.data.enums.Regime;
import utils.engine.data.enums.Regime.RegimeType;

public class ApiRestRegimes extends RetraiteController {

	public static void getRegimes() {
		final List<RestRegimeNameAndType> regimes = new ArrayList<>();
		for (final Regime regime : Regime.values()) {
			regimes.add(new RestRegimeNameAndType(regime));
		}
		renderJSON(regimes);
	}

	public static void getRegimesBaseAlignes() {
		final List<RestRegimeNameAndType> regimes = new ArrayList<>();
		for (final Regime regime : Regime.values()) {
			if (regime.getType() == RegimeType.BASE_ALIGNE) {
				regimes.add(new RestRegimeNameAndType(regime));
			}
		}
		renderJSON(regimes);
	}

	@SuppressWarnings("unused")
	private static final class RestRegimeNameAndType {
		private final String name;
		private final String type;

		public RestRegimeNameAndType(final Regime regime) {
			this.name = regime.toString();
			this.type = regime.getType().toString();
		}

	}
}
