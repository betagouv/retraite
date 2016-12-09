package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import play.db.jpa.GenericModel;

@Entity
public class Caisse extends GenericModel {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="caisse_generator")
	@SequenceGenerator(name="caisse_generator", sequenceName="caisse_id_seq")
	public Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse1() {
		return adresse1;
	}

	public void setAdresse1(String adresse1) {
		this.adresse1 = adresse1;
	}

	public String getAdresse2() {
		return adresse2;
	}

	public void setAdresse2(String adresse2) {
		this.adresse2 = adresse2;
	}

	public String getAdresse3() {
		return adresse3;
	}

	public void setAdresse3(String adresse3) {
		this.adresse3 = adresse3;
	}

	public String getAdresse4() {
		return adresse4;
	}

	public void setAdresse4(String adresse4) {
		this.adresse4 = adresse4;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getLinkLabel1() {
		return linkLabel1;
	}

	public void setLinkLabel1(String linkLabel1) {
		this.linkLabel1 = linkLabel1;
	}

	public String getLinkUrl1() {
		return linkUrl1;
	}

	public void setLinkUrl1(String linkUrl1) {
		this.linkUrl1 = linkUrl1;
	}

	public String getLinkLabel2() {
		return linkLabel2;
	}

	public void setLinkLabel2(String linkLabel2) {
		this.linkLabel2 = linkLabel2;
	}

	public String getLinkUrl2() {
		return linkUrl2;
	}

	public void setLinkUrl2(String linkUrl2) {
		this.linkUrl2 = linkUrl2;
	}

	public String getLinkLabel3() {
		return linkLabel3;
	}

	public void setLinkLabel3(String linkLabel3) {
		this.linkLabel3 = linkLabel3;
	}

	public String getLinkUrl3() {
		return linkUrl3;
	}

	public void setLinkUrl3(String linkUrl3) {
		this.linkUrl3 = linkUrl3;
	}

	public String nom;

	public String adresse1;

	public String adresse2;

	public String adresse3;

	public String adresse4;

	public String telephone;

	public String fax;

	public String site;

	public String linkLabel1;

	public String linkUrl1;

	public String linkLabel2;

	public String linkUrl2;

	public String linkLabel3;

	public String linkUrl3;

	@Override
	public String toString() {
		return "Caisse[id=" + id + ",nom=" + nom + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((adresse1 == null) ? 0 : adresse1.hashCode());
		result = prime * result + ((adresse2 == null) ? 0 : adresse2.hashCode());
		result = prime * result + ((adresse3 == null) ? 0 : adresse3.hashCode());
		result = prime * result + ((adresse4 == null) ? 0 : adresse4.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((linkLabel1 == null) ? 0 : linkLabel1.hashCode());
		result = prime * result + ((linkLabel2 == null) ? 0 : linkLabel2.hashCode());
		result = prime * result + ((linkLabel3 == null) ? 0 : linkLabel3.hashCode());
		result = prime * result + ((linkUrl1 == null) ? 0 : linkUrl1.hashCode());
		result = prime * result + ((linkUrl2 == null) ? 0 : linkUrl2.hashCode());
		result = prime * result + ((linkUrl3 == null) ? 0 : linkUrl3.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result + ((telephone == null) ? 0 : telephone.hashCode());
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
		final Caisse other = (Caisse) obj;
		if (adresse1 == null) {
			if (other.adresse1 != null)
				return false;
		} else if (!adresse1.equals(other.adresse1))
			return false;
		if (adresse2 == null) {
			if (other.adresse2 != null)
				return false;
		} else if (!adresse2.equals(other.adresse2))
			return false;
		if (adresse3 == null) {
			if (other.adresse3 != null)
				return false;
		} else if (!adresse3.equals(other.adresse3))
			return false;
		if (adresse4 == null) {
			if (other.adresse4 != null)
				return false;
		} else if (!adresse4.equals(other.adresse4))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (linkLabel1 == null) {
			if (other.linkLabel1 != null)
				return false;
		} else if (!linkLabel1.equals(other.linkLabel1))
			return false;
		if (linkLabel2 == null) {
			if (other.linkLabel2 != null)
				return false;
		} else if (!linkLabel2.equals(other.linkLabel2))
			return false;
		if (linkLabel3 == null) {
			if (other.linkLabel3 != null)
				return false;
		} else if (!linkLabel3.equals(other.linkLabel3))
			return false;
		if (linkUrl1 == null) {
			if (other.linkUrl1 != null)
				return false;
		} else if (!linkUrl1.equals(other.linkUrl1))
			return false;
		if (linkUrl2 == null) {
			if (other.linkUrl2 != null)
				return false;
		} else if (!linkUrl2.equals(other.linkUrl2))
			return false;
		if (linkUrl3 == null) {
			if (other.linkUrl3 != null)
				return false;
		} else if (!linkUrl3.equals(other.linkUrl3))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		if (telephone == null) {
			if (other.telephone != null)
				return false;
		} else if (!telephone.equals(other.telephone))
			return false;
		return true;
	}

}
