package models;

import javax.persistence.Entity;

import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Model;
import utils.JsonExclude;

@Entity
public class RetraiteUser extends Model {

	@Required
	public String login;

	@Required
	@Password
	@JsonExclude
	public String password;

	public String authorizedChecklists;

	@Override
	public String toString() {
		return login;
	}

	// Utilisateurs spéciaux

	public static RetraiteUser superAdminUser;

	static {
		superAdminUser = new RetraiteUser();
		superAdminUser.id = 0L;
	}

}
