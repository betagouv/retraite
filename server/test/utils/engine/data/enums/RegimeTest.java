package utils.engine.data.enums;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.Regime.AGIRC_ARRCO;
import static utils.engine.data.enums.Regime.CIPAV;
import static utils.engine.data.enums.Regime.CNAV;
import static utils.engine.data.enums.Regime.IRCANTEC;
import static utils.engine.data.enums.Regime.MSA;
import static utils.engine.data.enums.Regime.RSI;

import org.junit.Test;

public class RegimeTest {

	@Test
	public void tests_fromStringList() {
		assertThat(Regime.fromStringList(null)).isEmpty();

		assertThat(Regime.fromStringList("")).isEmpty();

		assertThat(Regime.fromStringList("IRCANTEC,CIPAV")).containsOnly(IRCANTEC, CIPAV);

		assertThat(Regime.fromStringList("AGIRC ARRCO,CNAV")).containsOnly(AGIRC_ARRCO, CNAV);
		assertThat(Regime.fromStringList("AGIRC-ARRCO,CNAV")).containsOnly(AGIRC_ARRCO, CNAV);

		assertThat(Regime.fromStringList("CNAV,ORGANIC")).containsOnly(CNAV, RSI);

		assertThat(Regime.fromStringList("AGIRC ARRCO,IRCANTEC,CCMSA,CNAV,CANCAVA")).containsOnly(AGIRC_ARRCO, IRCANTEC, MSA, CNAV, RSI);
	}

	@Test
	public void tests_valueOfNom() {

		assertThat(Regime.valueOfNom("IRCANTEC")).isEqualTo(IRCANTEC);
		assertThat(Regime.valueOfNom("CNAV")).isEqualTo(CNAV);

		assertThat(Regime.valueOfNom("AGIRC ARRCO")).isEqualTo(AGIRC_ARRCO);
		assertThat(Regime.valueOfNom("AGIRC-ARRCO")).isEqualTo(AGIRC_ARRCO);

		assertThat(Regime.valueOfNom("ORGANIC")).isEqualTo(RSI);
		assertThat(Regime.valueOfNom("CANCAVA")).isEqualTo(RSI);

	}

}
