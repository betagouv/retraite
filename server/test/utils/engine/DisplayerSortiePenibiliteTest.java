package utils.engine;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.RegimeAligne.MSA;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import controllers.data.PostData;
import utils.engine.data.RenderData;
import utils.engine.utils.RetraiteConstants;
import utils.engine.utils.RetraitePropertiesLoader;

public class DisplayerSortiePenibiliteTest {

	private DisplayerSortiePenibilite displayerSortiePenibilite;

	@Before
	public void setUp() throws Exception {
		displayerSortiePenibilite = new DisplayerSortiePenibilite();
	}

	@Test
	public void test_avec_regime_liquidateur() {

		final PostData postData = new PostData();
		postData.hidden_liquidateur = MSA;
		final RenderData renderData = new RenderData();

		displayerSortiePenibilite.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displaySortiePenibilite");
		final Map<String, Object> expectedExtras = new HashMap<String, Object>() {
			{
				put("urlInfosPenibilite", MSA.urlInfosPenibilite);
			}
		};
		assertThat(renderData.extras).isEqualTo(expectedExtras);
	}

	@Test
	public void test_sans_regime_liquidateur() {

		final PostData postData = new PostData();
		postData.hidden_liquidateur = null;
		final RenderData renderData = new RenderData();

		displayerSortiePenibilite.fillData(postData, renderData);

		assertThat(renderData.hidden_step).isEqualTo("displaySortiePenibilite");
		final Map<String, Object> expectedExtras = new HashMap<String, Object>() {
			{
				put("urlInfosPenibilite", RetraitePropertiesLoader.getInstance().getProperty("GIP_UR.urlInfosPenibilite"));
			}
		};
		assertThat(renderData.extras).isEqualTo(expectedExtras);
	}

}
