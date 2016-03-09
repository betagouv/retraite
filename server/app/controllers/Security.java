package controllers;

import play.Logger;
import play.Play;
import utils.dao.RetraiteUserDao;

public class Security {

	private static final RetraiteUserDao retraiteUserDao = new RetraiteUserDao();

	static boolean authenticate(final String login, final String password) {
		if (retraiteUserDao.findByLoginAndPassword(login, password) != null) {
			Logger.debug("Authentification OK pour " + login);
			return true;
		}
		if (login.equals(Play.configuration.getProperty("application.superadmin"))
				&& password.equals(Play.configuration.getProperty("application.superadminpwd"))) {
			Logger.debug("Authentification spéciale OK pour " + login);
			return true;
		}
		Logger.debug("Authentification KO pour " + login);
		return false;
	}

}
