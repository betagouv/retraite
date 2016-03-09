package controllers;

import java.util.ArrayList;
import java.util.List;

import models.FakeData;
import play.mvc.With;
import utils.engine.data.enums.Regime;

//@Check("admin")
@With(Secure.class)
public class FakeDatas extends CRUD {

	public static void blank() {
		final List<String> regimes = buildRegimesNamesList();
		render(regimes);
	}

	public static void show(final Long id) {
		final FakeData object = FakeData.findById(id);
		final List<String> regimes = buildRegimesNamesList();
		render(object, regimes);
	}

	private static List<String> buildRegimesNamesList() {
		final List<String> regimes = new ArrayList<>();
		for (final Regime regime : Regime.values()) {
			regimes.add(regime.toString());
		}
		return regimes;
	}

}
