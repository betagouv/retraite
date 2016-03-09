package integration;

import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import java.util.ArrayList;
import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import utils.engine.data.LiquidateurReponses;
import utils.engine.data.QuestionLiquidateur;
import utils.engine.data.enums.ChecklistName;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.intern.ChecklistNameSelector;
import utils.engine.intern.QuestionsLiquidateurBuilder;

public class VerificationChecklistNameSelectorTest {

	@Test
	@Ignore
	public void test() {

		final QuestionsLiquidateurBuilder questionsLiquidateurBuilder = new QuestionsLiquidateurBuilder();
		final ChecklistNameSelector checklistNameSelector = new ChecklistNameSelector();

		final List<RegimeAligne[]> combinaisonsRegimesAlignes = getCombinaisonsRegimesAlignes();

		for (final RegimeAligne[] regimesAlignes : combinaisonsRegimesAlignes) {
			final List<QuestionLiquidateur> listQuestions = questionsLiquidateurBuilder.buildQuestions(regimesAlignes);

			final List<LiquidateurReponses> combinaisonsReponses = createCombinaisonsReponses(listQuestions);

			for (final LiquidateurReponses reponses : combinaisonsReponses) {
				final ChecklistName checklistName = checklistNameSelector.select(regimesAlignes, reponses);
				Assertions.assertThat(checklistName).isNotNull();
			}

		}
	}

	private List<RegimeAligne[]> getCombinaisonsRegimesAlignes() {
		final List<RegimeAligne[]> combinaisonsRegimesAlignes = new ArrayList<>();
		combinaisonsRegimesAlignes.add(arr(CNAV, RSI));
		combinaisonsRegimesAlignes.add(arr(CNAV, MSA));
		combinaisonsRegimesAlignes.add(arr(MSA, RSI));
		return combinaisonsRegimesAlignes;
	}

	private RegimeAligne[] arr(final RegimeAligne... regimesAlignes) {
		return regimesAlignes;
	}

	private List<LiquidateurReponses> createCombinaisonsReponses(final List<QuestionLiquidateur> listQuestions) {
		// Créer le liste des valeurs de réponse possible, et pour chacune chercher de façon récursive les autres valeurs de réponses éventuellement possible,
		// pour chacune de ces réponses, dans le reste de la liste des questions
		final List<LiquidateurReponses> combinaisons = new ArrayList<>();
		return combinaisons;
	}
}
