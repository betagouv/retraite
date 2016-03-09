package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import utils.engine.data.enums.ChecklistName;

@Entity
public class CaisseDepartementale extends RetraiteModel {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	public ChecklistName checklistName;

	public String departement;

	@ManyToOne
	public Caisse caisse;

}
