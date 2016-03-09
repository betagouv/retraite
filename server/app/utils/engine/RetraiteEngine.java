package utils.engine;

import static java.util.Arrays.asList;

import java.lang.reflect.Field;

import controllers.data.PostData;
import play.Logger;
import utils.DateUtils;
import utils.RetraiteException;
import utils.dao.DaoFakeData;
import utils.engine.data.CommonExchangeData;
import utils.engine.data.ComplementReponses;
import utils.engine.data.LiquidateurReponses;
import utils.engine.data.MonthAndYear;
import utils.engine.data.RenderData;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.QuestionComplementairesEvaluator;
import utils.engine.intern.QuestionsComplementairesBuilder;
import utils.engine.intern.QuestionsLiquidateurBuilder;
import utils.engine.intern.StepFormsDataProvider;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;
import utils.engine.utils.AgeCalculator;
import utils.engine.utils.AgeLegalEvaluator;
import utils.engine.utils.DateProvider;
import utils.wsinforetraite.InfoRetraite;
import utils.wsinforetraite.InfoRetraiteResult;

public class RetraiteEngine {

	private final StepFormsDataProvider stepFormsDataProvider;
	private final InfoRetraite infoRetraite;
	private final UserChecklistGenerator userChecklistGenerator;
	private final CalculateurRegimeAlignes calculateurRegimeAlignes;
	private final QuestionComplementairesEvaluator questionComplementairesEvaluator;
	private final QuestionsLiquidateurBuilder questionsLiquidateurBuilder;
	private final DaoFakeData daoFakeData;
	private final UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder;
	private final QuestionsComplementairesBuilder questionsComplementairesBuilder;
	private final AgeCalculator ageCalculator;
	private final AgeLegalEvaluator ageLegalEvaluator;
	private final DateProvider dateProvider;

	public RetraiteEngine(final StepFormsDataProvider stepFormsDataProvider, final InfoRetraite infoRetraite,
			final UserChecklistGenerator userChecklistProvider, final CalculateurRegimeAlignes calculateurRegimeAlignes,
			final QuestionsLiquidateurBuilder questionsLiquidateurBuilder, final QuestionsComplementairesBuilder questionsComplementairesBuilder,
			final DaoFakeData daoFakeData, final UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder,
			final QuestionComplementairesEvaluator questionComplementairesEvaluator, final AgeCalculator ageCalculator,
			final AgeLegalEvaluator ageLegalEvaluator, final DateProvider dateProvider) {

		this.stepFormsDataProvider = stepFormsDataProvider;
		this.infoRetraite = infoRetraite;
		this.userChecklistGenerator = userChecklistProvider;
		this.calculateurRegimeAlignes = calculateurRegimeAlignes;
		this.questionsLiquidateurBuilder = questionsLiquidateurBuilder;
		this.questionsComplementairesBuilder = questionsComplementairesBuilder;
		this.daoFakeData = daoFakeData;
		this.userChecklistGenerationDataBuilder = userChecklistGenerationDataBuilder;
		this.questionComplementairesEvaluator = questionComplementairesEvaluator;
		this.ageCalculator = ageCalculator;
		this.ageLegalEvaluator = ageLegalEvaluator;
		this.dateProvider = dateProvider;
	}

	public RenderData processToNextStep(final PostData data) {
		final RenderData renderData = new RenderData();

		if (data != null && data.hidden_step == null) {
			throw new RetraiteException("Situation anormale : pas d'étape (step) pour le traitement");
		}

		if (data == null) {
			return displayWelcome(renderData);
		}

		copyHiddenFields(renderData, data);

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
			if (regimesAlignes.length == 2) {
				return displayLiquidateurQuestions(data, renderData, regimes, regimesAlignes);
			}
			return displayDepartureDateWithoutLiquidateurQuestions(data, renderData, regimes);
		} else if (data.hidden_step.equals("displayLiquidateurQuestions")) {
			return displayDepartureDateWithLiquidateurQuestions(renderData, data);
		} else if (data.hidden_step.equals("displayDepartureDate")) {
			if (data.departInconnu) {
				return displaySortieDepartInconnu(renderData);
			}
			if (!ageLegalEvaluator.isAgeLegal(data.hidden_naissance, data.departMois, data.departAnnee)) {
				return displayQuestionCarriereLongue(renderData, data.departMois, data.departAnnee);
			}
			return displayAdditionalQuestions(renderData, data.departMois, data.departAnnee);
		} else if (data.hidden_step.equals("displayQuestionCarriereLongue")) {
			return displayAdditionalQuestionsAvecCarriereLongue(data, renderData);
		} else if (data.hidden_step.equals("displayAdditionalQuestions") || data.hidden_step.equals("displayCheckList")) {
			final MonthAndYear dateDepart = new MonthAndYear(data.hidden_departMois, data.hidden_departAnnee);
			final RegimeAligne[] regimesAlignes = calculateurRegimeAlignes.getRegimesAlignes(data.hidden_regimes);
			final LiquidateurReponses liquidateurReponses = LiquidateurReponses.retrieveLiquidateurReponsesFromJson(data.hidden_liquidateurReponseJsonStr);
			final ComplementReponses complementReponses = ComplementReponses.retrieveComplementReponsesFromJson(data.complementReponseJsonStr);
			final Regime[] regimes = Regime.fromStringList(data.hidden_regimes);
			final boolean isParcoursDemat = questionComplementairesEvaluator.isParcoursDemat(complementReponses);
			final UserChecklistGenerationData userChecklistGenerationData = userChecklistGenerationDataBuilder.build(dateDepart, data.hidden_departement,
					regimes, regimesAlignes, liquidateurReponses, complementReponses, isParcoursDemat, true, data.hidden_attestationCarriereLongue);
			return displayCheckList(renderData, userChecklistGenerationData, data.complementReponseJsonStr, liquidateurReponses);
		} else {
			throw new RetraiteException("Situation anormale : l'étape '" + data.hidden_step + "' pour le traitement");
		}
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

	private void copyHiddenFields(final RenderData renderData, final PostData data) {
		try {
			for (final Field field : CommonExchangeData.class.getFields()) {
				final String fieldName = field.getName();
				if (fieldName.startsWith("hidden_")) {
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

	private RenderData displayLiquidateurQuestions(final PostData data, final RenderData renderData, final String regimes,
			final RegimeAligne[] regimesAlignes) {
		renderData.hidden_nom = data.nom;
		renderData.hidden_naissance = data.naissance;
		renderData.hidden_nir = data.nir;
		renderData.hidden_step = "displayLiquidateurQuestions";
		renderData.hidden_regimes = regimes;
		renderData.questionsLiquidateur = questionsLiquidateurBuilder.buildQuestions(regimesAlignes);
		return renderData;
	}

	private RenderData displayDepartureDateWithLiquidateurQuestions(final RenderData renderData, final PostData data) {
		return displayDepartureDate(data, renderData, data.liquidateurReponseJsonStr, null);
	}

	private RenderData displayDepartureDateWithoutLiquidateurQuestions(final PostData data, final RenderData renderData, final String regimes) {
		return displayDepartureDate(data, renderData, null, regimes);
	}

	private RenderData displayDepartureDate(final PostData data, final RenderData renderData, final String liquidateurReponseJsonStr, final String regimes) {
		renderData.hidden_step = "displayDepartureDate";
		if (liquidateurReponseJsonStr != null) {
			renderData.hidden_liquidateurReponseJsonStr = liquidateurReponseJsonStr;
		}
		if (regimes != null) {
			renderData.hidden_regimes = regimes;
		}
		renderData.listeMoisAvecPremier = stepFormsDataProvider.getListMoisAvecPremier();
		renderData.listeAnneesDepart = stepFormsDataProvider.getListAnneesDepart();
		return renderData;
	}

	private Object firstNotNull(final Object... objects) {
		for (final Object object : objects) {
			if (object != null) {
				return object;
			}
		}
		return null;
	}

	private RenderData displayAdditionalQuestionsAvecCarriereLongue(final PostData data, final RenderData renderData) {
		renderData.hidden_attestationCarriereLongue = true;
		return displayAdditionalQuestions(renderData, data.hidden_departMois, data.hidden_departAnnee);
	}

	private RenderData displayAdditionalQuestions(final RenderData renderData, final String departMois, final String departAnnee) {
		renderData.hidden_step = "displayAdditionalQuestions";
		renderData.hidden_departMois = departMois;
		renderData.hidden_departAnnee = departAnnee;
		renderData.questionsComplementaires = questionsComplementairesBuilder.buildQuestions();
		return renderData;
	}

	private RenderData displayCheckList(final RenderData renderData, final UserChecklistGenerationData userChecklistGenerationData,
			final String complementReponseJsonStr, final LiquidateurReponses liquidateurReponses) {
		renderData.hidden_step = "displayCheckList";
		renderData.hidden_complementReponseJsonStr = complementReponseJsonStr;
		try {
			renderData.userChecklist = userChecklistGenerator.generate(userChecklistGenerationData, liquidateurReponses);
		} catch (final RetraiteException e) {
			Logger.error(e, "Impossible de déterminer le régime liquidateur");
			renderData.errorMessage = "Désolé, impossible de déterminer le régime liquidateur...";
		}
		renderData.dateGeneration = DateUtils.format(dateProvider.getCurrentDate());
		return renderData;
	}

}
