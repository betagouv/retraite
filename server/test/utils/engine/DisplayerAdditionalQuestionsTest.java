package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.engine.data.Departement;
import utils.engine.data.RenderData;
import utils.engine.intern.StepFormsDataProvider;

public class DisplayerAdditionalQuestionsTest {

	private final List<Departement> departementsMock = createListeDepartements();

	private DisplayerAdditionalQuestions displayerAdditionalQuestions;

	@Before
	public void setUp() throws Exception {

		final StepFormsDataProvider stepFormsDataProviderMock = mock(StepFormsDataProvider.class);
		when(stepFormsDataProviderMock.getListDepartements()).thenReturn(departementsMock);

		displayerAdditionalQuestions = new DisplayerAdditionalQuestions(stepFormsDataProviderMock);
	}

	@Test
	public void step_display_additionnal_questions() {

		final PostData postData = new PostData();
		postData.hidden_step = "displayDepartureDate";
		postData.departMois = "11";
		postData.departAnnee = "2017";
		postData.departInconnu = false;
		final RenderData renderData = new RenderData();

		displayerAdditionalQuestions.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displayAdditionalQuestions");
		assertThat(renderData.hidden_departMois).isEqualTo("11");
		assertThat(renderData.hidden_departAnnee).isEqualTo("2017");
		assertThat(renderData.departements).isSameAs(departementsMock);
	}

	@Test
	public void step_display_additionnal_questions_after_carriere_longue_with_hidden_data() {

		final PostData postData = new PostData();
		postData.hidden_step = "displaySortieQuestionCarriereLongue";
		postData.hidden_departMois = "11";
		postData.hidden_departAnnee = "2017";
		final RenderData renderData = new RenderData();

		displayerAdditionalQuestions.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displayAdditionalQuestions");
		assertThat(renderData.hidden_departMois).isEqualTo("11");
		assertThat(renderData.hidden_departAnnee).isEqualTo("2017");
		assertThat(renderData.departements).isSameAs(departementsMock);
	}

	private List<Departement> createListeDepartements() {
		final List<Departement> departements = new ArrayList<>();
		departements.add(new Departement("01", "Ain"));
		departements.add(new Departement("02", "Aisne"));
		return departements;
	}

}
