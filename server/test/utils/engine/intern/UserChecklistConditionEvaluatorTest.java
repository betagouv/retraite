package utils.engine.intern;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.engine.data.enums.Regime.AGIRC_ARRCO;
import static utils.engine.data.enums.Regime.BFSP;
import static utils.engine.data.enums.Regime.CNAV;
import static utils.engine.data.enums.Regime.FSPOEIE;
import static utils.engine.data.enums.Regime.IRCANTEC;
import static utils.engine.data.enums.Regime.RSI;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import models.Condition;
import utils.engine.data.ConditionDelai;
import utils.engine.data.MonthAndYear;
import utils.engine.data.UserChecklistGenerationData;

public class UserChecklistConditionEvaluatorTest {

	private UserChecklistConditionDelaiEvaluator userChecklistConditionDelaiEvaluatorMock;

	private UserChecklistConditionEvaluator userChecklistConditionEvaluator;

	@Before
	public void setUp() {

		userChecklistConditionDelaiEvaluatorMock = mock(UserChecklistConditionDelaiEvaluator.class);

		userChecklistConditionEvaluator = new UserChecklistConditionEvaluator(userChecklistConditionDelaiEvaluatorMock);
	}

	@Test
	public void should_return_true_for_unknown_condition() {

		final Condition condition = new Condition();
		condition.props.put("type", "xx");
		condition.props.put("regime", "yy");

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create().get();

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isTrue();
	}

	// Tests pour les conditions de type "regimeDetecte"

	@Test
	public void test_regimeDetecte_agirc_arrco_true() {

		final Condition condition = new Condition();
		condition.props.put("type", "regimeDetecte");
		condition.props.put("regime", "agirc-arrco");

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withRegimes(RSI, AGIRC_ARRCO, CNAV)
				.get();

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isTrue();
	}

	@Test
	public void test_regimeDetecte_agirc_arrco_false() {

		final Condition condition = new Condition();
		condition.props.put("type", "regimeDetecte");
		condition.props.put("regime", "agirc-arrco");

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withRegimes(RSI, CNAV)
				.get();

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isFalse();
	}

	@Test
	public void test_regimeDetecte_regimes_base_hors_alignes_true() {

		final Condition condition = new Condition();
		condition.props.put("type", "regimeDetecte");
		condition.props.put("regime", "regimes-base-hors-alignés");

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withRegimes(RSI, BFSP, AGIRC_ARRCO, CNAV)
				.get();

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isTrue();
	}

	@Test
	public void test_regimeDetecte_regimes_base_hors_alignes_false() {

		final Condition condition = new Condition();
		condition.props.put("type", "regimeDetecte");
		condition.props.put("regime", "regimes-base-hors-alignés");

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withRegimes(RSI, AGIRC_ARRCO, CNAV)
				.get();

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isFalse();
	}

	@Test
	public void test_regimeDetecte_regimes_complémentaires_hors_agirc_arrco_true() {

		final Condition condition = new Condition();
		condition.props.put("type", "regimeDetecte");
		condition.props.put("regime", "regimes-complémentaires-hors-agirc-arrco");

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withRegimes(RSI, IRCANTEC, AGIRC_ARRCO, CNAV)
				.get();

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isTrue();
	}

	@Test
	public void test_regimeDetecte_regimes_complémentaires_hors_agirc_arrco_false() {

		final Condition condition = new Condition();
		condition.props.put("type", "regimeDetecte");
		condition.props.put("regime", "regimes-complémentaires-hors-agirc-arrco");

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withRegimes(RSI, AGIRC_ARRCO, CNAV)
				.get();

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isFalse();
	}

	@Test
	public void test_regimeDetecte_regimes_hors_alignés_et_hors_agirc_arrco_true() {

		final Condition condition = new Condition();
		condition.props.put("type", "regimeDetecte");
		condition.props.put("regime", "regimes-hors-alignés-et-hors-agirc-arrco");

		{
			final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
					.withRegimes(RSI, IRCANTEC, AGIRC_ARRCO, CNAV)
					.get();

			final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

			assertThat(isVerified).isTrue();
		}
		{
			final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
					.withRegimes(RSI, FSPOEIE, AGIRC_ARRCO, CNAV)
					.get();

			final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

			assertThat(isVerified).isTrue();
		}
	}

	@Test
	public void test_regimeDetecte_regimes_hors_alignés_et_hors_agirc_arrco_false() {

		final Condition condition = new Condition();
		condition.props.put("type", "regimeDetecte");
		condition.props.put("regime", "regimes-hors-alignés-et-hors-agirc-arrco");

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withRegimes(RSI, AGIRC_ARRCO, CNAV)
				.get();

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isFalse();
	}

	// Tests pour les conditions de type "statut"

	@Test
	public void test_statuts_true() {

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withNSA(true)
				.withSA(true)
				.withConjointCollaborateur(true)
				.get();

		final List<TestStatutData> data = asList(
				data("nsa", true),
				data("sa", true),
				data("conjoint-collaborateur", true));

		for (final TestStatutData testStatutData : data) {
			final Condition condition = new Condition();
			condition.props.put("type", "statut");
			condition.props.put("statut", testStatutData.statut);

			final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

			assertThat(isVerified)
					.overridingErrorMessage(
							"Pour le statut " + testStatutData.statut + ", le résultat n'est pas celui attendu " + testStatutData.expectedResult)
					.isEqualTo(testStatutData.expectedResult);
		}
	}

	@Test
	public void test_statuts_false() {

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withNSA(false)
				.withSA(false)
				.withConjointCollaborateur(false)
				.get();

		final List<TestStatutData> data = asList(
				data("nsa", false),
				data("sa", false),
				data("conjoint-collaborateur", false));

		for (final TestStatutData testStatutData : data) {
			final Condition condition = new Condition();
			condition.props.put("type", "statut");
			condition.props.put("statut", testStatutData.statut);

			final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

			assertThat(isVerified)
					.overridingErrorMessage(
							"Pour le statut " + testStatutData.statut + ", le résultat n'est pas celui attendu " + testStatutData.expectedResult)
					.isEqualTo(testStatutData.expectedResult);
		}
	}

	// type:'statut', statut: 'chef-entreprise'

	@Test
	public void test_delai_true() {

		final Condition condition = new Condition();
		condition.props.put("type", "delai");
		condition.props.put("plusOuMoins", "MOINS");
		condition.props.put("nombre", "3");
		condition.props.put("unite", "MOIS");

		when(userChecklistConditionDelaiEvaluatorMock.isVerified(any(ConditionDelai.class), any(MonthAndYear.class))).thenReturn(true);

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create().get();

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isTrue();
	}

	@Test
	public void test_delai_false() {

		final Condition condition = new Condition();
		condition.props.put("type", "delai");
		condition.props.put("plusOuMoins", "MOINS");
		condition.props.put("nombre", "3");
		condition.props.put("unite", "MOIS");

		when(userChecklistConditionDelaiEvaluatorMock.isVerified(any(ConditionDelai.class), any(MonthAndYear.class))).thenReturn(false);

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create().get();

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isFalse();
	}

	// type:'carriere-a-reconstituer'

	@Test
	public void test_carriere_a_reconstituer_true() {

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withCarriereAReconstituer(true)
				.get();

		final Condition condition = new Condition();
		condition.props.put("type", "carriere-a-reconstituer");

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isTrue();
	}

	@Test
	public void test_carriere_a_reconstituer_false() {

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withCarriereAReconstituer(false)
				.get();

		final Condition condition = new Condition();
		condition.props.put("type", "carriere-a-reconstituer");

		final boolean isVerified = userChecklistConditionEvaluator.isVerified(condition, userChecklistGenerationData);

		assertThat(isVerified).isFalse();
	}

	// type:'carriere-longue-non'

	@Test
	public void test_carriere_longue_non() {

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withCarriereLonge(false)
				.get();

		final Condition conditionNon = new Condition();
		conditionNon.props.put("type", "carriere-longue-non");
		final Condition conditionOui = new Condition();
		conditionOui.props.put("type", "carriere-longue-oui");

		final boolean isVerifiedNon = userChecklistConditionEvaluator.isVerified(conditionNon, userChecklistGenerationData);
		final boolean isVerifiedOui = userChecklistConditionEvaluator.isVerified(conditionOui, userChecklistGenerationData);

		assertThat(isVerifiedNon).isTrue();
		assertThat(isVerifiedOui).isFalse();
	}

	@Test
	public void test_carriere_longue_oui() {

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withCarriereLonge(true)
				.get();

		final Condition conditionNon = new Condition();
		conditionNon.props.put("type", "carriere-longue-non");
		final Condition conditionOui = new Condition();
		conditionOui.props.put("type", "carriere-longue-oui");

		final boolean isVerifiedNon = userChecklistConditionEvaluator.isVerified(conditionNon, userChecklistGenerationData);
		final boolean isVerifiedOui = userChecklistConditionEvaluator.isVerified(conditionOui, userChecklistGenerationData);

		assertThat(isVerifiedNon).isFalse();
		assertThat(isVerifiedOui).isTrue();
	}

	// Méthodes privées

	private TestStatutData data(final String statut, final boolean expectedResult) {
		return new TestStatutData(statut, expectedResult);
	}

	private static class TestStatutData {
		final String statut;
		final boolean expectedResult;

		TestStatutData(final String statut, final boolean expectedResult) {
			this.statut = statut;
			this.expectedResult = expectedResult;
		}
	}

}
