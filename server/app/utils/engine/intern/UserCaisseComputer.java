package utils.engine.intern;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;

import models.Caisse;
import utils.engine.data.UserCaisse;
import utils.engine.data.UserLink;
import utils.engine.data.enums.LinkType;
import utils.engine.utils.LinkConverter;

public class UserCaisseComputer {

	private final DozerBeanMapper dozerBeanMapper;
	
	public UserCaisseComputer() {
		dozerBeanMapper = new DozerBeanMapper();
	}
	
	public UserCaisse compute(Caisse caisse) {

		UserCaisse userCaisse = null;
		
		if (caisse != null) {
		
			userCaisse = dozerBeanMapper.map(caisse, UserCaisse.class);
			
			List<UserLink> linkList = new ArrayList<UserLink>();
			if (caisse.linkUrl1 != null && caisse.linkLabel1 != null) {
				linkList.add(new UserLink(caisse.linkLabel1, caisse.linkUrl1, caisse.linkType1));
			}
			if (caisse.linkUrl2 != null && caisse.linkLabel2 != null) {
				linkList.add(new UserLink(caisse.linkLabel2, caisse.linkUrl2, caisse.linkType2));
			}
			if (caisse.linkUrl3 != null && caisse.linkLabel3 != null) {
				linkList.add(new UserLink(caisse.linkLabel3, caisse.linkUrl3, caisse.linkType3));
			}
			userCaisse.setLinkList(linkList);
			
			List<String> urls = new ArrayList<String>();
			
			for (UserLink userLink : userCaisse.getLinkList()) {
				urls.add(LinkConverter.convertTextForLink(userLink.getUrl(), Boolean.FALSE));
			}		
			
			userCaisse.urls = urls;
		
		}
		
		return userCaisse;
		
	}
	
	public List<UserCaisse> compute (List<Caisse> listCaisse) {

		List<UserCaisse> listUserCaisse = null;
		if (listCaisse != null) {
			listUserCaisse = new ArrayList<UserCaisse>();
			for (Caisse caisse : listCaisse) {
				listUserCaisse.add(compute(caisse));
			}
		}
		return listUserCaisse;
		
	}

}
