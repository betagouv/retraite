package utils.mail;

import java.io.File;

import com.sendgrid.SendGrid;

import utils.RetraiteException;

public class MailSenderWithSendGrid {

	public void sendMail(final String from, final String to, final String subject, final String htmlContent, final File file) {

		final SendGrid sendgrid = new SendGrid("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"); // recommended
		final SendGrid.Email email = new SendGrid.Email();
		email.setFrom(from);
		email.addTo(to);
		email.setSubject(subject);
		email.setHtml(htmlContent);
		try {
			email.addAttachment("Parcours.pdf", file);
			final SendGrid.Response response = sendgrid.send(email);
			if (!response.getStatus()) {
				throw new RetraiteException("Error sending mail '" + subject + "' from " + from + " to " + to + " : code=" + response.getCode() + ", message="
						+ response.getMessage());
			}
		} catch (final Exception e) {
			throw new RetraiteException("Error sending mail '" + subject + "' from " + from + " to " + to, e);
		}
	}

}
