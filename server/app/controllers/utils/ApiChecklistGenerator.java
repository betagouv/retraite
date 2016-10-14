package controllers.utils;

import java.util.Arrays;
import java.util.List;

import controllers.data.ApiUserChecklistParams;
import utils.JsonUtils;
import utils.engine.data.MonthAndYear;
import utils.engine.data.RenderData;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.ChecklistName;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;
import utils.wsinforetraite.FakeRegimeDataProvider;

public class ApiChecklistGenerator {

	private final UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder;
	private final UserChecklistGenerator userChecklistGenerator;
	private final CalculateurRegimeAlignes calculateurRegimeAlignes;

	public ApiChecklistGenerator(final UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilder,
			final UserChecklistGenerator userChecklistGenerator, final CalculateurRegimeAlignes calculateurRegimeAlignes) {
		this.userChecklistGenerationDataBuilder = userChecklistGenerationDataBuilder;
		this.userChecklistGenerator = userChecklistGenerator;
		this.calculateurRegimeAlignes = calculateurRegimeAlignes;
	}

	public RenderData generate(final ApiUserChecklistParams apiUserChecklistParams) {

		final String nom = apiUserChecklistParams.nom;
		final String dateNaissance = apiUserChecklistParams.dateNaissance;
		final String nir = apiUserChecklistParams.nir;
		final String departement = apiUserChecklistParams.departement;
		final String departMois = apiUserChecklistParams.departMois;
		final String departAnnee = apiUserChecklistParams.departAnnee;
		final RegimeAligne regimeLiquidateur = apiUserChecklistParams.regimeLiquidateur;
		final List<UserStatus> userStatus = asList(apiUserChecklistParams.userStatus);
		final Regime[] regimes = apiUserChecklistParams.regimes;
		final boolean isCarriereLongue = apiUserChecklistParams.isCarriereLongue;
		final boolean published = apiUserChecklistParams.published;

		if (isNullOrEmpty(departement)) {
			return dataWithErrorMessage("Le paramètre 'departement' est manquant");
		}
		if (isNullOrEmpty(departMois)) {
			return dataWithErrorMessage("Le paramètre 'departMois' est manquant");
		}
		if (isNullOrEmpty(departAnnee)) {
			return dataWithErrorMessage("Le paramètre 'departAnnee' est manquant");
		}
		if (isNullOrEmpty(dateNaissance)) {
			return dataWithErrorMessage("Le paramètre 'dateNaissance' est manquant");
		}
		if (isNull(regimeLiquidateur)) {
			return dataWithErrorMessage("Le paramètre 'regimeLiquidateur' est manquant");
		}

		final RenderData data = new RenderData();

		data.hidden_nom = nom;
		data.hidden_naissance = dateNaissance;
		data.hidden_nir = nir;
		data.hidden_departMois = departMois;
		data.hidden_departAnnee = departAnnee;

		final String regimesInfosJsonStr = JsonUtils.toJson(new FakeRegimeDataProvider().create(regimes));

		final MonthAndYear dateDepart = new MonthAndYear(departMois, departAnnee);
		final RegimeAligne[] regimesAlignes = calculateurRegimeAlignes.getRegimesAlignes(regimes);
		final UserChecklistGenerationData userChecklistGenerationData = userChecklistGenerationDataBuilder.build(dateDepart, departement, regimes,
				regimesAlignes, regimeLiquidateur, published, isCarriereLongue, userStatus, false, regimesInfosJsonStr);
		final ChecklistName checklistName = ChecklistName.valueOf(apiUserChecklistParams.regimeLiquidateur);

		data.userChecklist = userChecklistGenerator.generate(checklistName, userChecklistGenerationData);
		return data;
	}

	private List<UserStatus> asList(final UserStatus userStatus) {
		return userStatus == null ? null : Arrays.asList(userStatus);
	}

	private boolean isNullOrEmpty(final String str) {
		return isNull(str) || str.isEmpty();
	}

	private boolean isNull(final Object str) {
		return str == null;
	}

	private RenderData dataWithErrorMessage(final String errorMessage) {
		final RenderData data = new RenderData();
		data.errorMessage = errorMessage;
		return data;
	}

}
