package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;
import utils.JsonExclude;

@Entity
public class RetraiteUser extends GenericModel {
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="retraiteuser_generator")
	@SequenceGenerator(name="retraiteuser_generator", sequenceName="retraiteuser_id_seq")
	public Long id;

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
