package controllers.data;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;
import static utils.engine.data.enums.RegimeAligne.MSA;

import org.junit.Before;
import org.junit.Test;

import play.mvc.Http.Request;
import play.mvc.Scope.Params;
import utils.RetraiteException;
import utils.engine.data.enums.Regime;

public class ApiUserChecklistParamsTest {

	private ApiUserChecklistParams apiUserChecklistParams;

	@Before
	public void setUp() {
		apiUserChecklistParams = new ApiUserChecklistParams();
	}

	@Test
	@SuppressWarnings("deprecation")
	public void should_create_form_http_params() {

		// Le code interne de Params vérifie qu'il y a une Request...
		Request.current.set(new Request());

		final Params params = new Params();
		put(params, "nom", "DUPONT");
		put(params, "dateNaissance", "23/11/1954");
		put(params, "nir", "1223344567890");
		put(params, "departMois", "11");
		put(params, "departAnnee", "2017");
		put(params, "regimeLiquidateur", "MSA");
		put(params, "regimes", "CNAV,RSI");

		final ApiUserChecklistParams paramsResult = apiUserChecklistParams.fromHttpParams(params);

		assertThat(paramsResult.nom).isEqualTo("DUPONT");
		assertThat(paramsResult.dateNaissance).isEqualTo("23/11/1954");
		assertThat(paramsResult.nir).isEqualTo("1223344567890");
		assertThat(paramsResult.departMois).isEqualTo("11");
		assertThat(paramsResult.departAnnee).isEqualTo("2017");
		assertThat(paramsResult.regimeLiquidateur).isEqualTo(MSA);
		assertThat(paramsResult.regimes).isEqualTo(new Regime[] { Regime.CNAV, Regime.RSI });

	}

	@Test
	@SuppressWarnings("deprecation")
	public void should_throw_exception_if_param_unknown() {

		// Le code interne de Params vérifie qu'il y a une Request...
		Request.current.set(new Request());

		final Params params = new Params();
		put(params, "nom", "DUPONT");
		put(params, "autre", "bidon");

		try {
			apiUserChecklistParams.fromHttpParams(params);
			fail("Devrait déclencher une exception");
		} catch (final RetraiteException e) {
			assertThat(e.getMessage()).isEqualTo("Le paramètre 'autre' avec la valeur 'bidon' n'est pas attendu");
		}

	}

	private void put(final Params params, final String key, final String value) {
		params.data.put(key, new String[] { value });

	}
}
