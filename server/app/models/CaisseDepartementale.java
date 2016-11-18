package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import play.db.jpa.GenericModel;
import utils.engine.data.enums.ChecklistName;

@Entity
public class CaisseDepartementale extends GenericModel {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="caissedepartementale_generator")
	@SequenceGenerator(name="caissedepartementale_generator", sequenceName="caissedepartementale_id_seq")
	public Long id;

	@Enumerated(EnumType.STRING)
	public ChecklistName checklistName;

	public String departement;

	@ManyToOne
	public Caisse caisse;

	public String toString() {
		return "CaisseDepartementale[id=" + id + ", checklistName=" + checklistName + ", departement=" + departement + ", caisse=" + caisse + "]";
	}
}
