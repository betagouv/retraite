package controllers.data;

import controllers.utils.Look;
import utils.engine.data.CommonExchangeData;

public class PostData extends CommonExchangeData {

	public String nom;
	public String naissance;
	public String nir;
	public String departement;
	public String liquidateurReponseJsonStr;
	public boolean departInconnu;
	public String departMois;
	public String departAnnee;
	public String email;
	public Look look;
	public boolean isForce55;

	@Override
	public String toString() {
		return "PostData [nom=" + nom + ", naissance=" + naissance + ", nir=" + nir + ", departement=" + departement + ", liquidateurReponseJsonStr="
				+ liquidateurReponseJsonStr + ", departInconnu=" + departInconnu + ", departMois=" + departMois + ", departAnnee=" + departAnnee + ", email="
				+ email + ", look=" + look + ", isForce55=" + isForce55 + ", hidden_step=" + hidden_step + ", hidden_nom=" + hidden_nom + ", hidden_naissance="
				+ hidden_naissance + ", hidden_nir=" + hidden_nir + ", hidden_departement=" + hidden_departement + ", hidden_regimes=" + hidden_regimes
				+ ", hidden_regimesInfosJsonStr=" + hidden_regimesInfosJsonStr + ", hidden_liquidateurReponseJsonStr=" + hidden_liquidateurReponseJsonStr
				+ ", hidden_departMois=" + hidden_departMois + ", hidden_departAnnee=" + hidden_departAnnee + ", hidden_attestationCarriereLongue="
				+ hidden_attestationCarriereLongue + ", hidden_liquidateurStep=" + hidden_liquidateurStep + ", hidden_liquidateur=" + hidden_liquidateur
				+ ", hidden_userStatus=" + hidden_userStatus + ", hidden_liquidateurReponsesHistory=" + hidden_liquidateurReponsesHistory + ", isPDF=" + isPDF
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((departAnnee == null) ? 0 : departAnnee.hashCode());
		result = prime * result + (departInconnu ? 1231 : 1237);
		result = prime * result + ((departMois == null) ? 0 : departMois.hashCode());
		result = prime * result + ((departement == null) ? 0 : departement.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((liquidateurReponseJsonStr == null) ? 0 : liquidateurReponseJsonStr.hashCode());
		result = prime * result + ((look == null) ? 0 : look.hashCode());
		result = prime * result + ((naissance == null) ? 0 : naissance.hashCode());
		result = prime * result + ((nir == null) ? 0 : nir.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
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
		final PostData other = (PostData) obj;
		if (departAnnee == null) {
			if (other.departAnnee != null)
				return false;
		} else if (!departAnnee.equals(other.departAnnee))
			return false;
		if (departInconnu != other.departInconnu)
			return false;
		if (departMois == null) {
			if (other.departMois != null)
				return false;
		} else if (!departMois.equals(other.departMois))
			return false;
		if (departement == null) {
			if (other.departement != null)
				return false;
		} else if (!departement.equals(other.departement))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (liquidateurReponseJsonStr == null) {
			if (other.liquidateurReponseJsonStr != null)
				return false;
		} else if (!liquidateurReponseJsonStr.equals(other.liquidateurReponseJsonStr))
			return false;
		if (look != other.look)
			return false;
		if (naissance == null) {
			if (other.naissance != null)
				return false;
		} else if (!naissance.equals(other.naissance))
			return false;
		if (nir == null) {
			if (other.nir != null)
				return false;
		} else if (!nir.equals(other.nir))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		return true;
	}

}
