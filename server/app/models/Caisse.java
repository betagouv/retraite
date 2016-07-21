package models;

import javax.persistence.Entity;

@Entity
public class Caisse extends RetraiteModel {

	private static final long serialVersionUID = 1L;

	public String nom;

	public String adresse1;

	public String adresse2;

	public String adresse3;

	public String adresse4;

	public String telephone;

	public String fax;

	public String site;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((adresse1 == null) ? 0 : adresse1.hashCode());
		result = prime * result + ((adresse2 == null) ? 0 : adresse2.hashCode());
		result = prime * result + ((adresse3 == null) ? 0 : adresse3.hashCode());
		result = prime * result + ((adresse4 == null) ? 0 : adresse4.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result + ((telephone == null) ? 0 : telephone.hashCode());
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
		final Caisse other = (Caisse) obj;
		if (adresse1 == null) {
			if (other.adresse1 != null)
				return false;
		} else if (!adresse1.equals(other.adresse1))
			return false;
		if (adresse2 == null) {
			if (other.adresse2 != null)
				return false;
		} else if (!adresse2.equals(other.adresse2))
			return false;
		if (adresse3 == null) {
			if (other.adresse3 != null)
				return false;
		} else if (!adresse3.equals(other.adresse3))
			return false;
		if (adresse4 == null) {
			if (other.adresse4 != null)
				return false;
		} else if (!adresse4.equals(other.adresse4))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		if (telephone == null) {
			if (other.telephone != null)
				return false;
		} else if (!telephone.equals(other.telephone))
			return false;
		return true;
	}

}
