package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.Regime.AGIRC_ARRCO;
import static utils.engine.data.enums.Regime.BFSP;
import static utils.engine.data.enums.Regime.CARCD;
import static utils.engine.data.enums.Regime.CNAV;
import static utils.engine.data.enums.Regime.IRCEC;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.MonthAndYear;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.UserChecklistVars;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;

public class UserChecklistVarsProviderTest {

	private UserChecklistVarsProvider userChecklistVarsProvider;

	@Before
	public void setUp() {
		userChecklistVarsProvider = new UserChecklistVarsProvider();
	}

	@Test
	public void should_provide_good_variables() {

		final MonthAndYear dateDepart = null;
		final Regime[] regimes = new Regime[] { CNAV, IRCEC, BFSP, AGIRC_ARRCO, CARCD };
		final RegimeAligne[] regimesAlignes = null;
		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(dateDepart, "973", regimes, regimesAlignes,
				true);

		final UserChecklistVars vars = userChecklistVarsProvider.provideVars(userChecklistGenerationData);

		final Map<String, String> expectedMap = new HashMap<String, String>() {
			{
				put("regimes_base_hors_alignes", "Banque de France,Caisse d'assurance retraite des chirurgiens dentistes");
				put("regimes_compl_hors_agirc_arrco", "Caisse nationale de retraite complémentaire des artistes auteurs");
			}
		};
		assertThat(vars.getMapOfValues()).isEqualTo(expectedMap);
	}
}
