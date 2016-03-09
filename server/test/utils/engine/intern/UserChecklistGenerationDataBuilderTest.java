package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.ComplementReponses;
import utils.engine.data.LiquidateurReponses;
import utils.engine.data.MonthAndYear;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;

public class UserChecklistGenerationDataBuilderTest {

	private LiquidateurReponsesEvaluator liquidateurReponsesEvaluatorMock;

	private UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder;

	@Before
	public void setUp() {

		liquidateurReponsesEvaluatorMock = mock(LiquidateurReponsesEvaluator.class);

		userChecklistGenerationDataBuilder = new UserChecklistGenerationDataBuilder(liquidateurReponsesEvaluatorMock);
	}

	@Test
	public void should_fill_all_fields() {

		final MonthAndYear dateDepart = new MonthAndYear();
		final Regime[] regimes = new Regime[] { Regime.CNAV };
		final RegimeAligne[] regimesAlignes = new RegimeAligne[] { RegimeAligne.CNAV };
		final LiquidateurReponses liquidateurReponses = new LiquidateurReponses();
		final ComplementReponses complementReponses = new ComplementReponses();

		when(liquidateurReponsesEvaluatorMock.isConjointCollaborateur(liquidateurReponses)).thenReturn(true);
		when(liquidateurReponsesEvaluatorMock.isNSA(liquidateurReponses)).thenReturn(true);
		when(liquidateurReponsesEvaluatorMock.isSA(liquidateurReponses)).thenReturn(true);
		when(liquidateurReponsesEvaluatorMock.isCarriereAReconstituer(complementReponses)).thenReturn(true);

		final UserChecklistGenerationData userChecklistGenerationData = userChecklistGenerationDataBuilder.build(dateDepart, "973", regimes, regimesAlignes,
				liquidateurReponses, complementReponses, true, false, true);

		assertThat(userChecklistGenerationData.getDateDepart()).isSameAs(dateDepart);
		assertThat(userChecklistGenerationData.getDepartement()).isEqualTo("973");
		assertThat(userChecklistGenerationData.getRegimes()).isSameAs(regimes);
		assertThat(userChecklistGenerationData.getRegimesAlignes()).isSameAs(regimesAlignes);
		assertThat(userChecklistGenerationData.isParcoursDematIfExist()).isTrue();
		assertThat(userChecklistGenerationData.published).isFalse();

		verify(liquidateurReponsesEvaluatorMock).isConjointCollaborateur(liquidateurReponses);
		verify(liquidateurReponsesEvaluatorMock).isNSA(liquidateurReponses);
		verify(liquidateurReponsesEvaluatorMock).isSA(liquidateurReponses);
		verify(liquidateurReponsesEvaluatorMock).isCarriereAReconstituer(complementReponses);

		assertThat(userChecklistGenerationData.isConjointCollaborateur).isTrue();
		assertThat(userChecklistGenerationData.isNSA).isTrue();
		assertThat(userChecklistGenerationData.isSA).isTrue();
		assertThat(userChecklistGenerationData.isCarriereAReconstituer).isTrue();
		assertThat(userChecklistGenerationData.isCarriereLongue).isTrue();
	}

}
