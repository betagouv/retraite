package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.Regime.CARMF;
import static utils.engine.data.enums.Regime.CARPIMKO;
import static utils.engine.data.enums.Regime.IRCANTEC;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import utils.JsonUtils;
import utils.engine.data.InfoRetraiteResultRegimeList;
import utils.engine.data.UserChecklist;
import utils.engine.data.enums.Regime;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;

public class AutreRegimeComputerTest {

	private AutreRegimeComputer autreRegimeComputer;

	@Before
	public void setUp() throws Exception {
		autreRegimeComputer = new AutreRegimeComputer();
	}

	@Test
	public void test() {

		final UserChecklist userChecklist = new UserChecklist();
		final String regimesInfosJsonStr = "[" + add("CNAV") + "," + add("AGIRC-ARRCO") + "," + add("ORGANIC") + "," + add("CANCAVA") + "," + add("CCMSA") + ","
				+ add("CARMF") + "," + add("IRCANTEC") + ","
				+ add("CARPIMKO") + "]";

		autreRegimeComputer.compute(userChecklist, regimesInfosJsonStr);

		final InfoRetraiteResultRegimeList autresRegimesDeBase = create(CARMF, CARPIMKO);
		assertThat(userChecklist.autresRegimesDeBase).isEqualTo(autresRegimesDeBase);
		final InfoRetraiteResultRegimeList regimesComplementaires = create(IRCANTEC);
		assertThat(userChecklist.regimesComplementaires).isEqualTo(regimesComplementaires);

	}

	private InfoRetraiteResultRegimeList create(final Regime... regimes) {
		final InfoRetraiteResultRegimeList list = new InfoRetraiteResultRegimeList();
		for (final Regime regime : regimes) {
			list.add(createInfo(regime.toString()));
		}
		return list;
	}

	private InfoRetraiteResultRegime createInfo(final String regime) {
		final InfoRetraiteResultRegime infos = new InfoRetraiteResultRegime();
		infos.nom = regime;
		infos.regime = "regime " + regime;
		infos.image = "http://" + regime;
		infos.adresse = "addr " + regime;
		infos.internet = "http://www." + regime;
		infos.tel1 = "tel " + regime;
		infos.email1 = "mail " + regime;
		return infos;
	}

	private String add(final String regime) {
		final Map<String, String> map = new HashMap<String, String>() {
			{
				put("nom", regime);
				put("regime", "regime " + regime);
				put("image", "http://" + regime);
				put("adresse", "addr " + regime);
				put("internet", "http://www." + regime);
				put("tel1", "tel " + regime);
				put("email1", "mail " + regime);
			}
		};
		return JsonUtils.toJson(map);
	}

}
