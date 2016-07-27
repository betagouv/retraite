package utils.engine.data;

import java.util.List;

import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class CommonExchangeData {

	public String hidden_step;
	public String hidden_nom;
	public String hidden_naissance;
	public String hidden_nir;
	public String hidden_departement;
	public String hidden_regimes;
	public String hidden_liquidateurReponseJsonStr;
	public String hidden_departMois;
	public String hidden_departAnnee;
	public boolean hidden_attestationCarriereLongue;
	public LiquidateurQuestionDescriptor hidden_liquidateurStep;
	public RegimeAligne hidden_liquidateur;
	public List<UserStatus> hidden_userStatus;
	public String hidden_liquidateurReponsesHistory;

	public boolean isPDF;

}
