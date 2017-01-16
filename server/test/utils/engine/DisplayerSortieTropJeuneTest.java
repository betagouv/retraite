package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.RegimeAligne.CNAV;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import controllers.utils.Look;
import utils.engine.data.RenderData;
import utils.engine.utils.RetraitePropertiesLoader;

public class DisplayerSortieTropJeuneTest {

	private DisplayerSortieTropJeune displayerSortieTropJeune;

	@Before
	public void setUp() throws Exception {
		displayerSortieTropJeune = new DisplayerSortieTropJeune();
	}

	@Test
	public void test_with_look() {

		final PostData postData = new PostData();
		postData.look = Look.CNAV;
		final RenderData renderData = new RenderData();

		displayerSortieTropJeune.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displaySortieTropJeune");
		final Map<String, Object> expectedExtras = new HashMap<String, Object>() {
			{
				put("urlDroits", CNAV.urlDroits);
				put("urlInfosDepartRetraite", CNAV.urlInfosDepartRetraite);
			}
		};
		assertThat(renderData.extras).isEqualTo(expectedExtras);
	}

	@Test
	public void test_without_look() {

		final PostData postData = new PostData();
		postData.look = Look.GENERIC;
		final RenderData renderData = new RenderData();

		displayerSortieTropJeune.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displaySortieTropJeune");
		final Map<String, Object> expectedExtras = new HashMap<String, Object>() {
			{
				put("urlDroits", RetraitePropertiesLoader.getInstance().getProperty("GIP_UR.urlDroits"));
				put("urlInfosDepartRetraite", RetraitePropertiesLoader.getInstance().getProperty("GIP_UR.urlInfosDepartRetraite"));
			}
		};
		assertThat(renderData.extras).isEqualTo(expectedExtras);
	}

}
