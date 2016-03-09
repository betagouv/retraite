package utils.engine;

import utils.dao.DaoFakeData;
import utils.dao.PeriodeDepartLegalDao;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.LiquidateurReponsesEvaluator;
import utils.engine.intern.QuestionComplementairesEvaluator;
import utils.engine.intern.QuestionsComplementairesBuilder;
import utils.engine.intern.QuestionsLiquidateurBuilder;
import utils.engine.intern.StepFormsDataProvider;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGeneratorFactory;
import utils.engine.utils.AgeCalculator;
import utils.engine.utils.AgeLegalEvaluator;
import utils.engine.utils.DateProvider;
import utils.wsinforetraite.InfoRetraiteBdd;

public class RetraiteEngineFactory {

	public static RetraiteEngine create() {
		final DateProvider dateProvider = new DateProvider();
		return new RetraiteEngine(
				new StepFormsDataProvider(
						dateProvider),
				new InfoRetraiteBdd(),
				/*
				 * new InfoRetraiteReal( new InfoRetraiteDecoder(), new InfoRetraiteConnector(new WsUtils())),
				 */
				UserChecklistGeneratorFactory.createUserChecklistGenerator(),
				new CalculateurRegimeAlignes(),
				new QuestionsLiquidateurBuilder(),
				new QuestionsComplementairesBuilder(),
				new DaoFakeData(),
				new UserChecklistGenerationDataBuilder(
						new LiquidateurReponsesEvaluator()),
				new QuestionComplementairesEvaluator(),
				new AgeCalculator(
						dateProvider),
				new AgeLegalEvaluator(
						new PeriodeDepartLegalDao()),
				dateProvider);
	}
}
