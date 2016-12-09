package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

import models.Caisse;
import utils.engine.data.UserCaisse;
import utils.engine.data.enums.LinkType;

public class UserCaisseComputerTest {

	private UserCaisseComputer userCaisseComputer;
	
	@Before
	public void setUp() {
		userCaisseComputer = new UserCaisseComputer();
	}
	
	@Test
	public void should_return_userCaisse_with_one_link() {
		
		Caisse caisse = new Caisse();
		caisse.adresse1 = "Mon adresse";
		caisse.linkUrl1 = "http://www.monsite.com/page.html";
		caisse.linkLabel1 = "Mon label 1";
		caisse.setLinkType1(LinkType.TELEPHONE.name());
		
		UserCaisse userCaisse = userCaisseComputer.compute(caisse);
		
		assertThat(userCaisse).isNotNull();
		assertThat(userCaisse.adresse1).isEqualTo(caisse.adresse1);
		assertThat(userCaisse.linkList).isNotEmpty();
		assertThat(userCaisse.linkList.get(0).getUrl()).isEqualTo(caisse.linkUrl1);
		assertThat(userCaisse.linkList.get(0).getType()).isEqualTo(LinkType.TELEPHONE);
		assertThat(userCaisse.urls).containsExactly("monsite.com/page.html");
	}

	
	@Test
	public void should_return_userCaisse_with_two_links() {
		
		Caisse caisse = new Caisse();
		caisse.adresse1 = "Mon adresse";
		caisse.linkUrl1 = "http://www.monsite.com/page.html";
		caisse.linkLabel1 = "Mon label 1";
		caisse.setLinkType1(LinkType.TELEPHONE.name());
		caisse.adresse3 = "Mon adresse 3";
		caisse.linkUrl3 = "http3://monsite.com/prive";
		caisse.linkLabel3 = "Mon label 3";
		caisse.setLinkType1(LinkType.ADRESSE.name());
		
		UserCaisse userCaisse = userCaisseComputer.compute(caisse);
		
		assertThat(userCaisse).isNotNull();
		assertThat(userCaisse.adresse1).isEqualTo(caisse.adresse1);
		assertThat(userCaisse.linkList).isNotEmpty();
		assertThat(userCaisse.linkList.size()).isEqualTo(2);
	}
	
}
