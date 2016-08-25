package utils.engine;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import models.AnneesEtMois;
import utils.engine.data.RenderData;
import utils.engine.data.ValueAndText;
import utils.engine.intern.StepFormsDataProvider;
import utils.engine.utils.AgeLegalEvaluator;

public class DisplayerDepartureDateTest {

	private final List<ValueAndText> listeMoisAvecPremierMock = asList(vet("1", "1er Janvier"), vet("12", "1er Décembre"));
	private final List<String> listeAnneesDepartMock = asList("2015", "2016");
	private final AnneesEtMois ageLegalPourPartir = new AnneesEtMois(58, 9);
	private final String dateDepartPossible = "17/02/2021";

	private DisplayerDepartureDate displayerDepartureDate;

	@Before
	public void setUp() throws Exception {
		final StepFormsDataProvider stepFormsDataProviderMock = mock(StepFormsDataProvider.class);
		when(stepFormsDataProviderMock.getListMoisAvecPremier()).thenReturn(listeMoisAvecPremierMock);
		when(stepFormsDataProviderMock.getListAnneesDepart()).thenReturn(listeAnneesDepartMock);

		final AgeLegalEvaluator ageLegalEvaluatorMock = mock(AgeLegalEvaluator.class);
		when(ageLegalEvaluatorMock.getAgeLegalPourPartir("03/11/1958")).thenReturn(ageLegalPourPartir);
		when(ageLegalEvaluatorMock.calculeDateEnAjoutant("03/11/1958", ageLegalPourPartir)).thenReturn(dateDepartPossible);

		displayerDepartureDate = new DisplayerDepartureDate(stepFormsDataProviderMock, ageLegalEvaluatorMock);
	}

	@Test
	public void step_display_departure_date_without_questions() {

		final PostData postData = new PostData();
		postData.naissance = "03/11/1958";
		final RenderData renderData = new RenderData();
		final String regimes = "CNAV,CCMSA,AGIRC ARRCO";

		displayerDepartureDate.fillData(postData, renderData, regimes);

		assertThat(renderData.hidden_regimes).isEqualTo(regimes);
		assertThat(renderData.listeMoisAvecPremier).isSameAs(listeMoisAvecPremierMock);
		assertThat(renderData.listeAnneesDepart).isSameAs(listeAnneesDepartMock);
		final Map<String, Object> expectedExtras = new HashMap<String, Object>() {
			{
				put("ageLegalPourPartir", ageLegalPourPartir);
				put("dateDepartPossible", dateDepartPossible);
			}
		};
		assertThat(renderData.extras).isEqualTo(expectedExtras);
	}

	@Test
	public void step_display_departure_date() {

		final PostData postData = new PostData();
		postData.hidden_naissance = "03/11/1958";
		postData.liquidateurReponseJsonStr = null;
		final RenderData renderData = new RenderData();

		displayerDepartureDate.fillData(postData, renderData, null);

		assertThat(renderData.hidden_regimes).isNull();
		assertThat(renderData.listeMoisAvecPremier).isSameAs(listeMoisAvecPremierMock);
		assertThat(renderData.listeAnneesDepart).isSameAs(listeAnneesDepartMock);
		final Map<String, Object> expectedExtras = new HashMap<String, Object>() {
			{
				put("ageLegalPourPartir", ageLegalPourPartir);
				put("dateDepartPossible", dateDepartPossible);
			}
		};
		assertThat(renderData.extras).isEqualTo(expectedExtras);
	}

	private ValueAndText vet(final String value, final String text) {
		return new ValueAndText(value, text);
	}

}
