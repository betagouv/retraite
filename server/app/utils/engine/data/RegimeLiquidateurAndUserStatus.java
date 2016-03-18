package utils.engine.data;

import utils.engine.data.enums.EcranSortie;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class RegimeLiquidateurAndUserStatus {

	private final RegimeAligne regimeLiquidateur;
	private final UserStatus status;
	private final EcranSortie ecranSortie;

	public RegimeLiquidateurAndUserStatus() {
		this(null, null);
	}

	public RegimeLiquidateurAndUserStatus(final RegimeAligne regimeLiquidateur, final UserStatus status) {
		this(regimeLiquidateur, status, null);
	}

	public RegimeLiquidateurAndUserStatus(final EcranSortie ecranSortie) {
		this(null, null, ecranSortie);
	}

	private RegimeLiquidateurAndUserStatus(final RegimeAligne regimeLiquidateur, final UserStatus status, final EcranSortie ecranSortie) {
		this.regimeLiquidateur = regimeLiquidateur;
		this.status = status;
		this.ecranSortie = ecranSortie;
	}

	public RegimeAligne getRegimeLiquidateur() {
		return regimeLiquidateur;
	}

	public UserStatus getStatus() {
		return status;
	}

	public EcranSortie getEcranSortie() {
		return ecranSortie;
	}

	@Override
	public String toString() {
		return "RegimeLiquidateurAndUserStatus[regimeLiquidateur=" + regimeLiquidateur + ", status=" + status + "]";
	}

}
