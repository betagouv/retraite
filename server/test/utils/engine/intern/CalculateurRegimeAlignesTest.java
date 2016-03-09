package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;

@RunWith(Parameterized.class)
@Ignore("Le temps de trouver comment lancer les TU en ligne de commande dans la CI, y compris pour Parameterized...")
public class CalculateurRegimeAlignesTest {

	@Parameters
	public static Collection<Object[]> data() {
		/* @formatter:off */
		return Arrays.asList(new Object[][] {
				{"CNAV", array(CNAV)},
				{"CCMSA", array(MSA)},
				{"CANCAVA", array(RSI)},
				{"ORGANIC", array(RSI)},
				{"CNAV,CCMSA", array(CNAV, MSA)},
				{"CNAV,ORGANIC", array(CNAV, RSI)},
				{"CNAV,CANCAVA,CCMSA", array(CNAV, MSA, RSI)},
				{"CNAV,CANCAVA,CCMSA,ORGANIC", array(CNAV, MSA, RSI)},
				{"toto,titi", array()},
				{array(Regime.CNAV, Regime.RSI), array(CNAV, RSI)}
		});
		/* @formatter:on */
	}

	private static Regime[] array(final Regime... regimes) {
		return regimes;
	}

	private static RegimeAligne[] array(final RegimeAligne... regimesAlignes) {
		return regimesAlignes;
	}

	private static RegimeAligne[] array() {
		return new RegimeAligne[0];
	}

	private final Object regimes;
	private final RegimeAligne[] expectedRegimesAlignes;
	private final CalculateurRegimeAlignes calculateurRegimeAlignes;

	public CalculateurRegimeAlignesTest(final Object regimes, final RegimeAligne[] expectedRegimesAlignes) {
		this.regimes = regimes;
		this.expectedRegimesAlignes = expectedRegimesAlignes;
		calculateurRegimeAlignes = new CalculateurRegimeAlignes();
	}

	@Test
	public void test() {
		if (regimes instanceof String) {
			assertThat(calculateurRegimeAlignes.getRegimesAlignes((String) regimes)).isEqualTo(expectedRegimesAlignes);
		} else {
			assertThat(calculateurRegimeAlignes.getRegimesAlignes((Regime[]) regimes)).isEqualTo(expectedRegimesAlignes);

		}
	}

}
