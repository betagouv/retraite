package utils.engine.data;

import java.util.List;
import java.util.Map;

import models.FakeData;
import utils.engine.data.enums.EcranSortie;

public class RenderData extends CommonExchangeData {

	public List<Departement> departements;

	public UserChecklist userChecklist;

	public List<ValueAndText> listeMoisAvecPremier;

	public List<String> listeAnneesDepart;

	// Temporaire pour afficher les DataRegime tant qu'on ne peut pas interroger le WS info-retraite
	public List<FakeData> fakeData;

	// public List<QuestionLiquidateur> questionsLiquidateur;

	public QuestionLiquidateur questionLiquidateur = new QuestionLiquidateur();

	public String errorMessage;

	public InfoRetraiteResultRegimeList regimesInfosAucunRegimeDeBaseAligne;

	public EcranSortie ecranSortie;

	public StringPairsList userInfos;

	public Map<String, Object> extras;

	public StringPairsList questionsAndResponses;

	@Override
	public String toString() {
		return "RenderData [departements=" + departements + ", userChecklist=" + userChecklist + ", listeMoisAvecPremier=" + listeMoisAvecPremier
				+ ", listeAnneesDepart=" + listeAnneesDepart + ", fakeData=" + fakeData + ", questionLiquidateur=" + questionLiquidateur + ", errorMessage="
				+ errorMessage + ", regimesInfosAucunRegimeDeBaseAligne=" + regimesInfosAucunRegimeDeBaseAligne + ", ecranSortie=" + ecranSortie
				+ ", userInfos=" + userInfos + ", extras=" + extras + ", questionsAndResponses=" + questionsAndResponses + ", hidden_step=" + hidden_step
				+ ", hidden_nom=" + hidden_nom + ", hidden_naissance=" + hidden_naissance + ", hidden_nir=" + hidden_nir + ", hidden_departement="
				+ hidden_departement + ", hidden_regimes=" + hidden_regimes + ", hidden_regimesInfosJsonStr=" + hidden_regimesInfosJsonStr
				+ ", hidden_liquidateurReponseJsonStr=" + hidden_liquidateurReponseJsonStr + ", hidden_departMois=" + hidden_departMois
				+ ", hidden_departAnnee=" + hidden_departAnnee + ", hidden_attestationCarriereLongue=" + hidden_attestationCarriereLongue
				+ ", hidden_liquidateurStep=" + hidden_liquidateurStep + ", hidden_liquidateur=" + hidden_liquidateur + ", hidden_userStatus="
				+ hidden_userStatus + ", hidden_liquidateurReponsesHistory=" + hidden_liquidateurReponsesHistory + ", isPDF=" + isPDF + "]";
	}

}
