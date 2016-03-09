package models;

import javax.persistence.Embeddable;

@Embeddable
public class AnneesEtMois {

	public int annees;

	public int mois;

	public AnneesEtMois() {
	}

	public AnneesEtMois(final int annees, final int mois) {
		this.annees = annees;
		this.mois = mois;
	}

	@Override
	public String toString() {
		return "AnneesEtMois[annees=" + annees + ", mois=" + mois + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + annees;
		result = prime * result + mois;
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AnneesEtMois other = (AnneesEtMois) obj;
		if (annees != other.annees)
			return false;
		if (mois != other.mois)
			return false;
		return true;
	}

}
