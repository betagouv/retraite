package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import play.db.jpa.GenericModel;

@Entity
public class FakeData extends GenericModel {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="fakedata_generator")
	@SequenceGenerator(name="fakedata_generator", sequenceName="fakedata_id_seq")
	public Long id;

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
