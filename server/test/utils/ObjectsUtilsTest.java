package utils;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import controllers.data.PostData;
import models.Caisse;
import utils.data.CaisseForEdition;
import utils.engine.data.RenderData;

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

	@Test
	public void test_copyHiddenFields() throws Exception {

		final PostData postData = new PostData();
		postData.hidden_departAnnee = "2020";
		postData.hidden_departMois = "7";

		final RenderData renderData = new RenderData();

		ObjectsUtils.copyHiddenFieldsExceptStep(postData, renderData);

		assertThat(renderData.hidden_departAnnee).isEqualTo("2020");
		assertThat(renderData.hidden_departMois).isEqualTo("7");
	}

	@Test
	public void test_synchronizedHiddenAndNotHiddenFields() throws Exception {

		final PostData postData = new PostData();
		postData.nir = "1998877123456";
		postData.hidden_nir = "";
		postData.departAnnee = "2020";
		postData.hidden_departMois = "7";

		ObjectsUtils.synchronizedHiddenAndNotHiddenFields(postData);

		assertThat(postData.nir).isEqualTo("1998877123456");
		assertThat(postData.hidden_nir).isEqualTo("1998877123456");
		assertThat(postData.departAnnee).isEqualTo("2020");
		assertThat(postData.hidden_departAnnee).isEqualTo("2020");
		assertThat(postData.departMois).isEqualTo("7");
		assertThat(postData.hidden_departMois).isEqualTo("7");
	}

}
