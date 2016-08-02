package utils.dao;

import static play.test.Fixtures.deleteDatabase;
import static play.test.Fixtures.loadModels;
import static utils.engine.data.enums.ChecklistName.CNAV;
import static utils.engine.data.enums.ChecklistName.MSA;

import org.junit.Before;
import org.junit.Test;

import models.Caisse;
import models.CaisseDepartementale;
import utils.RetraiteUnitTestBase;

public class CaisseDepartementaleDaoTest extends RetraiteUnitTestBase {

	private CaisseDepartementaleDao dao;

	@Before
	public void setUp() throws Exception {
		deleteDatabase();
		loadModels("utils/dao/CaisseDepartementaleDaoTest.yml");

		dao = new CaisseDepartementaleDao();
	}

	@Test
	public void deleteDepartement_should_delete_CaisseDepartementale() {

		// Avant
		assertThat(CaisseDepartementale.count()).isEqualTo(5);
		assertThat(CaisseDepartementale.find("byChecklistName", CNAV).fetch()).hasSize(4);
		assertThat(CaisseDepartementale.find("byChecklistNameAndDepartement", CNAV, "14").fetch()).hasSize(1);
		assertThat(Caisse.count()).isEqualTo(3);

		dao.deleteDepartement(retrieveCaisse("Caisse CNAV 2").id, "14");

		// Après : 1 CaisseDepartementale en moins pour CNAV et 14
		assertThat(CaisseDepartementale.count()).isEqualTo(4);
		assertThat(CaisseDepartementale.find("byChecklistName", CNAV).fetch()).hasSize(3);
		assertThat(CaisseDepartementale.find("byChecklistNameAndDepartement", CNAV, "14").fetch()).hasSize(0);
		assertThat(Caisse.count()).isEqualTo(3);
	}

	@Test
	public void deleteDepartement_should_delete_CaisseDepartementale_and_Caisse_if_not_more_used() {

		// Avant
		assertThat(CaisseDepartementale.count()).isEqualTo(5);
		assertThat(CaisseDepartementale.find("byChecklistName", MSA).fetch()).hasSize(1);
		assertThat(CaisseDepartementale.find("byChecklistNameAndDepartement", MSA, "05").fetch()).hasSize(1);
		assertThat(Caisse.count()).isEqualTo(3);

		dao.deleteDepartement(retrieveCaisse("Caisse MSA 1").id, "05");

		// Après : 1 CaisseDepartementale en moins pour CNAV et 14
		assertThat(CaisseDepartementale.count()).isEqualTo(4);
		assertThat(CaisseDepartementale.find("byChecklistName", MSA).fetch()).hasSize(0);
		assertThat(CaisseDepartementale.find("byChecklistNameAndDepartement", MSA, "05").fetch()).hasSize(0);
		assertThat(Caisse.count()).isEqualTo(2);
	}

	private Caisse retrieveCaisse(final String name) {
		return Caisse.find("byNom", name).first();
	}
}
