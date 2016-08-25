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
	public void should_provide_good_variables_with_booleans_1() {

		final MonthAndYear dateDepart = null;
		final Regime[] regimes = new Regime[] { CNAV, IRCEC, BFSP, AGIRC_ARRCO, CARCD };
		final RegimeAligne[] regimesAlignes = null;
		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withDateDepart(dateDepart)
				.withDepartement("973")
				.withRegimes(regimes)
				.withRegimesAlignes(regimesAlignes)
				.withPublished(true)
				.withIsPDF(false)
				.withRegimesInfosJsonStr("")
				.withSA(true)
				.withNSA(false)
				.withConjointCollaborateur(false)
				.withChef(true)
				.get();

		final UserChecklistVars vars = userChecklistVarsProvider.provideVars(userChecklistGenerationData);

		final Map<String, Object> expectedMap = new HashMap<String, Object>() {
			{
				put("regimes_base_hors_alignes", "Banque de France,Caisse d'assurance retraite des chirurgiens dentistes");
				put("regimes_compl_hors_agirc_arrco", "Caisse nationale de retraite complémentaire des artistes auteurs");
				put("regimes_hors_alignes_et_hors_agirc_arrco",
						"Caisse nationale de retraite complémentaire des artistes auteurs,Banque de France,Caisse d'assurance retraite des chirurgiens dentistes");
				put("agirc_arrco", true);
				put("status_nsa", false);
				put("status_sa", true);
				put("status_chef", true);
				put("status_conjoint", false);

			}
		};
		assertThat(vars.getMapOfValues()).isEqualTo(expectedMap);
	}

	@Test
	public void should_provide_good_variables_with_booleans_2() {

		final MonthAndYear dateDepart = null;
		final Regime[] regimes = new Regime[] { CNAV, IRCEC, BFSP, CARCD };
		final RegimeAligne[] regimesAlignes = null;
		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withDateDepart(dateDepart)
				.withDepartement("973")
				.withRegimes(regimes)
				.withRegimesAlignes(regimesAlignes)
				.withPublished(true)
				.withIsPDF(false)
				.withRegimesInfosJsonStr("")
				.withSA(false)
				.withNSA(true)
				.withConjointCollaborateur(true)
				.withChef(false)
				.get();

		final UserChecklistVars vars = userChecklistVarsProvider.provideVars(userChecklistGenerationData);

		final Map<String, Object> expectedMap = new HashMap<String, Object>() {
			{
				put("regimes_base_hors_alignes", "Banque de France,Caisse d'assurance retraite des chirurgiens dentistes");
				put("regimes_compl_hors_agirc_arrco", "Caisse nationale de retraite complémentaire des artistes auteurs");
				put("regimes_hors_alignes_et_hors_agirc_arrco",
						"Caisse nationale de retraite complémentaire des artistes auteurs,Banque de France,Caisse d'assurance retraite des chirurgiens dentistes");
				put("agirc_arrco", false);
				put("status_nsa", true);
				put("status_sa", false);
				put("status_chef", false);
				put("status_conjoint", true);

			}
		};
		assertThat(vars.getMapOfValues()).isEqualTo(expectedMap);
	}
}
