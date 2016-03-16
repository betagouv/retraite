package utils.engine;

import controllers.data.PostData;
import play.Logger;
import utils.DateUtils;
import utils.RetraiteException;
import utils.engine.data.ComplementReponses;
import utils.engine.data.LiquidateurReponses;
import utils.engine.data.MonthAndYear;
import utils.engine.data.RenderData;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.QuestionComplementairesEvaluator;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;
import utils.engine.utils.DateProvider;

public class DisplayerChecklist {

	private final QuestionComplementairesEvaluator questionComplementairesEvaluator;
	private final UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder;
	private final UserChecklistGenerator userChecklistGenerator;
	private final DateProvider dateProvider;
	private final CalculateurRegimeAlignes calculateurRegimeAlignes;

	public DisplayerChecklist(final QuestionComplementairesEvaluator questionComplementairesEvaluator,
			final UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder, final UserChecklistGenerator userChecklistGenerator,
			final DateProvider dateProvider, final CalculateurRegimeAlignes calculateurRegimeAlignes) {
		this.questionComplementairesEvaluator = questionComplementairesEvaluator;
		this.userChecklistGenerationDataBuilder = userChecklistGenerationDataBuilder;
		this.userChecklistGenerator = userChecklistGenerator;
		this.dateProvider = dateProvider;
		this.calculateurRegimeAlignes = calculateurRegimeAlignes;
	}

	public void fillData(final PostData data, final RenderData renderData) {
		final RegimeAligne[] regimesAlignes = calculateurRegimeAlignes.getRegimesAlignes(data.hidden_regimes);
		final LiquidateurReponses liquidateurReponses = LiquidateurReponses.retrieveLiquidateurReponsesFromJson(data.hidden_liquidateurReponseJsonStr);
		final ComplementReponses complementReponses = ComplementReponses.retrieveComplementReponsesFromJson(data.complementReponseJsonStr);
		final Regime[] regimes = Regime.fromStringList(data.hidden_regimes);
		final boolean isParcoursDemat = questionComplementairesEvaluator.isParcoursDemat(complementReponses);
		final MonthAndYear dateDepart = new MonthAndYear(data.hidden_departMois, data.hidden_departAnnee);
		final UserChecklistGenerationData userChecklistGenerationData = userChecklistGenerationDataBuilder.build(dateDepart, data.hidden_departement,
				regimes, regimesAlignes, liquidateurReponses, complementReponses, isParcoursDemat, true, data.hidden_attestationCarriereLongue);
		renderData.hidden_step = "displayCheckList";
		renderData.hidden_complementReponseJsonStr = data.complementReponseJsonStr;
		try {
			renderData.userChecklist = userChecklistGenerator.generate(userChecklistGenerationData, liquidateurReponses);
		} catch (final RetraiteException e) {
			Logger.error(e, "Impossible de déterminer le régime liquidateur");
			renderData.errorMessage = "Désolé, impossible de déterminer le régime liquidateur...";
		}
		renderData.dateGeneration = DateUtils.format(dateProvider.getCurrentDate());
	}

}
