package utils.wsinforetraite;

import java.util.Arrays;

public class InfoRetraiteResult {

	public enum Status {
		FOUND, NOTFOUND, ERROR
	};

	public final Status status;

	public final InfoRetraiteResultRegime[] regimes;

	public InfoRetraiteResult(final Status status, final InfoRetraiteResultRegime[] regimes) {
		this.status = status;
		this.regimes = regimes;
	}

	public String extractRegimes() {
		String regimesStr = "";
		for (final InfoRetraiteResultRegime infoRetraiteResultRegime : regimes) {
			if (!regimesStr.isEmpty()) {
				regimesStr += ",";
			}
			regimesStr += infoRetraiteResultRegime.nom.trim();
		}
		return regimesStr;
	}

	@Override
	public String toString() {
		return "InfoRetraiteResult[status=" + status + ",items=" + Arrays.toString(regimes) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(regimes);
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
		final InfoRetraiteResult other = (InfoRetraiteResult) obj;
		if (!Arrays.equals(regimes, other.regimes))
			return false;
		return true;
	}

	public static class InfoRetraiteResultRegime {
		public String image;
		public String regime;
		public String nom;
		public String adresse;
		public String internet;
		public String tel1;
		public String tel2;
		public String fax;
		public String email1;
		public String email2;

		@Override
		public String toString() {
			return "InfoRetraiteResultItem[image=" + image + ", regime=" + regime + ", nom=" + nom + ", adresse=" + adresse + ", internet=" + internet
					+ ", tel1=" + tel1 + ", tel2=" + tel2 + ", fax=" + fax + ", email1=" + email1 + ", email2=" + email2 + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((adresse == null) ? 0 : adresse.hashCode());
			result = prime * result + ((email1 == null) ? 0 : email1.hashCode());
			result = prime * result + ((email2 == null) ? 0 : email2.hashCode());
			result = prime * result + ((fax == null) ? 0 : fax.hashCode());
			result = prime * result + ((image == null) ? 0 : image.hashCode());
			result = prime * result + ((internet == null) ? 0 : internet.hashCode());
			result = prime * result + ((nom == null) ? 0 : nom.hashCode());
			result = prime * result + ((regime == null) ? 0 : regime.hashCode());
			result = prime * result + ((tel1 == null) ? 0 : tel1.hashCode());
			result = prime * result + ((tel2 == null) ? 0 : tel2.hashCode());
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
			final InfoRetraiteResultRegime other = (InfoRetraiteResultRegime) obj;
			if (adresse == null) {
				if (other.adresse != null)
					return false;
			} else if (!adresse.equals(other.adresse))
				return false;
			if (email1 == null) {
				if (other.email1 != null)
					return false;
			} else if (!email1.equals(other.email1))
				return false;
			if (email2 == null) {
				if (other.email2 != null)
					return false;
			} else if (!email2.equals(other.email2))
				return false;
			if (fax == null) {
				if (other.fax != null)
					return false;
			} else if (!fax.equals(other.fax))
				return false;
			if (image == null) {
				if (other.image != null)
					return false;
			} else if (!image.equals(other.image))
				return false;
			if (internet == null) {
				if (other.internet != null)
					return false;
			} else if (!internet.equals(other.internet))
				return false;
			if (nom == null) {
				if (other.nom != null)
					return false;
			} else if (!nom.equals(other.nom))
				return false;
			if (regime == null) {
				if (other.regime != null)
					return false;
			} else if (!regime.equals(other.regime))
				return false;
			if (tel1 == null) {
				if (other.tel1 != null)
					return false;
			} else if (!tel1.equals(other.tel1))
				return false;
			if (tel2 == null) {
				if (other.tel2 != null)
					return false;
			} else if (!tel2.equals(other.tel2))
				return false;
			return true;
		}

	}
}
