package models;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import utils.JsonExclude;

@Entity
public class Chapitre extends RetraiteModel implements Cloneable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JsonExclude
	public Checklist checklist;

	public String titre;

	public String description;

	public boolean closedInEdition;

	@Column(columnDefinition = "text")
	public String texteActions;

	@Column(columnDefinition = "text")
	public String texteModalites;

	@Column(columnDefinition = "text")
	public String texteInfos;

	@Deprecated
	@Column(columnDefinition = "text")
	public String texteIntro;

	@Deprecated
	public boolean parcoursDematDifferent;

	@Deprecated
	@Column(columnDefinition = "text")
	public String parcours;

	@Deprecated
	@Column(columnDefinition = "text")
	public String parcoursDemat;

	@Deprecated
	@Column(columnDefinition = "text")
	public String texteComplementaire;

	@Column(columnDefinition = "text")
	public String notes;

	@OneToMany(mappedBy = "chapitre", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Condition> conditions;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "type", column = @Column(name = "delai_type")),
			@AttributeOverride(name = "min", column = @Column(name = "delai_min")),
			@AttributeOverride(name = "max", column = @Column(name = "delai_max")),
			@AttributeOverride(name = "unite", column = @Column(name = "delai_unite"))
	})
	public Delai delai;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "type", column = @Column(name = "delai_sa_type")),
			@AttributeOverride(name = "min", column = @Column(name = "delai_sa_min")),
			@AttributeOverride(name = "max", column = @Column(name = "delai_sa_max")),
			@AttributeOverride(name = "unite", column = @Column(name = "delai_sa_unite"))
	})
	public Delai delaiSA;

	public void preSave() {
		if (conditions != null) {
			for (final Condition condition : conditions) {
				condition.chapitre = this;
			}
		}
	}

	@Override
	public String toString() {
		return "Chapitre[id=" + id + ",checklistId=" + (checklist == null ? "null" : checklist.id) + "," + titre + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (closedInEdition ? 1231 : 1237);
		result = prime * result + ((conditions == null) ? 0 : conditions.hashCode());
		result = prime * result + ((delai == null) ? 0 : delai.hashCode());
		result = prime * result + ((delaiSA == null) ? 0 : delaiSA.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((texteActions == null) ? 0 : texteActions.hashCode());
		result = prime * result + ((texteInfos == null) ? 0 : texteInfos.hashCode());
		result = prime * result + ((texteModalites == null) ? 0 : texteModalites.hashCode());
		result = prime * result + ((titre == null) ? 0 : titre.hashCode());
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
		final Chapitre other = (Chapitre) obj;
		if (closedInEdition != other.closedInEdition)
			return false;
		if (conditions == null) {
			if (other.conditions != null)
				return false;
		} else if (!conditions.equals(other.conditions))
			return false;
		if (delai == null) {
			if (other.delai != null)
				return false;
		} else if (!delai.equals(other.delai))
			return false;
		if (delaiSA == null) {
			if (other.delaiSA != null)
				return false;
		} else if (!delaiSA.equals(other.delaiSA))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (texteActions == null) {
			if (other.texteActions != null)
				return false;
		} else if (!texteActions.equals(other.texteActions))
			return false;
		if (texteInfos == null) {
			if (other.texteInfos != null)
				return false;
		} else if (!texteInfos.equals(other.texteInfos))
			return false;
		if (texteModalites == null) {
			if (other.texteModalites != null)
				return false;
		} else if (!texteModalites.equals(other.texteModalites))
			return false;
		if (titre == null) {
			if (other.titre != null)
				return false;
		} else if (!titre.equals(other.titre))
			return false;
		return true;
	}

}
