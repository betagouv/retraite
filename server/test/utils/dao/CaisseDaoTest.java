package utils.dao;

import static play.test.Fixtures.deleteDatabase;
import static play.test.Fixtures.loadModels;
import static utils.engine.data.enums.ChecklistName.CNAV;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import models.Caisse;
import utils.RetraiteUnitTestBase;
import utils.data.CaisseForEdition;

public class CaisseDaoTest extends RetraiteUnitTestBase {

	private CaisseDao dao;

	@Before
	public void setUp() throws Exception {
		deleteDatabase();
		loadModels("utils/dao/res/CaisseDaoTest.yml");

		dao = new CaisseDao();
	}

	@Test
	public void test_findCaissesList() {

		final List<Caisse> caisses = dao.findCaissesList(CNAV);

		assertThat(caisses).hasSize(2);
	}

	@Test
	public void test_findCaissesListForEdition() {

		final List<CaisseForEdition> caisses = dao.findCaissesListForEdition(CNAV);

		assertThat(caisses).hasSize(2);
		final CaisseForEdition caisseCnav1 = caisses.get(0);
		assertThat(caisseCnav1.nom).isEqualTo("Caisse CNAV 1");
		assertThat(caisseCnav1.departements).containsExactly("05", "13");
	}
}
