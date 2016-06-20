package utils.engine;

import utils.WsUtils;
import utils.dao.CaisseDao;
import utils.dao.DaoChecklist;
import utils.dao.DaoFakeData;
import utils.dao.PeriodeDepartLegalDao;
import utils.db.HtmlCleaner;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.ChecklistNameSelector;
import utils.engine.intern.StepFormsDataProvider;
import utils.engine.intern.UserChecklistChapitreComputer;
import utils.engine.intern.UserChecklistChapitreFilter;
import utils.engine.intern.UserChecklistComputer;
import utils.engine.intern.UserChecklistConditionDelaiEvaluator;
import utils.engine.intern.UserChecklistConditionEvaluator;
import utils.engine.intern.UserChecklistDelaiComputer;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;
import utils.engine.intern.UserChecklistParcoursComputer;
import utils.engine.intern.UserChecklistVarsProvider;
import utils.engine.utils.AgeCalculator;
import utils.engine.utils.AgeLegalEvaluator;
import utils.engine.utils.DateProvider;
import utils.engine.utils.VariablesReplacerMustache;
import utils.wsinforetraite.InfoRetraite;
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
				new InfoRetraiteWsUr(
						new InfoRetraiteDecoder(),
						new InfoRetraiteConnector(wsUtils, new InfoRetraiteTokenRecuperator(wsUtils)));
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
				new DisplayerDepartureDate(stepFormsDataProvider),
				new DisplayerAdditionalQuestions(stepFormsDataProvider),
				new DisplayerChecklist(
						new UserChecklistGenerationDataBuilder(),
						new UserChecklistGenerator(
								new ChecklistNameSelector(),
								new DaoChecklist(new HtmlCleaner()),
								new UserChecklistComputer(
										new UserChecklistChapitreFilter(
												new UserChecklistConditionEvaluator(new UserChecklistConditionDelaiEvaluator(dateProvider))),
										new UserChecklistChapitreComputer(new UserChecklistDelaiComputer(dateProvider),
												new UserChecklistParcoursComputer(
														new UserChecklistVarsProvider(),
														new VariablesReplacerMustache())),
										new CaisseDao())),
						dateProvider,
						new CalculateurRegimeAlignes()),
				new DisplayerQuestionCarriereLongue(ageLegalEvaluator),
				new DisplayerSortieDepartInconnu(),
				new DisplayerSortiePenibilite());
	}
}
