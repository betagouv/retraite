package utils.wsinforetraite;

import static java.lang.String.valueOf;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import utils.RetraiteUnitTestBase;
import utils.WsUtils;

public class InfoRetraiteTest_RecupCasDeTest extends RetraiteUnitTestBase {

	@Test
	@Ignore
	public void run() throws Exception {

		final WsUtils wsUtils = new WsUtils();
		final InfoRetraite infoRetraite = new InfoRetraiteWsUr(
				new InfoRetraiteDecoder(),
				new InfoRetraiteConnector(wsUtils, new InfoRetraiteTokenRecuperator(wsUtils)));

		final FileWriter fileWriter = new FileWriter("ListeNomsEtNirsEtRegimes.txt");

		final Set<String> allRegimes = new HashSet<>();

		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("ListeNomsEtNirs.txt")));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				final String[] nirEtNom = line.split(",");

				final String regimes = infoRetraite.retrieveRegimes(nirEtNom[1], nirEtNom[0], "1/2/3");

				allRegimes.addAll(Arrays.asList(regimes.split(",")));

				fileWriter.write(line + "," + regimes + "\n");
				fileWriter.flush();

				i++;
				log(valueOf(i));
				pause();
			}
		} finally {
			fileWriter.write("\nRégimes trouvés :\n");
			for (final String regime : allRegimes) {
				fileWriter.write(regime + "\n");
			}

			fileWriter.close();
		}

		log("---");
	}

	private void log(final String string) {
		System.out.println(string);
	}

	private void pause() {
		try {
			Thread.sleep(500);
		} catch (final InterruptedException e) {
		}
	}

}
