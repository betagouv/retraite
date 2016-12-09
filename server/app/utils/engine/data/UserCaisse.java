package utils.engine.data;

import java.util.List;

import utils.engine.data.enums.LinkType;

public class UserCaisse {

	public Long id;
	
	public String nom;

	public String adresse1;

	public String adresse2;

	public String adresse3;

	public String adresse4;

	public String telephone;

	public String fax;

	public String site;
	
	public List<UserLink> linkList;

	public List<String> urls;

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

	public List<UserLink> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<UserLink> linkList) {
		this.linkList = linkList;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}


}
