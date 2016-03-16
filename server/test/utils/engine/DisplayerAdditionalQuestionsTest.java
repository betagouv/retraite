package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.engine.data.QuestionComplementaire;
import utils.engine.data.RenderData;
import utils.engine.intern.QuestionsComplementairesBuilder;

public class DisplayerAdditionalQuestionsTest {

	private final List<QuestionComplementaire> questionsComplementaire = RetraiteEngineTest.createQuestionsComplementaires();

	private DisplayerAdditionalQuestions displayerAdditionalQuestions;

	@Before
	public void setUp() throws Exception {

		final QuestionsComplementairesBuilder questionsComplementairesBuilderMock = mock(QuestionsComplementairesBuilder.class);
		when(questionsComplementairesBuilderMock.buildQuestions()).thenReturn(questionsComplementaire);

		displayerAdditionalQuestions = new DisplayerAdditionalQuestions(questionsComplementairesBuilderMock);
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
		assertThat(renderData.questionsComplementaires).isSameAs(questionsComplementaire);
	}

	@Test
	public void step_display_additionnal_questions_after_carriere_longue() {

		final PostData postData = new PostData();
		postData.hidden_step = "displayQuestionCarriereLongue";
		postData.hidden_departMois = "11";
		postData.hidden_departAnnee = "2017";
		final RenderData renderData = new RenderData();

		displayerAdditionalQuestions.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displayAdditionalQuestions");
		assertThat(renderData.hidden_departMois).isEqualTo("11");
		assertThat(renderData.hidden_departAnnee).isEqualTo("2017");
		assertThat(renderData.questionsComplementaires).isSameAs(questionsComplementaire);
	}

}
