package utils.wsinforetraite;

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

		final InfoRetraite infoRetraite = new InfoRetraiteReal(new InfoRetraiteDecoder(), new InfoRetraiteConnector(new WsUtils()));

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
				System.out.println(i);
				pause();
			}
		} finally {
			fileWriter.write("\nRégimes trouvés :\n");
			for (final String regime : allRegimes) {
				fileWriter.write(regime + "\n");
			}

			fileWriter.close();
		}

		System.out.println("---");
	}

	private void pause() {
		try {
			Thread.sleep(500);
		} catch (final InterruptedException e) {
		}
	}

}
