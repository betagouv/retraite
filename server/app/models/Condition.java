package models;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import utils.JsonExclude;

@Entity
@Table(name = "ChapitreCondition")
public class Condition extends GenericModel implements Cloneable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="chapitrecondition_generator")
	@SequenceGenerator(name="chapitrecondition_generator", sequenceName="chapitrecondition_id_seq")
	public Long id;

	@ManyToOne
	@JsonExclude
	public Chapitre chapitre;

	@ElementCollection
	@MapKeyColumn(name = "cle")
	@Column(name = "valeur")
	@CollectionTable(name = "ConditionProps", joinColumns = @JoinColumn(name = "condition_id") )
	public Map<String, String> props = new HashMap<String, String>();

	@Override
	public String toString() {
		return "Condition[props=" + props + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((props == null) ? 0 : props.hashCode());
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
		final Condition other = (Condition) obj;
		if (props == null) {
			if (other.props != null)
				return false;
		} else if (!props.equals(other.props))
			return false;
		return true;
	}

}
