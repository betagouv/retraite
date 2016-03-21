package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static utils.engine.data.enums.EcranSortie.ECRAN_SORTIE_PENIBILITE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_A;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_B;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_C;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_D;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_E;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor2.QUESTION_F;
import static utils.engine.data.enums.QuestionChoiceValue.CONJOINT_INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.INVALIDITE_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.NSA;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.QuestionChoiceValue.SALARIE;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_CPAM;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_MSA;
import static utils.engine.data.enums.QuestionChoiceValue.SANTE_RSI;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_CHEF;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;
import static utils.engine.data.enums.UserStatus.STATUS_SA;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.engine.data.QuestionChoice;
import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.RenderData;
import utils.engine.data.enums.LiquidateurQuestionDescriptor2;
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class DisplayerLiquidateurQuestionsTest {

	private PostData postData;
	private RenderData renderData;
	private final String regimes = "CNAV,CCMSA,AGIRC ARRCO";

	private QuestionSolverA solverQuestionAMock;
	private QuestionSolverB solverQuestionBMock;
	private QuestionSolverC solverQuestionCMock;

	private DisplayerLiquidateurQuestions displayerLiquidateurQuestions;

	@Before
	public void setUp() throws Exception {
		postData = new PostData();
		renderData = new RenderData();

		solverQuestionAMock = mock(QuestionSolverA.class);
		when(solverQuestionAMock.solve(any(RegimeAligne[].class), anyString()))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		solverQuestionBMock = mock(QuestionSolverB.class);
		when(solverQuestionBMock.solve(any(RegimeAligne[].class), anyString()))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		solverQuestionCMock = mock(QuestionSolverC.class);
		when(solverQuestionCMock.solve(any(RegimeAligne[].class), anyString()))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions = new DisplayerLiquidateurQuestions(solverQuestionAMock, solverQuestionBMock, solverQuestionCMock);
	}

	@After
	public void noMoreInteractionsWithOtherMocks() {
		verifyNoMoreInteractions(solverQuestionAMock, solverQuestionBMock, solverQuestionCMock);
	}

	@Test
	public void should_set_step_and_store_regimes() {

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, null);

		assertThat(renderData.hidden_step).isEqualTo("displayLiquidateurQuestions");
		assertThat(renderData.hidden_regimes).isEqualTo(regimes);
	}

	@Test
	public void test_debut_question_A_si_MSA() {

		// Step : (null) --> QUESTION_A

		postData.hidden_liquidateurStep = null;
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV, MSA };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_A");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_A);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_debut_question_B_si_pas_MSA() {

		// Step : (null) --> QUESTION_B

		postData.hidden_liquidateurStep = null;
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_B");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_B);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(SALARIE);
	}

	@Test
	public void test_question_B_apres_question_A_avec_filtre_et_avec_deux_activites() {

		// Step : QUESTION_A --> QUESTION_B avec DEUX_ACTIVITES

		postData.hidden_liquidateurStep = "QUESTION_A";
		postData.liquidateurReponseJsonStr = "[\"NSA\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA, RSI };

		when(solverQuestionAMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(null, STATUS_NSA));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionAMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_B");
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isEqualTo("STATUS_NSA");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_B);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(NSA, SA, INDEP, CONJOINT_INDEP, DEUX_ACTIVITES);
	}

	@Test
	public void test_question_B_apres_question_A_avec_filtre_et_sans_deux_activites() {

		// Step : QUESTION_A --> QUESTION_B sans DEUX_ACTIVITES

		postData.hidden_liquidateurStep = "QUESTION_A";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionAMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_B");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_B);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(INDEP, CONJOINT_INDEP);
	}

	@Test
	public void test_question_C_apres_question_A_si_RSI_et_regime_liquidateur_determine() {

		// Step : QUESTION_A --> QUESTION_C (= pas de QUESTION_B si régime liquidateur déterminé)

		postData.hidden_liquidateurStep = "QUESTION_A";
		postData.liquidateurReponseJsonStr = "[\"NSA\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };

		when(solverQuestionAMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(MSA, STATUS_NSA));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionAMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_C");
		assertThat(renderData.hidden_liquidateur).isEqualTo("MSA");
		assertThat(renderData.hidden_userStatus).isEqualTo("STATUS_NSA");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_E_apres_question_A_si_MSA_regime_liquidateur() {

		// Step : QUESTION_A --> QUESTION_E

		postData.hidden_liquidateurStep = "QUESTION_A";
		postData.liquidateurReponseJsonStr = "[\"DEUX_ACTIVITES\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };

		// Ecran A : réponse "Les deux" :
		when(solverQuestionAMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(MSA, null));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionAMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_E");
		assertThat(renderData.hidden_liquidateur).isEqualTo("MSA");
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_E);
	}

	@Test
	public void test_plus_de_question_apres_question_A_si_CNAV_regime_liquidateur() {

		// Step : QUESTION_A --> (plus de question)

		postData.hidden_liquidateurStep = "QUESTION_A";
		postData.liquidateurReponseJsonStr = "[\"NSA\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA, CNAV };

		// Ecran A : réponse "Les deux" :
		when(solverQuestionAMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(CNAV, null));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionAMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_question_C_apres_question_B_si_RSI() {

		// Step : QUESTION_B --> QUESTION_C sans filtre de choix

		postData.hidden_liquidateurStep = "QUESTION_B";
		postData.liquidateurReponseJsonStr = "[\"INDEP\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };

		when(solverQuestionBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionBMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_C");
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_C_apres_question_B_avec_filtre_si_RSI_liquidateur_depuis_ancienne_question() {

		// Step : QUESTION_B --> QUESTION_C avec filtre pour les choix

		postData.hidden_liquidateurStep = "QUESTION_B";
		postData.liquidateurReponseJsonStr = "[\"INDEP\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = "RSI";

		when(solverQuestionBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionBMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_C");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(INVALIDITE_RSI, PENIBILITE);
	}

	@Test
	public void test_question_C_apres_question_B_avec_filtre_si_RSI_liquidateur_depuis_derniere_question() {

		// Step : QUESTION_B --> QUESTION_C avec filtre pour les choix

		postData.hidden_liquidateurStep = "QUESTION_B";
		postData.liquidateurReponseJsonStr = "[\"INDEP\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = null;

		when(solverQuestionBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(RSI, STATUS_CHEF));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionBMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_C");
		assertThat(renderData.hidden_liquidateur).isEqualTo("RSI");
		assertThat(renderData.hidden_userStatus).isEqualTo("STATUS_CHEF");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(INVALIDITE_RSI, PENIBILITE);
	}

	@Test
	public void test_question_D_apres_question_B() {

		// Step : QUESTION_B --> QUESTION_D

		postData.hidden_liquidateurStep = "QUESTION_B";
		postData.liquidateurReponseJsonStr = "[\"INDEP\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };
		postData.hidden_liquidateur = null;

		// Ecran B : réponse "Deux activités en même temps"
		when(solverQuestionBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionBMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_D");
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_D);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(SANTE_CPAM);
	}

	@Test
	public void test_plus_de_question_apres_question_B_si_regime_liquidateur_MSA_determine() {

		// Step : QUESTION_B --> (plus de question)

		postData.hidden_liquidateurStep = "QUESTION_B";
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };
		postData.hidden_liquidateur = null;

		// Ecran B : choix "Chef d'exploitation"
		when(solverQuestionBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(RegimeAligne.MSA, STATUS_NSA));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionBMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_ecran_sortie_apres_question_C() {

		// Step : QUESTION_C --> Ecran de sortie PENINILITE

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[\"PENIBILITE\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = null;

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(ECRAN_SORTIE_PENIBILITE));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionCMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
		assertThat(renderData.ecranSortie).isEqualTo(ECRAN_SORTIE_PENIBILITE);
	}

	@Test
	public void test_question_D_apres_question_C_pas_de_filtre_si_3_regimes() {

		// Step : QUESTION_C --> QUESTION_D sans filtre pour les choix

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV, MSA, RSI };
		postData.hidden_liquidateur = null;

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionCMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_D");
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_D);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_D_apres_question_C_avec_filtre_si_2_regimes() {

		// Step : QUESTION_C --> QUESTION_D avec filtre pour les choix

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA, RSI };
		postData.hidden_liquidateur = null;

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionCMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_D");
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_D);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(SANTE_MSA, SANTE_RSI);
	}

	@Test
	public void test_question_E_apres_question_C_si_liq_MSA_et_sans_etats_NSA_SA() {

		// Step : QUESTION_C --> QUESTION_E si liquidateur=MSA et ni NSA ni SA

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };
		postData.hidden_liquidateur = "MSA";

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionCMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_E");
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_E);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_F_apres_question_C_si_liq_RSI_et_sans_etats_CHEF_CC() {

		// Step : QUESTION_C --> QUESTION_F si liquidateur=RSI et ni CHEF ni CC

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = "RSI";

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionCMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo("QUESTION_F");
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_F);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_plus_de_question_apres_question_C_si_liq_RSI_et_avec_etats_NSA() {

		// Step : QUESTION_C --> (plus de question)

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };
		postData.hidden_liquidateur = "MSA";
		postData.hidden_userStatus = STATUS_NSA.toString();

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionCMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_plus_de_question_apres_question_C_si_liq_MSA_et_avec_etats_SA() {

		// Step : QUESTION_C --> (plus de question)

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };
		postData.hidden_liquidateur = "MSA";
		postData.hidden_userStatus = STATUS_SA.toString();

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionCMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_plus_de_question_apres_question_C_si_liq_RSI_et_avec_etats_CHEF() {

		// Step : QUESTION_C --> (plus de question)

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = "RSI";
		postData.hidden_userStatus = UserStatus.STATUS_CHEF.toString();

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionCMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_plus_de_question_apres_question_C_si_liq_RSI_et_avec_etats_CC() {

		// Step : QUESTION_C --> (plus de question)

		postData.hidden_liquidateurStep = "QUESTION_C";
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = "RSI";
		postData.hidden_userStatus = UserStatus.STATUS_CONJOINT_COLLABORATEUR.toString();

		when(solverQuestionCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(solverQuestionCMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_plus_de_question() {

		postData.hidden_liquidateurStep = getLastQuestionAsStr();
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	private String getLastQuestionAsStr() {
		final LiquidateurQuestionDescriptor2[] values = LiquidateurQuestionDescriptor2.values();
		return values[values.length - 1].toString();
	}

	private List<QuestionChoiceValue> choicesValues(final List<QuestionChoice> choices) {
		if (choices == null) {
			return null;
		}
		final ArrayList<QuestionChoiceValue> choicesValues = new ArrayList<>();
		for (final QuestionChoice questionChoice : choices) {
			choicesValues.add(questionChoice.getValue());
		}
		return choicesValues;
	}

	private void verifySolverIsCalled(final QuestionSolver solver) {
		verify(solver).solve(any(RegimeAligne[].class), anyString());
	}

}
