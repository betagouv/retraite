package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.Regime.AGIRC_ARRCO;
import static utils.engine.data.enums.Regime.BFSP;
import static utils.engine.data.enums.Regime.CARCD;
import static utils.engine.data.enums.Regime.CNAV;
import static utils.engine.data.enums.Regime.IRCANTEC;
import static utils.engine.data.enums.Regime.IRCEC;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.MonthAndYear;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.UserChecklistVars;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.Regime.RegimeType;
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
		final Regime[] regimes = new Regime[] { CNAV, IRCEC, BFSP, AGIRC_ARRCO, CARCD, IRCANTEC };
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
				.withCarriereLonge(true)
				.get();

		final UserChecklistVars vars = userChecklistVarsProvider.provideVars(userChecklistGenerationData);

		final Map<String, Object> expectedMap = new HashMap<String, Object>() {
			{
				put("regimes_base_hors_alignes", "<ul><li>BFSP (Banque de France)</li><li>CARCD (Caisse d'assurance retraite des chirurgiens dentistes)</li></ul>");
				put("regimes_compl_hors_agirc_arrco", "<ul><li>IRCEC (Caisse nationale de retraite complémentaire des artistes auteurs)</li><li>IRCANTEC</ul>");
				put("regimes_hors_alignes_et_hors_agirc_arrco",
						"<ul><li>IRCEC (Caisse nationale de retraite complémentaire des artistes auteurs)</li><li>BFSP (Banque de France)</li><li>CARCD (Caisse d'assurance retraite des chirurgiens dentistes)</li><li>IRCANTEC</li></ul>");
				put("regimes_compl_hors_agirc_arrco", "<ul><li>IRCEC (Caisse nationale de retraite complémentaire des artistes auteurs)</li><li>IRCANTEC</li></ul>");
				put("regimes_hors_alignes_ou_regimes_compl",
						"<ul><li>IRCEC (Caisse nationale de retraite complémentaire des artistes auteurs)</li><li>BFSP (Banque de France)</li><li>AGIRC_ARRCO (AGIRC ARRCO)</li><li>CARCD (Caisse d'assurance retraite des chirurgiens dentistes)</li><li>IRCANTEC</li></ul>");
				put("agirc_arrco", true);
				put("status_nsa", false);
				put("status_sa", true);
				put("status_chef", true);
				put("status_conjoint", false);
				put("carriere_longue", true);
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
				.withCarriereLonge(false)
				.get();

		final UserChecklistVars vars = userChecklistVarsProvider.provideVars(userChecklistGenerationData);

		final Map<String, Object> expectedMap = new HashMap<String, Object>() {
			{
				put("regimes_base_hors_alignes", "<ul><li>BFSP (Banque de France)</li><li>CARCD (Caisse d'assurance retraite des chirurgiens dentistes)</li></ul>");
				put("regimes_compl_hors_agirc_arrco", "<ul><li>IRCEC (Caisse nationale de retraite complémentaire des artistes auteurs)</li></ul>");
				put("regimes_hors_alignes_et_hors_agirc_arrco",
						"<ul><li>IRCEC (Caisse nationale de retraite complémentaire des artistes auteurs)</li><li>BFSP (Banque de France)</li><li>CARCD (Caisse d'assurance retraite des chirurgiens dentistes)</li></ul>");
				put("regimes_hors_alignes_ou_regimes_compl",
						"<ul><li>IRCEC (Caisse nationale de retraite complémentaire des artistes auteurs)</li><li>BFSP (Banque de France)</li><li>CARCD (Caisse d'assurance retraite des chirurgiens dentistes)</li></ul>");
				put("agirc_arrco", false);
				put("status_nsa", true);
				put("status_sa", false);
				put("status_chef", false);
				put("status_conjoint", true);
				put("status_conjoint", true);
				put("carriere_longue", false);

			}
		};
		assertThat(vars.getMapOfValues()).isEqualTo(expectedMap);
	}


	@Test
	public void getNameFor_should_return_acronyme_item() {
		Regime regime = Regime.CNAV;
		String name = userChecklistVarsProvider.getNameFor(regime);
		assertThat(name).isEqualTo("<li>" + regime.toString() + "</li>");
	}

	@Test
	public void getNameFor_should_return_acronyme_end_name() {
		Regime regime = Regime.CARCD;
		String name = userChecklistVarsProvider.getNameFor(regime);
		assertThat(name).isEqualTo("<li>" + regime.toString() + " (" + regime.getNom() + ")</li>");
	}

	@Test
	public void buildHtmlList_should_return_null_with_text_null() {
		String text = null;		
		String htmlText = userChecklistVarsProvider.buildHtmlList(text);		
		assertThat(htmlText).isNull();
	}

	@Test
	public void buildHtmlList_should_return_empty_with_empty() {
		String text = "";		
		String htmlText = userChecklistVarsProvider.buildHtmlList(text);		
		assertThat(htmlText).isEqualTo("");
	}

	@Test
	public void buildHtmlList_should_return_html_list_with_text_notnull() {
		String text = "toto";		
		String htmlText = userChecklistVarsProvider.buildHtmlList(text);		
		assertThat(htmlText).isEqualTo("<ul>"+text+"</ul>");
	}
}
