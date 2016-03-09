package models;

import javax.persistence.Entity;

@Entity
public class FakeData extends RetraiteModel {

	private static final long serialVersionUID = 1L;

	public String nom;
	public String nir;
	public String regimes;

	public static FakeData find(final String nom, final String nir) {
		return find("byNomAndNir", nom, nir).first();
	}

	@Override
	public String toString() {
		return nom + " / " + nir + " : " + regimes;
	}

}
