package poc.mail.sendgrid;

import org.junit.Ignore;
import org.junit.Test;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

public class PocSendGrid {

	@Test
	@Ignore
	public void test_send_mail() throws SendGridException {

		final SendGrid sendgrid = new SendGrid("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"); // recommended
		// OR
		// final SendGrid sendgrid = new SendGrid(api_user, api_password);

		final SendGrid.Email email = new SendGrid.Email();

		email.addTo("xnopre@gmail.com");
		email.setFrom("envoi.retraite@sgmap.fr");
		email.setSubject("Sending with SendGrid is Fun");
		email.setHtml("and easy to do anywhere, even with Java");

		final SendGrid.Response response = sendgrid.send(email);
		System.out.println("response=" + response);
		System.out.println("getCode=" + response.getCode());
		System.out.println("getMessage=" + response.getMessage());
		System.out.println("getStatus=" + response.getStatus());

		// Résultats :
		// response=com.sendgrid.SendGrid$Response@7adda9cc
		// getCode=200
		// getMessage={"message":"success"}
		// getStatus=true

	}
}
