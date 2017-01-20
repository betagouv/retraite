package controllers;

import controllers.data.UserData;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.Http;

public class Secure extends Controller {

	public static void authenticate(@Required UserData userData) throws Throwable {
				
		String username = userData.username;
		String password = userData.password;
		
		// Check tokens
		final Boolean allowed = Security.authenticate(username, password);
		if (validation.hasErrors() || !allowed) {
			error(Http.StatusCode.UNAUTHORIZED, "Authentication failed !");
		}
		// Mark user as connected
		session.put("username", username);

		// Redirect to the original URL (or /)
		// redirectToOriginalURL();
		ok();

	}

	public static void logout() throws Throwable {
		session.clear();
		ok();
	}

}
