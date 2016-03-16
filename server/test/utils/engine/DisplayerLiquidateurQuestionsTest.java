package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.engine.data.QuestionLiquidateur;
import utils.engine.data.RenderData;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.intern.QuestionsLiquidateurBuilder;

public class DisplayerLiquidateurQuestionsTest {

	private final List<QuestionLiquidateur> questionsLiquidateur = createQuestionsLiquidateur();
	private final String regimes = "CNAV,CCMSA,AGIRC ARRCO";

	private QuestionsLiquidateurBuilder questionsLiquidateurBuilderMock;

	private DisplayerLiquidateurQuestions displayerLiquidateurQuestions;

	@Before
	public void setUp() throws Exception {
		questionsLiquidateurBuilderMock = mock(QuestionsLiquidateurBuilder.class);
		when(questionsLiquidateurBuilderMock.buildQuestions(new RegimeAligne[] { CNAV, MSA })).thenReturn(questionsLiquidateur);

		displayerLiquidateurQuestions = new DisplayerLiquidateurQuestions(questionsLiquidateurBuilderMock);
	}

	@Test
	public void step_display_liquidateur_questions_si_2_regimes() {

		// Step : getUserData --> displayLiquidateurQuestions

		final PostData postData = new PostData();
		final RenderData renderData = new RenderData();

		displayerLiquidateurQuestions.fillData(postData, renderData, regimes, new RegimeAligne[] { CNAV, MSA });

		assertThat(renderData.hidden_step).isEqualTo("displayLiquidateurQuestions");
		assertThat(renderData.hidden_regimes).isEqualTo(regimes);
		assertThat(renderData.questionsLiquidateur).isSameAs(questionsLiquidateur);
	}

	private List<QuestionLiquidateur> createQuestionsLiquidateur() {
		final ArrayList<QuestionLiquidateur> questions = new ArrayList<>();
		questions.add(new QuestionLiquidateur());
		questions.add(new QuestionLiquidateur());
		return questions;
	}

}
