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
	public String texteIntro;

	public boolean parcoursDematDifferent;

	@Column(columnDefinition = "text")
	public String parcours;

	@Column(columnDefinition = "text")
	public String parcoursDemat;

	@Column(columnDefinition = "text")
	public String texteComplementaire;

	@Column(columnDefinition = "text")
	public String notes;

	@OneToMany(mappedBy = "chapitre", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Condition> conditions;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "type", column = @Column(name = "delai_type") ),
			@AttributeOverride(name = "min", column = @Column(name = "delai_min") ),
			@AttributeOverride(name = "max", column = @Column(name = "delai_max") ),
			@AttributeOverride(name = "unite", column = @Column(name = "delai_unite") )
	})
	public Delai delai;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "type", column = @Column(name = "delai_sa_type") ),
			@AttributeOverride(name = "min", column = @Column(name = "delai_sa_min") ),
			@AttributeOverride(name = "max", column = @Column(name = "delai_sa_max") ),
			@AttributeOverride(name = "unite", column = @Column(name = "delai_sa_unite") )
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
		return "Chapitre[" + id + "," + (checklist == null ? "null" : checklist.id) + "," + titre + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((parcours == null) ? 0 : parcours.hashCode());
		result = prime * result + ((parcoursDemat == null) ? 0 : parcoursDemat.hashCode());
		result = prime * result + (parcoursDematDifferent ? 1231 : 1237);
		result = prime * result + ((texteComplementaire == null) ? 0 : texteComplementaire.hashCode());
		result = prime * result + ((texteIntro == null) ? 0 : texteIntro.hashCode());
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
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (parcours == null) {
			if (other.parcours != null)
				return false;
		} else if (!parcours.equals(other.parcours))
			return false;
		if (parcoursDemat == null) {
			if (other.parcoursDemat != null)
				return false;
		} else if (!parcoursDemat.equals(other.parcoursDemat))
			return false;
		if (parcoursDematDifferent != other.parcoursDematDifferent)
			return false;
		if (texteComplementaire == null) {
			if (other.texteComplementaire != null)
				return false;
		} else if (!texteComplementaire.equals(other.texteComplementaire))
			return false;
		if (texteIntro == null) {
			if (other.texteIntro != null)
				return false;
		} else if (!texteIntro.equals(other.texteIntro))
			return false;
		if (titre == null) {
			if (other.titre != null)
				return false;
		} else if (!titre.equals(other.titre))
			return false;
		return true;
	}

}
