package utils.data;

import java.util.List;

import models.Caisse;

public class CaisseForEdition extends Caisse {

	public List<String> departements;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((departements == null) ? 0 : departements.hashCode());
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
		final CaisseForEdition other = (CaisseForEdition) obj;
		if (departements == null) {
			if (other.departements != null)
				return false;
		} else if (!departements.equals(other.departements))
			return false;
		return true;
	}

}
