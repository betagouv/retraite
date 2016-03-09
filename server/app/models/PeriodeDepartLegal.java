package models;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
public class PeriodeDepartLegal extends RetraiteModel {

	private static final long serialVersionUID = 1L;

	public String dateNaissanceDebut;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "annees", column = @Column(name = "ageDepartLegal_annees") ),
			@AttributeOverride(name = "mois", column = @Column(name = "ageDepartLegal_mois") )
	})
	public AnneesEtMois ageDepartLegal;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "annees", column = @Column(name = "ageTauxPlein_annees") ),
			@AttributeOverride(name = "mois", column = @Column(name = "ageTauxPlein_mois") )
	})
	public AnneesEtMois ageTauxPlein;

	public PeriodeDepartLegal() {
	}

	public PeriodeDepartLegal(final String dateNaissanceDebut, final AnneesEtMois ageDepartLegal, final AnneesEtMois ageTauxPlein) {
		this.dateNaissanceDebut = dateNaissanceDebut;
		this.ageDepartLegal = ageDepartLegal;
		this.ageTauxPlein = ageTauxPlein;
	}

	@Override
	public String toString() {
		return "PeriodeDepartLegal[dateNaissanceDebut=" + dateNaissanceDebut + ", ageDepartLegal=" + ageDepartLegal + ", ageTauxPlein=" + ageTauxPlein + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ageDepartLegal == null) ? 0 : ageDepartLegal.hashCode());
		result = prime * result + ((ageTauxPlein == null) ? 0 : ageTauxPlein.hashCode());
		result = prime * result + ((dateNaissanceDebut == null) ? 0 : dateNaissanceDebut.hashCode());
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
		final PeriodeDepartLegal other = (PeriodeDepartLegal) obj;
		if (ageDepartLegal == null) {
			if (other.ageDepartLegal != null)
				return false;
		} else if (!ageDepartLegal.equals(other.ageDepartLegal))
			return false;
		if (ageTauxPlein == null) {
			if (other.ageTauxPlein != null)
				return false;
		} else if (!ageTauxPlein.equals(other.ageTauxPlein))
			return false;
		if (dateNaissanceDebut == null) {
			if (other.dateNaissanceDebut != null)
				return false;
		} else if (!dateNaissanceDebut.equals(other.dateNaissanceDebut))
			return false;
		return true;
	}

}
