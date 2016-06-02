package controllers;

import static utils.engine.intern.UserChecklistGeneratorFactory.createUserChecklistGenerator;

import controllers.data.ApiUserChecklistParams;
import controllers.utils.ApiChecklistGenerator;
import utils.engine.data.RenderData;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.UserChecklistGenerationDataBuilder;

public class ApiUserChecklist extends RetraiteController {

	public static void getUserChecklist() {

		final ApiChecklistGenerator generator = new ApiChecklistGenerator(
				new UserChecklistGenerationDataBuilder(),
				createUserChecklistGenerator(),
				new CalculateurRegimeAlignes());

		final ApiUserChecklistParams apiUserChecklistParams = ApiUserChecklistParams.fromHttpParams(params);

		final RenderData data = generator.generate(apiUserChecklistParams);

		if (data.errorMessage != null) {
			renderText(data.errorMessage);
		}
		data.isPDF = false;
		if (apiUserChecklistParams.full) {
			final String look = "style";
			renderTemplate("Application/steps/displayChecklist.html", data, look);
		} else {
			renderTemplate("ApiUserChecklist/getUserChecklist.html", data);
		}
	}

}
