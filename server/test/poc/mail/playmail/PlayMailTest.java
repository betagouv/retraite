package poc.mail.playmail;

import java.util.concurrent.Future;

import org.apache.commons.mail.SimpleEmail;
import org.junit.Ignore;
import org.junit.Test;

import play.libs.Mail;
import utils.RetraiteUnitTestBase;

public class PlayMailTest extends RetraiteUnitTestBase {

	@Test
	@Ignore
	public void test_send_mail() throws Exception {

		final SimpleEmail email = new SimpleEmail();
		email.setFrom("envoi.retraite@sgmap.fr");
		email.addTo("xnopre@gmail.com");
		email.setSubject("test");
		email.setMsg("Message de test");
		final Future<Boolean> future = Mail.send(email);
		final Boolean result = future.get();
		System.out.println("Résultat envoi mail : " + result);
	}
}
