package utils.engine.intern;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.UserStatus.STATUS_CONJOINT_COLLABORATEUR;
import static utils.engine.data.enums.UserStatus.STATUS_NSA;
import static utils.engine.data.enums.UserStatus.STATUS_SA;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.MonthAndYear;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class UserChecklistGenerationDataBuilderTest {

	private UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder;

	@Before
	public void setUp() {
		userChecklistGenerationDataBuilder = new UserChecklistGenerationDataBuilder();
	}

	@Test
	public void should_fill_all_fields() {

		final MonthAndYear dateDepart = new MonthAndYear();
		final Regime[] regimes = new Regime[] { Regime.CNAV };
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RegimeAligne.CNAV };
		final RegimeAligne regimeLiquidateur = CNAV;
		final List<UserStatus> userStatus = asList(STATUS_NSA, STATUS_CONJOINT_COLLABORATEUR, STATUS_SA);

		final UserChecklistGenerationData userChecklistGenerationData = userChecklistGenerationDataBuilder.build(dateDepart, "973", regimes, regimesAlignes,
				regimeLiquidateur, true, false, true, userStatus);

		assertThat(userChecklistGenerationData.getDateDepart()).isSameAs(dateDepart);
		assertThat(userChecklistGenerationData.getDepartement()).isEqualTo("973");
		assertThat(userChecklistGenerationData.getRegimes()).isSameAs(regimes);
		assertThat(userChecklistGenerationData.getRegimesAlignes()).isSameAs(regimesAlignes);
		assertThat(userChecklistGenerationData.isParcoursDematIfExist()).isTrue();
		assertThat(userChecklistGenerationData.published).isFalse();

		// [XN-29/03/2016-En attendant de remettre les questions complémentaires, on force l'affichage des chapitres]
		// verify(liquidateurReponsesEvaluatorMock).isCarriereAReconstituer(complementReponses);

		assertThat(userChecklistGenerationData.isConjointCollaborateur).isTrue();
		assertThat(userChecklistGenerationData.isNSA).isTrue();
		assertThat(userChecklistGenerationData.isSA).isTrue();
		assertThat(userChecklistGenerationData.isCarriereAReconstituer).isTrue();
		assertThat(userChecklistGenerationData.isCarriereLongue).isTrue();
	}

	@Test
	public void should_fill_all_fields_even_if_userStatus_is_null() {

		final MonthAndYear dateDepart = new MonthAndYear();
		final Regime[] regimes = new Regime[] { Regime.CNAV };
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RegimeAligne.CNAV };
		final RegimeAligne regimeLiquidateur = CNAV;
		final List<UserStatus> userStatus = null;

		final UserChecklistGenerationData userChecklistGenerationData = userChecklistGenerationDataBuilder.build(dateDepart, "973", regimes, regimesAlignes,
				regimeLiquidateur, true, false, true, userStatus);

		assertThat(userChecklistGenerationData.isConjointCollaborateur).isFalse();
		assertThat(userChecklistGenerationData.isNSA).isFalse();
		assertThat(userChecklistGenerationData.isSA).isFalse();
		assertThat(userChecklistGenerationData.isCarriereAReconstituer).isTrue();
		assertThat(userChecklistGenerationData.isCarriereLongue).isTrue();
	}

}
