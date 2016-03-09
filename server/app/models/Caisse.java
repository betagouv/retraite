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
}
