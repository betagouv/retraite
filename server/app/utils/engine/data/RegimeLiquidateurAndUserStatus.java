package utils.engine.data;

import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class RegimeLiquidateurAndUserStatus {

	private final RegimeAligne regimeLiquidateur;
	private final UserStatus status;

	public RegimeLiquidateurAndUserStatus() {
		this(null, null);
	}

	public RegimeLiquidateurAndUserStatus(final RegimeAligne regimeLiquidateur, final UserStatus status) {
		this.regimeLiquidateur = regimeLiquidateur;
		this.status = status;
	}

	public RegimeAligne getRegimeLiquidateur() {
		return regimeLiquidateur;
	}

	public UserStatus getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "RegimeLiquidateurAndUserStatus[regimeLiquidateur=" + regimeLiquidateur + ", status=" + status + "]";
	}

}
