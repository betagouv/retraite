package utils.engine;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.JsonUtils.toJson;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.engine.data.LiquidateurReponses;
import utils.engine.data.RenderData;
import utils.engine.data.ValueAndText;
import utils.engine.intern.StepFormsDataProvider;

public class DisplayerDepartureDateTest {

	private final List<ValueAndText> listeMoisAvecPremierMock = asList(vet("1", "1er Janvier"), vet("12", "1er Décembre"));
	private final List<String> listeAnneesDepartMock = asList("2015", "2016");

	private DisplayerDepartureDate displayerDepartureDate;

	@Before
	public void setUp() throws Exception {
		final StepFormsDataProvider stepFormsDataProviderMock = mock(StepFormsDataProvider.class);
		when(stepFormsDataProviderMock.getListMoisAvecPremier()).thenReturn(listeMoisAvecPremierMock);
		when(stepFormsDataProviderMock.getListAnneesDepart()).thenReturn(listeAnneesDepartMock);

		displayerDepartureDate = new DisplayerDepartureDate(stepFormsDataProviderMock);
	}

	@Test
	public void step_display_departure_date_without_questions() {

		final PostData postData = new PostData();
		final RenderData renderData = new RenderData();
		final String regimes = "CNAV,CCMSA,AGIRC ARRCO";

		displayerDepartureDate.fillData(postData, renderData, regimes);

		assertThat(renderData.hidden_regimes).isEqualTo(regimes);
		assertThat(renderData.listeMoisAvecPremier).isSameAs(listeMoisAvecPremierMock);
		assertThat(renderData.listeAnneesDepart).isSameAs(listeAnneesDepartMock);
	}

	@Test
	public void step_display_departure_date() {

		final LiquidateurReponses liquidateurReponse = RetraiteEngineTest.createLiquidateurReponses();
		final String liquidateurReponseJsonStr = toJson(liquidateurReponse.getReponses());

		final PostData postData = new PostData();
		postData.liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		final RenderData renderData = new RenderData();

		displayerDepartureDate.fillData(postData, renderData, null);

		assertThat(renderData.hidden_regimes).isNull();
		assertThat(renderData.listeMoisAvecPremier).isSameAs(listeMoisAvecPremierMock);
		assertThat(renderData.listeAnneesDepart).isSameAs(listeAnneesDepartMock);
	}

	private ValueAndText vet(final String value, final String text) {
		return new ValueAndText(value, text);
	}

}
