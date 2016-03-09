package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import play.data.validation.Required;

@Entity
public class Checklist extends RetraiteModel implements Cloneable {

	private static final long serialVersionUID = 1L;

	@Required
	public String nom;

	public boolean published;

	public boolean modifiedButNotPublished;

	public String type;

	@OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderColumn(name = "sortIndex")
	public List<Chapitre> chapitres;

	@Override
	public Checklist save() {
		preSave();
		return super.save();
	}

	@Override
	public String toString() {
		return "Checklist[" + nom + ", published=" + published + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((chapitres == null) ? 0 : chapitres.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + (published ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Checklist other = (Checklist) obj;
		if (chapitres == null) {
			if (other.chapitres != null)
				return false;
		} else if (!chapitres.equals(other.chapitres))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (published != other.published)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	private void preSave() {
		if (chapitres != null) {
			for (final Chapitre chapitre : chapitres) {
				chapitre.checklist = this;
				chapitre.preSave();
			}
		}
	}

}
