package utils.dao;

import models.Regime;

public class RegimeDao {

	public Regime findWithCode(final String code) {
		return Regime.findById(code);
	}
}
