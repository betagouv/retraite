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
import utils.engine.data.enums.QuestionChoiceValue;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class DisplayerLiquidateurQuestionsTest {

	private PostData postData;
	private RenderData renderData;
	private final String regimes = "CNAV,CCMSA,AGIRC ARRCO";

	private QuestionSolverA questionSolverAMock;
	private QuestionSolverB questionSolverBMock;
	private QuestionSolverC questionSolverCMock;
	private QuestionSolverD questionSolverDMock;
	private QuestionSolverE questionSolverEMock;
	private QuestionSolverF questionSolverFMock;

	private DisplayerLiquidateurQuestions displayerLiquidateurQuestions;

	@Before
	public void setUp() throws Exception {
		postData = new PostData();
		renderData = new RenderData();

		questionSolverAMock = mock(QuestionSolverA.class);
		solveNothingWhenCalled(questionSolverAMock);

		questionSolverBMock = mock(QuestionSolverB.class);
		solveNothingWhenCalled(questionSolverBMock);

		questionSolverCMock = mock(QuestionSolverC.class);
		solveNothingWhenCalled(questionSolverCMock);

		questionSolverDMock = mock(QuestionSolverD.class);
		solveNothingWhenCalled(questionSolverDMock);

		questionSolverEMock = mock(QuestionSolverE.class);
		solveNothingWhenCalled(questionSolverEMock);

		questionSolverFMock = mock(QuestionSolverF.class);
		solveNothingWhenCalled(questionSolverFMock);

		displayerLiquidateurQuestions = new DisplayerLiquidateurQuestions(questionSolverAMock, questionSolverBMock, questionSolverCMock, questionSolverDMock,
				questionSolverEMock, questionSolverFMock);
	}

	@After
	public void noMoreInteractionsWithOtherMocks() {
		verifyNoMoreInteractions(questionSolverAMock, questionSolverBMock, questionSolverCMock);
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

		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_A.toString());
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_A);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_debut_question_B_si_pas_MSA() {

		// Step : (null) --> QUESTION_B

		postData.hidden_liquidateurStep = null;
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_B.toString());
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_B);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(SALARIE);
	}

	@Test
	public void test_question_B_apres_question_A_avec_filtre_et_avec_deux_activites() {

		// Step : QUESTION_A --> QUESTION_B avec DEUX_ACTIVITES

		postData.hidden_liquidateurStep = QUESTION_A.toString();
		postData.liquidateurReponseJsonStr = "[\"NSA\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA, RSI };

		when(questionSolverAMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(null, STATUS_NSA));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverAMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_B.toString());
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isEqualTo("STATUS_NSA");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_B);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(NSA, SA, INDEP, CONJOINT_INDEP, DEUX_ACTIVITES);
	}

	@Test
	public void test_question_B_apres_question_A_avec_filtre_et_sans_deux_activites() {

		// Step : QUESTION_A --> QUESTION_B sans DEUX_ACTIVITES

		postData.hidden_liquidateurStep = QUESTION_A.toString();
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverAMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_B.toString());
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_B);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(INDEP, CONJOINT_INDEP);
	}

	@Test
	public void test_question_C_apres_question_A_si_RSI_et_regime_liquidateur_determine() {

		// Step : QUESTION_A --> QUESTION_C (= pas de QUESTION_B si régime liquidateur déterminé)

		postData.hidden_liquidateurStep = QUESTION_A.toString();
		postData.liquidateurReponseJsonStr = "[\"NSA\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };

		when(questionSolverAMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(MSA, STATUS_NSA));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverAMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_C.toString());
		assertThat(renderData.hidden_liquidateur).isEqualTo(MSA.toString());
		assertThat(renderData.hidden_userStatus).isEqualTo("STATUS_NSA");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_E_apres_question_A_si_MSA_regime_liquidateur() {

		// Step : QUESTION_A --> QUESTION_E

		postData.hidden_liquidateurStep = QUESTION_A.toString();
		postData.liquidateurReponseJsonStr = "[\"DEUX_ACTIVITES\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };

		// Ecran A : réponse "Les deux" :
		when(questionSolverAMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(MSA, null));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverAMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_E.toString());
		assertThat(renderData.hidden_liquidateur).isEqualTo(MSA.toString());
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_E);
	}

	@Test
	public void test_plus_de_question_apres_question_A_si_CNAV_regime_liquidateur() {

		// Step : QUESTION_A --> (plus de question)

		postData.hidden_liquidateurStep = QUESTION_A.toString();
		postData.liquidateurReponseJsonStr = "[\"NSA\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA, CNAV };

		// Ecran A : réponse "Les deux" :
		when(questionSolverAMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(CNAV, null));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverAMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_question_C_apres_question_B_si_RSI() {

		// Step : QUESTION_B --> QUESTION_C sans filtre de choix

		postData.hidden_liquidateurStep = QUESTION_B.toString();
		postData.liquidateurReponseJsonStr = "[\"INDEP\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };

		when(questionSolverBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverBMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_C.toString());
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_C_apres_question_B_avec_filtre_si_RSI_liquidateur_depuis_ancienne_question() {

		// Step : QUESTION_B --> QUESTION_C avec filtre pour les choix

		postData.hidden_liquidateurStep = QUESTION_B.toString();
		postData.liquidateurReponseJsonStr = "[\"INDEP\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = RSI.toString();

		when(questionSolverBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverBMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_C.toString());
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(INVALIDITE_RSI, PENIBILITE);
	}

	@Test
	public void test_question_C_apres_question_B_avec_filtre_si_RSI_liquidateur_depuis_derniere_question() {

		// Step : QUESTION_B --> QUESTION_C avec filtre pour les choix

		postData.hidden_liquidateurStep = QUESTION_B.toString();
		postData.liquidateurReponseJsonStr = "[\"INDEP\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = null;

		when(questionSolverBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(RSI, STATUS_CHEF));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverBMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_C.toString());
		assertThat(renderData.hidden_liquidateur).isEqualTo(RSI.toString());
		assertThat(renderData.hidden_userStatus).isEqualTo("STATUS_CHEF");
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_C);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(INVALIDITE_RSI, PENIBILITE);
	}

	@Test
	public void test_question_D_apres_question_B() {

		// Step : QUESTION_B --> QUESTION_D

		postData.hidden_liquidateurStep = QUESTION_B.toString();
		postData.liquidateurReponseJsonStr = "[\"INDEP\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };
		postData.hidden_liquidateur = null;

		// Ecran B : réponse "Deux activités en même temps"
		when(questionSolverBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverBMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_D.toString());
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_D);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(SANTE_CPAM);
	}

	@Test
	public void test_plus_de_question_apres_question_B_si_regime_liquidateur_MSA_determine() {

		// Step : QUESTION_B --> (plus de question)

		postData.hidden_liquidateurStep = QUESTION_B.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };
		postData.hidden_liquidateur = null;

		// Ecran B : choix "Chef d'exploitation"
		when(questionSolverBMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(RegimeAligne.MSA, STATUS_NSA));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverBMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_ecran_sortie_apres_question_C() {

		// Step : QUESTION_C --> Ecran de sortie PENINILITE

		postData.hidden_liquidateurStep = QUESTION_C.toString();
		postData.liquidateurReponseJsonStr = "[\"PENIBILITE\"]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = null;

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus(ECRAN_SORTIE_PENIBILITE));

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverCMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
		assertThat(renderData.ecranSortie).isEqualTo(ECRAN_SORTIE_PENIBILITE);
	}

	@Test
	public void test_question_D_apres_question_C_pas_de_filtre_si_3_regimes() {

		// Step : QUESTION_C --> QUESTION_D sans filtre pour les choix

		postData.hidden_liquidateurStep = QUESTION_C.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV, MSA, RSI };
		postData.hidden_liquidateur = null;

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverCMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_D.toString());
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_D);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_D_apres_question_C_avec_filtre_si_2_regimes() {

		// Step : QUESTION_C --> QUESTION_D avec filtre pour les choix

		postData.hidden_liquidateurStep = QUESTION_C.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA, RSI };
		postData.hidden_liquidateur = null;

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverCMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_D.toString());
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_D);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).containsOnly(SANTE_MSA, SANTE_RSI);
	}

	@Test
	public void test_question_E_apres_question_C_si_liq_MSA_et_sans_etats_NSA_SA() {

		// Step : QUESTION_C --> QUESTION_E si liquidateur=MSA et ni NSA ni SA

		postData.hidden_liquidateurStep = QUESTION_C.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };
		postData.hidden_liquidateur = MSA.toString();

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverCMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_E.toString());
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_E);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_F_apres_question_C_si_liq_RSI_et_sans_etats_CHEF_CC() {

		// Step : QUESTION_C --> QUESTION_F si liquidateur=RSI et ni CHEF ni CC

		postData.hidden_liquidateurStep = QUESTION_C.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = RSI.toString();

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverCMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_F.toString());
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_F);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_plus_de_question_apres_question_C_si_liq_RSI_et_avec_etats_NSA() {

		// Step : QUESTION_C --> (plus de question)

		postData.hidden_liquidateurStep = QUESTION_C.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };
		postData.hidden_liquidateur = MSA.toString();
		postData.hidden_userStatus = STATUS_NSA.toString();

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverCMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_plus_de_question_apres_question_C_si_liq_MSA_et_avec_etats_SA() {

		// Step : QUESTION_C --> (plus de question)

		postData.hidden_liquidateurStep = QUESTION_C.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };
		postData.hidden_liquidateur = MSA.toString();
		postData.hidden_userStatus = STATUS_SA.toString();

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverCMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_plus_de_question_apres_question_C_si_liq_RSI_et_avec_etats_CHEF() {

		// Step : QUESTION_C --> (plus de question)

		postData.hidden_liquidateurStep = QUESTION_C.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = RSI.toString();
		postData.hidden_userStatus = UserStatus.STATUS_CHEF.toString();

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverCMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_plus_de_question_apres_question_C_si_liq_RSI_et_avec_etats_CC() {

		// Step : QUESTION_C --> (plus de question)

		postData.hidden_liquidateurStep = QUESTION_C.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = RSI.toString();
		postData.hidden_userStatus = UserStatus.STATUS_CONJOINT_COLLABORATEUR.toString();

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverCMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_question_E_apres_question_D_si_liq_MSA_et_sans_etats_NSA_SA() {

		// Step : QUESTION_D --> QUESTION_E si liquidateur=MSA et ni NSA ni SA

		postData.hidden_liquidateurStep = QUESTION_D.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { MSA };
		postData.hidden_liquidateur = MSA.toString();

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverDMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_E.toString());
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_E);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_question_F_apres_question_D_si_liq_RSI_et_sans_etats_CHEF_CC() {

		// Step : QUESTION_D --> QUESTION_F si liquidateur=RSI et ni CHEF ni CC

		postData.hidden_liquidateurStep = QUESTION_D.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = RSI.toString();

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverDMock);
		assertThat(renderData.hidden_liquidateurStep).isEqualTo(QUESTION_F.toString());
		assertThat(renderData.hidden_liquidateur).isNull();
		assertThat(renderData.hidden_userStatus).isNull();
		assertThat(renderData.questionLiquidateur.liquidateurQuestionDescriptor).isEqualTo(QUESTION_F);
		assertThat(choicesValues(renderData.questionLiquidateur.choices)).isNull();
	}

	@Test
	public void test_plus_de_question_apres_question_D_si_liq_RSI_et_avec_etats_CC() {

		// Step : QUESTION_D --> (plus de question)

		postData.hidden_liquidateurStep = QUESTION_D.toString();
		postData.liquidateurReponseJsonStr = "[]";
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RSI };
		postData.hidden_liquidateur = RSI.toString();
		postData.hidden_userStatus = UserStatus.STATUS_CONJOINT_COLLABORATEUR.toString();

		when(questionSolverCMock.solve(regimesAlignes, postData.liquidateurReponseJsonStr))
				.thenReturn(new RegimeLiquidateurAndUserStatus());

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverDMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_plus_de_question_apres_question_E() {

		postData.hidden_liquidateurStep = QUESTION_E.toString();
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverEMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
	}

	@Test
	public void test_plus_de_question_apres_question_F() {

		postData.hidden_liquidateurStep = QUESTION_F.toString();
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { CNAV };

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, regimesAlignes);

		verifySolverIsCalled(questionSolverFMock);
		assertThat(renderData.hidden_liquidateurStep).isNull();
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

	private void solveNothingWhenCalled(final QuestionSolver questionSolverMock) {
		when(questionSolverMock.solve(any(RegimeAligne[].class), anyString()))
				.thenReturn(new RegimeLiquidateurAndUserStatus());
	}

}
