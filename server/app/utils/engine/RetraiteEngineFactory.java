package utils.engine;

import utils.dao.CaisseDao;
import utils.dao.DaoChecklist;
import utils.dao.DaoFakeData;
import utils.dao.PeriodeDepartLegalDao;
import utils.db.HtmlCleaner;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.ChecklistNameSelector;
import utils.engine.intern.LiquidateurReponsesEvaluator;
import utils.engine.intern.QuestionComplementairesEvaluator;
import utils.engine.intern.QuestionsComplementairesBuilder;
import utils.engine.intern.QuestionsLiquidateurBuilder;
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
import utils.wsinforetraite.InfoRetraiteBdd;

public class RetraiteEngineFactory {

	public static RetraiteEngine create() {
		final DateProvider dateProvider = new DateProvider();
		final LiquidateurReponsesEvaluator liquidateurReponsesEvaluator = new LiquidateurReponsesEvaluator();
		return new RetraiteEngine(
				new StepFormsDataProvider(
						dateProvider),
				new InfoRetraiteBdd(),
				/*
				 * new InfoRetraiteReal( new InfoRetraiteDecoder(), new InfoRetraiteConnector(new WsUtils())),
				 */
				new CalculateurRegimeAlignes(),
				new QuestionsLiquidateurBuilder(),
				new DaoFakeData(),
				new AgeCalculator(
						dateProvider),
				new AgeLegalEvaluator(
						new PeriodeDepartLegalDao()),
				new DisplayerDepartureDate(new StepFormsDataProvider(dateProvider)),
				new DisplayerAdditionalQuestions(new QuestionsComplementairesBuilder()), new DisplayerChecklist(
						new QuestionComplementairesEvaluator(),
						new UserChecklistGenerationDataBuilder(liquidateurReponsesEvaluator),
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
						dateProvider, new CalculateurRegimeAlignes()));
	}
}
