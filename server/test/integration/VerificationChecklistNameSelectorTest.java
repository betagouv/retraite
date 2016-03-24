package integration;

public class VerificationChecklistNameSelectorTest {

	// @Test
	// @Ignore
	// public void test() {
	//
	// final QuestionsLiquidateurBuilder questionsLiquidateurBuilder = new QuestionsLiquidateurBuilder();
	// final ChecklistNameSelector checklistNameSelector = new ChecklistNameSelector();
	//
	// final List<RegimeAligne[]> combinaisonsRegimesAlignes = getCombinaisonsRegimesAlignes();
	//
	// for (final RegimeAligne[] regimesAlignes : combinaisonsRegimesAlignes) {
	// final List<QuestionLiquidateur> listQuestions = questionsLiquidateurBuilder.buildQuestions(regimesAlignes);
	//
	// final List<LiquidateurReponses> combinaisonsReponses = createCombinaisonsReponses(listQuestions);
	//
	// for (final LiquidateurReponses reponses : combinaisonsReponses) {
	// final ChecklistName checklistName = checklistNameSelector.select(regimesAlignes, reponses);
	// Assertions.assertThat(checklistName).isNotNull();
	// }
	//
	// }
	// }
	//
	// private List<RegimeAligne[]> getCombinaisonsRegimesAlignes() {
	// final List<RegimeAligne[]> combinaisonsRegimesAlignes = new ArrayList<>();
	// combinaisonsRegimesAlignes.add(arr(CNAV, RSI));
	// combinaisonsRegimesAlignes.add(arr(CNAV, MSA));
	// combinaisonsRegimesAlignes.add(arr(MSA, RSI));
	// return combinaisonsRegimesAlignes;
	// }
	//
	// private RegimeAligne[] arr(final RegimeAligne... regimesAlignes) {
	// return regimesAlignes;
	// }
	//
	// private List<LiquidateurReponses> createCombinaisonsReponses(final List<QuestionLiquidateur> listQuestions) {
	// // Créer le liste des valeurs de réponse possible, et pour chacune chercher de façon récursive les autres valeurs de réponses éventuellement possible,
	// // pour chacune de ces réponses, dans le reste de la liste des questions
	// final List<LiquidateurReponses> combinaisons = new ArrayList<>();
	// return combinaisons;
	// }
}
