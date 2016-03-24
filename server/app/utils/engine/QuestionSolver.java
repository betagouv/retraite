package utils.engine;

import utils.engine.data.RegimeLiquidateurAndUserStatus;
import utils.engine.data.enums.RegimeAligne;

public interface QuestionSolver {

	RegimeLiquidateurAndUserStatus solve(RegimeAligne[] regimesAlignes, String liquidateurReponseJsonStr);

}