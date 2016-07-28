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

}
