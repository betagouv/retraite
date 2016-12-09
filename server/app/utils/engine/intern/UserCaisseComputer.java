package utils.engine.intern;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;

import models.Caisse;
import utils.engine.data.UserCaisse;
import utils.engine.utils.LinkConverter;

public class UserCaisseComputer {

	private final DozerBeanMapper dozerBeanMapper;
	
	public UserCaisseComputer() {
		dozerBeanMapper = new DozerBeanMapper();
	}
	
	public UserCaisse compute(Caisse caisse) {

		UserCaisse userCaisse = dozerBeanMapper.map(caisse, UserCaisse.class);
		
		List<String> urls = new ArrayList<String>();
		String[] urlLinks = {caisse.linkUrl1, caisse.linkUrl2, caisse.linkUrl3};
		
		for (String urlLink : urlLinks) {
			if (urlLink != null) {
				urls.add(LinkConverter.convertTextForLink(urlLink, Boolean.FALSE));
			}
		}
		
		userCaisse.urls = urls;
		
		return userCaisse;
		
	}

}
