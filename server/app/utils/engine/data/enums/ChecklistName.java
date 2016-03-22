package utils.engine.data.enums;

public enum ChecklistName {
	CNAV, RSI, MSA;

	public static ChecklistName valueOf(final RegimeAligne regime) {
		return valueOf(regime.toString());
	}
}
