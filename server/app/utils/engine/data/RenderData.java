package utils.engine.data;

import java.util.List;
import java.util.Map;

import models.FakeData;
import utils.engine.data.enums.EcranSortie;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;

public class RenderData extends CommonExchangeData {

	public List<Departement> departements;

	public UserChecklist userChecklist;

	public List<ValueAndText> listeMoisAvecPremier;

	public List<String> listeAnneesDepart;

	// Temporaire pour afficher les DataRegime tant qu'on ne peut pas interroger le WS info-retraite
	public List<FakeData> fakeData;

	// public List<QuestionLiquidateur> questionsLiquidateur;

	public QuestionLiquidateur2 questionLiquidateur = new QuestionLiquidateur2();

	public List<QuestionComplementaire> questionsComplementaires;

	public String errorMessage;

	public boolean isPDF;

	public List<InfoRetraiteResultRegime> regimesInfos;

	public EcranSortie ecranSortie;

	public Map<String, String> userInfos;
}
