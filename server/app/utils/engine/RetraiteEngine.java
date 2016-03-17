package utils.engine;

import static java.util.Arrays.asList;

import java.lang.reflect.Field;

import controllers.data.PostData;
import play.Logger;
import utils.RetraiteException;
import utils.dao.DaoFakeData;
import utils.engine.data.CommonExchangeData;
import utils.engine.data.RenderData;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.StepFormsDataProvider;
import utils.engine.utils.AgeCalculator;
import utils.engine.utils.AgeLegalEvaluator;
import utils.wsinforetraite.InfoRetraite;
import utils.wsinforetraite.InfoRetraiteResult;

public class RetraiteEngine {

	private final StepFormsDataProvider stepFormsDataProvider;
	private final InfoRetraite infoRetraite;
	private final CalculateurRegimeAlignes calculateurRegimeAlignes;
	private final DaoFakeData daoFakeData;
	private final AgeCalculator ageCalculator;
	private final AgeLegalEvaluator ageLegalEvaluator;
	private final DisplayerLiquidateurQuestions displayerLiquidateurQuestions;
	private final DisplayerDepartureDate displayerDepartureDate;
	private final DisplayerAdditionalQuestions displayerAdditionalQuestions;
	private final DisplayerChecklist displayerChecklist;

	public RetraiteEngine(
			final StepFormsDataProvider stepFormsDataProvider,
			final InfoRetraite infoRetraite,
			final CalculateurRegimeAlignes calculateurRegimeAlignes,
			final DaoFakeData daoFakeData,
			final AgeCalculator ageCalculator,
			final AgeLegalEvaluator ageLegalEvaluator,
			final DisplayerLiquidateurQuestions displayerLiquidateurQuestions,
			final DisplayerDepartureDate displayerDepartureDate,
			final DisplayerAdditionalQuestions displayerAdditionalQuestions,
			final DisplayerChecklist displayerChecklist) {

		this.stepFormsDataProvider = stepFormsDataProvider;
		this.infoRetraite = infoRetraite;
		this.calculateurRegimeAlignes = calculateurRegimeAlignes;
		this.daoFakeData = daoFakeData;
		this.ageCalculator = ageCalculator;
		this.ageLegalEvaluator = ageLegalEvaluator;
		this.displayerLiquidateurQuestions = displayerLiquidateurQuestions;
		this.displayerDepartureDate = displayerDepartureDate;
		this.displayerAdditionalQuestions = displayerAdditionalQuestions;
		this.displayerChecklist = displayerChecklist;
	}

	public RenderData processToNextStep(final PostData data) {
		final RenderData renderData = new RenderData();

		if (data != null && data.hidden_step == null) {
			throw new RetraiteException("Situation anormale : pas d'étape (step) pour le traitement");
		}

		if (data == null) {
			return displayWelcome(renderData);
		}

		copyHiddenFields(data, renderData);

		if (data.hidden_step.equals("welcome")) {
			return displayGetUserData(renderData);
		} else if (data.hidden_step.equals("getUserData")) {
			Logger.info("Connexion d'un usager : nom=" + data.nom + " , nir=" + data.nir + " , naissance=" + data.naissance);
			if (ageCalculator.getAge(data.naissance) < 55) {
				return displaySortieTropJeune(renderData);
			}
			final String regimes = infoRetraite.retrieveRegimes(data.nom, data.nir, data.naissance);
			if (regimes.isEmpty()) {
				renderData.errorMessage = "Désolé, aucune information n'a pu être trouvée avec les données saisies";
				return displayGetUserData(renderData);
			}
			final RegimeAligne[] regimesAlignes = calculateurRegimeAlignes.getRegimesAlignes(regimes);
			if (regimesAlignes.length == 0) {
				return displaySortieAucunRegimeDeBaseAligne(renderData);
			}
			if (regimesAlignes.length >= 2) {
				displayerLiquidateurQuestions.fillData(data, renderData, regimes, regimesAlignes);
				return renderData;
			}
			displayerDepartureDate.fillData(data, renderData, regimes);
		} else if (data.hidden_step.equals("displayLiquidateurQuestions")) {
			final RegimeAligne[] regimesAlignes = calculateurRegimeAlignes.getRegimesAlignes(data.hidden_regimes);
			displayerLiquidateurQuestions.fillData(data, renderData, data.hidden_regimes, regimesAlignes);
			if (renderData.hidden_liquidateurStep == null) {
				displayerDepartureDate.fillData(data, renderData, null);
			}
		} else if (data.hidden_step.equals("displayDepartureDate")) {
			if (data.departInconnu) {
				return displaySortieDepartInconnu(renderData);
			}
			if (!ageLegalEvaluator.isAgeLegal(data.hidden_naissance, data.departMois, data.departAnnee)) {
				return displayQuestionCarriereLongue(renderData, data.departMois, data.departAnnee);
			}
			displayerAdditionalQuestions.fillData(data, renderData);
		} else if (data.hidden_step.equals("displayQuestionCarriereLongue")) {
			renderData.hidden_attestationCarriereLongue = true;
			displayerAdditionalQuestions.fillData(data, renderData);
		} else if (data.hidden_step.equals("displayAdditionalQuestions") || data.hidden_step.equals("displayCheckList")) {
			displayerChecklist.fillData(data, renderData);
		} else {
			throw new RetraiteException("Situation anormale : l'étape '" + data.hidden_step + "' pour le traitement");
		}
		return renderData;
	}

	private RenderData displaySortieTropJeune(final RenderData renderData) {
		renderData.hidden_step = "displaySortieTropJeune";
		return renderData;
	}

	private RenderData displaySortieAucunRegimeDeBaseAligne(final RenderData renderData) {
		final InfoRetraiteResult allInformations = infoRetraite.retrieveAllInformations(renderData.hidden_nom, renderData.hidden_nir,
				renderData.hidden_naissance);
		renderData.hidden_step = "displaySortieAucunRegimeDeBaseAligne";
		renderData.regimesInfos = asList(allInformations.regimes);
		return renderData;
	}

	private RenderData displaySortieDepartInconnu(final RenderData renderData) {
		renderData.hidden_step = "displaySortieDepartInconnu";
		return renderData;
	}

	private RenderData displayQuestionCarriereLongue(final RenderData renderData, final String departMois, final String departAnnee) {
		renderData.hidden_step = "displayQuestionCarriereLongue";
		renderData.hidden_departMois = departMois;
		renderData.hidden_departAnnee = departAnnee;
		return renderData;
	}

	private RenderData displayWelcome(final RenderData renderData) {
		renderData.hidden_step = "welcome";
		return renderData;
	}

	private RenderData displayGetUserData(final RenderData renderData) {
		renderData.hidden_step = "getUserData";
		renderData.departements = stepFormsDataProvider.getListDepartements();

		// Temporaire pour afficher les DataRegime tant qu'on ne peut pas interroger le WS info-retraite
		renderData.fakeData = daoFakeData.findAll();
		return renderData;
	}

	private void copyHiddenFields(final PostData data, final RenderData renderData) {
		try {
			for (final Field field : CommonExchangeData.class.getFields()) {
				final String fieldName = field.getName();
				if (fieldName.startsWith("hidden_") && !fieldName.toLowerCase().contains("step")) {
					final Object hiddenData = field.get(data);
					final Object noHiddenData = tryToGetNoHiddenData(data, fieldName);
					field.set(renderData, firstNotNull(noHiddenData, hiddenData));
				}
			}
		} catch (final Exception e) {
			throw new RetraiteException("Error copying hidden fields", e);
		}
	}

	private Object tryToGetNoHiddenData(final PostData data, final String fieldName) throws IllegalAccessException {
		final String noHiddenFieldName = fieldName.substring("hidden_".length());
		try {
			final Field fieldInPostData = PostData.class.getField(noHiddenFieldName);
			return fieldInPostData.get(data);
		} catch (final NoSuchFieldException e) {
			return null;
		}
	}

	private Object firstNotNull(final Object... objects) {
		for (final Object object : objects) {
			if (object != null) {
				return object;
			}
		}
		return null;
	}

}
