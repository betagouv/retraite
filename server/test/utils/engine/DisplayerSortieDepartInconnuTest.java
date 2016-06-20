package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.RegimeAligne.CNAV;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.engine.data.RenderData;

public class DisplayerSortieDepartInconnuTest {

	private DisplayerSortieDepartInconnu displayerSortieDepartInconnu;

	@Before
	public void setUp() throws Exception {
		displayerSortieDepartInconnu = new DisplayerSortieDepartInconnu();
	}

	@Test
	public void test_CNAV() {

		final PostData postData = new PostData();
		postData.hidden_liquidateur = CNAV;
		final RenderData renderData = new RenderData();

		displayerSortieDepartInconnu.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displaySortieDepartInconnu");
		final Map<String, Object> expectedExtras = new HashMap<String, Object>() {
			{
				put("urlAgeDepart", CNAV.urlAgeDepart);
				put("urlDroits", CNAV.urlDroits);
				put("urlSimulMontant", CNAV.urlSimulMontant);
			}
		};
		assertThat(renderData.extras).isEqualTo(expectedExtras);
	}

}
