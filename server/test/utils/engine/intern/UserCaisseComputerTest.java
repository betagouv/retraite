package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;

import models.Caisse;
import utils.engine.data.UserCaisse;

public class UserCaisseComputerTest {

	private UserCaisseComputer userCaisseComputer;
	
	@Before
	public void setUp() {
		userCaisseComputer = new UserCaisseComputer();
	}
	
	@Test
	public void should_return_userCaisse_with_one_url() {
		
		Caisse caisse = new Caisse();
		caisse.adresse1 = "Mon adresse";
		caisse.linkUrl1 = "http://www.monsite.com/page.html";
		
		UserCaisse userCaisse = userCaisseComputer.compute(caisse);
		
		assertThat(userCaisse).isNotNull();
		assertThat(userCaisse.adresse1).isEqualTo(caisse.adresse1);
		assertThat(userCaisse.linkUrl1).isEqualTo(caisse.linkUrl1);
		assertThat(userCaisse.urls).containsExactly("monsite.com/page.html");
	}
	
}
