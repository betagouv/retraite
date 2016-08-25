package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.engine.data.enums.RegimeAligne.CNAV;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import models.AnneesEtMois;
import utils.engine.data.RenderData;
import utils.engine.utils.AgeLegalEvaluator;

public class DisplayerSortieQuestionCarriereLongueTest {

	private DisplayerSortieQuestionCarriereLongue displayerSortieQuestionCarriereLongue;
	private AgeLegalEvaluator ageLegalEvaluatorMock;

	@Before
	public void setUp() throws Exception {
		ageLegalEvaluatorMock = mock(AgeLegalEvaluator.class);
		displayerSortieQuestionCarriereLongue = new DisplayerSortieQuestionCarriereLongue(ageLegalEvaluatorMock);
	}

	@Test
	public void step_display_question_carriere_longue() {

		final PostData postData = new PostData();
		postData.hidden_step = "displayDepartureDate";
		postData.hidden_naissance = "03/11/1958";
		postData.departMois = "11";
		postData.departAnnee = "2017";
		postData.hidden_liquidateur = CNAV;
		final RenderData renderData = new RenderData();

		final AnneesEtMois age = new AnneesEtMois(51, 6);
		final AnneesEtMois ageLegalPourPartir = new AnneesEtMois(58, 9);
		final String dateDepartPossible = "17/02/2021";

		when(ageLegalEvaluatorMock.calculeAgeADateDonnee("03/11/1958", "11", "2017")).thenReturn(age);
		when(ageLegalEvaluatorMock.getAgeLegalPourPartir("03/11/1958")).thenReturn(ageLegalPourPartir);
		when(ageLegalEvaluatorMock.calculeDateEnAjoutant("03/11/1958", ageLegalPourPartir)).thenReturn(dateDepartPossible);

		displayerSortieQuestionCarriereLongue.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displaySortieQuestionCarriereLongue");
		assertThat(renderData.hidden_departMois).isEqualTo("11");
		assertThat(renderData.hidden_departAnnee).isEqualTo("2017");
		final Map<String, Object> expectedExtras = new HashMap<String, Object>() {
			{
				put("age", age);
				put("ageLegalPourPartir", ageLegalPourPartir);
				put("dateDepartPossible", dateDepartPossible);
				put("urlAgeDepart", CNAV.urlAgeDepart);
				put("urlDispositifDepartAvantAgeLegal", CNAV.urlDispositifDepartAvantAgeLegal);
			}
		};
		assertThat(renderData.extras).isEqualTo(expectedExtras);
	}

}
