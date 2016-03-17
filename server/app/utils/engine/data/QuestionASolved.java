package utils.engine.data;

import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class QuestionASolved {

	private final RegimeAligne regimeLiquidateur;
	private final UserStatus status;

	public QuestionASolved() {
		this(null, null);
	}

	public QuestionASolved(final RegimeAligne regimeLiquidateur, final UserStatus status) {
		this.regimeLiquidateur = regimeLiquidateur;
		this.status = status;
	}

	public RegimeAligne getRegimeLiquidateur() {
		return regimeLiquidateur;
	}

	public UserStatus getStatus() {
		return status;
	}

}
