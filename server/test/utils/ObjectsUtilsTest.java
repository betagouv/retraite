package utils;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.assertions.Assertions;
import org.junit.Test;

import models.Caisse;
import utils.data.CaisseForEdition;

public class ObjectsUtilsTest {

	@Test
	public void test_copyFields() throws Exception {

		final Caisse from = new Caisse();
		from.id = 99L;
		from.nom = "nom";
		from.adresse1 = "adresse1";
		from.adresse2 = "adresse2";
		from.telephone = "telephone";
		from.site = "site";
		from.linkLabel1 = "linkLabel";
		from.linkUrl1 = "linkUrl";
		final CaisseForEdition to = new CaisseForEdition();

		ObjectsUtils.copyFields(from, to);

		assertThat(to.id).isEqualTo(99L);
		assertThat(to.nom).isEqualTo("nom");
		assertThat(to.adresse1).isEqualTo("adresse1");
		assertThat(to.adresse2).isEqualTo("adresse2");
		assertThat(to.telephone).isEqualTo("telephone");
		assertThat(to.site).isEqualTo("site");
		assertThat(to.linkLabel1).isEqualTo("linkLabel");
		assertThat(to.linkUrl1).isEqualTo("linkUrl");
	}

}
