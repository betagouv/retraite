package models;

import javax.persistence.MappedSuperclass;

import play.db.jpa.Model;

@MappedSuperclass
public class RetraiteModel extends Model {

	/**
	 * La super-classe Model renvoie "false" si ID = null,
	 */
	@Override
	public boolean equals(Object other) {
		return other != null && other.getClass() == getClass();
	}

	public void setId(Long id) {
		this.id = id;
	}
}
