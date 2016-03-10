package models;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Delai {

	public static enum Type {
		AUCUN, DESQUE, AUPLUS, ENTRE, APARTIR, SIMPLE
	};

	public static enum Unite {
		MOIS, ANNEES
	};

	public Delai() {
	}

	public Delai(final Type type, final int min, final int max, final Unite unite) {
		super();
		this.type = type;
		this.min = min;
		this.max = max;
		this.unite = unite;
	}

	@Enumerated(EnumType.STRING)
	public Type type;

	public int min;

	public int max;

	@Enumerated(EnumType.STRING)
	public Unite unite;

	@Override
	public String toString() {
		return "Delai[type=" + type + ", min=" + min + ", max=" + max + ", unite=" + unite + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + max;
		result = prime * result + min;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((unite == null) ? 0 : unite.hashCode());
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
		final Delai other = (Delai) obj;
		if (max != other.max)
			return false;
		if (min != other.min)
			return false;
		if (type != other.type)
			return false;
		if (unite != other.unite)
			return false;
		return true;
	}

}
