package utils.engine;

import static utils.engine.intern.UserChecklistComputerFactory.createUserChecklistComputer;

import utils.WsUtils;
import utils.dao.DaoChecklist;
import utils.dao.DaoFakeData;
import utils.dao.PeriodeDepartLegalDao;
import utils.db.HtmlCleaner;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.ChecklistNameSelector;
import utils.engine.intern.StepFormsDataProvider;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;
import utils.engine.utils.AgeCalculator;
import utils.engine.utils.AgeLegalEvaluator;
import utils.engine.utils.DateProvider;
import utils.wsinforetraite.InfoRetraite;
import utils.wsinforetraite.InfoRetraiteAnnuaireDai;
import utils.wsinforetraite.InfoRetraiteBdd;
import utils.wsinforetraite.InfoRetraiteConnector;
import utils.wsinforetraite.InfoRetraiteDecoder;
import utils.wsinforetraite.InfoRetraiteTokenRecuperator;
import utils.wsinforetraite.InfoRetraiteWsUr;

public class RetraiteEngineFactory {

	public static RetraiteEngine create(final boolean testMode) {
		final DateProvider dateProvider = new DateProvider();
		final WsUtils wsUtils = new WsUtils();

		// @formatter:off
		final InfoRetraite infoRetraite =
			testMode ?
				new InfoRetraiteBdd()
			:
				new InfoRetraiteAnnuaireDai();
				/*new InfoRetraiteWsUr(
						new InfoRetraiteDecoder(),
						new InfoRetraiteConnector(wsUtils, new InfoRetraiteTokenRecuperator(wsUtils)));*/
		// @formatter:on

		final AgeLegalEvaluator ageLegalEvaluator = new AgeLegalEvaluator(new PeriodeDepartLegalDao());
		final StepFormsDataProvider stepFormsDataProvider = new StepFormsDataProvider(dateProvider);
		return new RetraiteEngine(
				// new InfoRetraiteBdd(),
				infoRetraite,

				new CalculateurRegimeAlignes(),

				new DaoFakeData(),
				new AgeCalculator(dateProvider),
				ageLegalEvaluator,
				new DisplayerLiquidateurQuestions(
						new QuestionSolverA(),
						new QuestionSolverB(),
						new QuestionSolverC(),
						new QuestionSolverD(),
						new QuestionSolverE(),
						new QuestionSolverF()),
				new DisplayerDepartureDate(stepFormsDataProvider, ageLegalEvaluator),
				new DisplayerAdditionalQuestions(stepFormsDataProvider),
				new DisplayerChecklist(
						new UserChecklistGenerationDataBuilder(),
						new UserChecklistGenerator(
								new ChecklistNameSelector(),
								new DaoChecklist(new HtmlCleaner()),
								createUserChecklistComputer(dateProvider)),
						dateProvider,
						new CalculateurRegimeAlignes(),
						new StepFormsDataProvider(dateProvider)),
				new DisplayerSortieQuestionCarriereLongue(ageLegalEvaluator),
				new DisplayerSortieDepartInconnu(),
				new DisplayerSortiePenibilite(),
				new DisplayerSortieTropJeune());
	}
}
