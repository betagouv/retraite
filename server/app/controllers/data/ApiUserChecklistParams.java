package controllers.data;

import java.util.Arrays;
import java.util.Set;

import play.mvc.Scope.Params;
import utils.RetraiteException;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class ApiUserChecklistParams {

	public String nom;
	public String dateNaissance;
	public String nir;
	public String departement;
	public String departMois;
	public String departAnnee;
	public RegimeAligne regimeLiquidateur;
	public Regime[] regimes;
	public boolean isCarriereLongue;
	public UserStatus userStatus;
	public boolean published;
	public boolean full;

	public static ApiUserChecklistParams fromHttpParams(final Params params) {
		final ApiUserChecklistParams apiUserChecklistParams = new ApiUserChecklistParams();
		apiUserChecklistParams.nom = getAndRemove(params, "nom");
		apiUserChecklistParams.dateNaissance = getAndRemove(params, "dateNaissance");
		apiUserChecklistParams.nir = getAndRemove(params, "nir");
		apiUserChecklistParams.departement = getAndRemove(params, "departement");
		apiUserChecklistParams.departMois = getAndRemove(params, "departMois");
		apiUserChecklistParams.departAnnee = getAndRemove(params, "departAnnee");
		apiUserChecklistParams.regimeLiquidateur = getAndRemoveAsRegimeAligne(params, "regimeLiquidateur");
		apiUserChecklistParams.regimes = Regime.fromStringList(getAndRemove(params, "regimes"));
		apiUserChecklistParams.isCarriereLongue = asBoolean(getAndRemove(params, "isCarriereLongue"));
		apiUserChecklistParams.userStatus = getAndRemoveAsUserStatus(params);
		apiUserChecklistParams.published = asBoolean(getAndRemove(params, "published"));
		apiUserChecklistParams.full = asBoolean(getAndRemove(params, "full"));
		checkDoNotRemainParams(params);
		return apiUserChecklistParams;
	}

	private static UserStatus getAndRemoveAsUserStatus(final Params params) {
		final String userStatusStr = getAndRemove(params, "userStatus");
		return userStatusStr == null ? null : UserStatus.valueOf(userStatusStr);
	}

	private static RegimeAligne getAndRemoveAsRegimeAligne(final Params params, final String key) {
		final String param = params.get(key);
		params.remove(key);
		return param == null ? null : RegimeAligne.valueOf(param);
	}

	private static String getAndRemove(final Params params, final String key) {
		final String param = params.get(key);
		params.remove(key);
		return param;
	}

	private static void checkDoNotRemainParams(final Params params) {
		getAndRemove(params, "controller");
		getAndRemove(params, "action");
		getAndRemove(params, "body");
		final Set<String> keySet = params.data.keySet();
		if (keySet.size() > 0) {
			final String key = keySet.iterator().next();
			final String value = params.get(key);
			throw new RetraiteException("Le paramètre '" + key + "' avec la valeur '" + value + "' n'est pas attendu");
		}
	}

	@Override
	public String toString() {
		return "ApiUserChecklistParams[nom=" + nom + ", dateNaissance=" + dateNaissance + ", nir=" + nir + ", departement=" + departement + ", departMois="
				+ departMois + ", departAnnee=" + departAnnee + ", regimeLiquidateur=" + regimeLiquidateur + ", regimes=" + Arrays.toString(regimes)
				+ ", isCarriereLongue=" + isCarriereLongue + ", userStatus=" + userStatus + ", published=" + published + ", full=" + full + "]";
	}

	private static boolean asBoolean(final String value) {
		return "true".equals(value);
	}

}
