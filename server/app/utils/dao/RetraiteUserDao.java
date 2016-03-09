package utils.dao;

import models.RetraiteUser;
import play.Play;

public class RetraiteUserDao {

	public RetraiteUser findByLogin(final String login) {
		RetraiteUser user = RetraiteUser.find("byLogin", login).first();
		if (user == null && login.equals(Play.configuration.getProperty("application.superadmin"))) {
			user = RetraiteUser.superAdminUser;
		}
		return user;
	}

	public RetraiteUser findByLoginAndPassword(final String login, final String password) {
		return RetraiteUser.find("byLoginAndPassword", login, password).first();
	}

}
