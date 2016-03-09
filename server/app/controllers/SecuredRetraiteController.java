package controllers;

import models.RetraiteUser;
import play.Logger;
import play.mvc.Before;
import play.mvc.Http;
import utils.dao.RetraiteUserDao;

public class SecuredRetraiteController extends RetraiteController {

	private static final RetraiteUserDao retraiteUserDao = new RetraiteUserDao();

	@Before
	static void checkAccess() throws Throwable {
		// Authent
		if (!session.contains("username")) {
			unauthorizedAuthenticationRequired();
		}
		final String username = session.get("username");
		final RetraiteUser user = retraiteUserDao.findByLogin(username);
		if (user == null) {
			Logger.error("No user found with username=" + username + " : respond with error");
			unauthorizedAuthenticationRequired();
		}
	}

	protected static void unauthorizedAuthenticationRequired() {
		error(Http.StatusCode.UNAUTHORIZED, "Authentication required !");
	}

	@Before
	static void setConnectedUser() throws Throwable {
		if (!isConnected()) {
			return;
		}
		final RetraiteUser user = retrieveConnectedUser();
		storeCurrentUserInRenderArgs(user);
	}

	static RetraiteUser retrieveConnectedUser() {
		final String username = connected();
		return retraiteUserDao.findByLogin(username);
	}

	static void storeCurrentUserInRenderArgs(final RetraiteUser user) {
		renderArgs.put("user", user);
	}

	private static String connected() {
		return session.get("username");
	}

	private static boolean isConnected() {
		return session.contains("username");
	}

}
