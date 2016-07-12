package utils.engine.intern;

import static models.Delai.Type.APARTIR;
import static models.Delai.Type.AUCUN;
import static models.Delai.Type.AUPLUS;
import static models.Delai.Type.DESQUE;
import static models.Delai.Type.ENTRE;
import static models.Delai.Type.SIMPLE;
import static models.Delai.Unite.MOIS;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import models.Delai;
import utils.engine.DateProviderFake;
import utils.engine.data.MonthAndYear;

public class UserChecklistDelaiComputerTest {

	private UserChecklistDelaiComputer userChecklistDelaiComputer;

	@Before
	public void setUp() {
		userChecklistDelaiComputer = new UserChecklistDelaiComputer(new DateProviderFake(5, 2, 2016));
	}

	// Cas particuliers

	@Test
	public void delai_null() {
		final Delai delai = null;
		final MonthAndYear dateDepart = null;

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("");
	}

	@Test
	public void aucun() {
		final Delai delai = new Delai(AUCUN, 0, 0, null);
		final MonthAndYear dateDepart = null;

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("");
	}

	// "Dès que possible"

	@Test
	public void des_que_possible() {
		final Delai delai = new Delai(DESQUE, 0, 0, null);
		final MonthAndYear dateDepart = null;

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("Dès que possible");
	}

	// "Au plus tard x mois/années"

	@Test
	public void au_plus_1_mois_sans_chgt_annee() {
		final Delai delai = new Delai(AUPLUS, 1, 1, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("03", "2017");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("Au plus tard<br/>fin janvier 2017");
	}

	@Test
	public void au_plus_3_mois_sans_chgt_annee() {
		final Delai delai = new Delai(AUPLUS, 3, 3, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("10", "2017");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("Au plus tard<br/>fin juin 2017");
	}

	@Test
	public void au_plus_3_mois_avec_chgt_annee() {
		final Delai delai = new Delai(AUPLUS, 3, 3, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("2", "2017");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("Au plus tard<br/>fin octobre 2016");
	}

	@Test
	public void au_plus_3_mois_depasse() {
		final Delai delai = new Delai(AUPLUS, 3, 3, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("5", "2016");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("Le plus tôt possible");
	}

	// "Entre x et y mois/années"

	@Test
	public void entre_2_et_4_mois_sans_chgt_annee() {
		final Delai delai = new Delai(ENTRE, 2, 4, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("10", "2017");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("De juin à fin juillet 2017");
	}

	@Test
	public void entre_4_et_2_mois_sans_chgt_annee_nombres_inverses() {
		final Delai delai = new Delai(ENTRE, 4, 2, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("10", "2017");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("De juin à fin juillet 2017");
	}

	@Test
	public void entre_2_et_4_mois_a_cheval_sur_2_annees() {
		final Delai delai = new Delai(ENTRE, 1, 3, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("3", "2017");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("De décembre 2016<br/>à fin janvier 2017");
	}

	@Test
	public void entre_2_et_4_mois_depasse_partiellement() {
		final Delai delai = new Delai(ENTRE, 2, 4, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("6", "2016");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("Au plus tard<br/>fin mars 2016");
	}

	@Test
	public void entre_2_et_4_mois_depasse_totalement() {
		final Delai delai = new Delai(ENTRE, 2, 4, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("4", "2016");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("Le plus tôt possible");
	}

	@Test
	public void entre_1_et_2_mois_doit_afficher_en_xxxx() {
		final Delai delai = new Delai(ENTRE, 1, 2, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("10", "2017");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("En août 2017");
	}

	@Test
	public void entre_1_et_2_mois_doit_afficher_en_xxxx_depasse() {
		final Delai delai = new Delai(ENTRE, 1, 2, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("3", "2016");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("Le plus tôt possible");
	}

	@Test
	public void entre_1_et_1_mois_doit_afficher_en_xxxx() {
		final Delai delai = new Delai(ENTRE, 1, 1, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("10", "2017");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("En septembre 2017");
	}

	@Test
	public void entre_1_et_1_mois_doit_afficher_en_xxxx_depasse() {
		final Delai delai = new Delai(ENTRE, 1, 1, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("3", "2016");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("Le plus tôt possible");
	}

	// "A partir de x mois/années"

	@Test
	public void a_partir_de_1_mois_sans_chgt_annee() {
		final Delai delai = new Delai(APARTIR, 1, 1, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("03", "2017");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("A partir de<br/>février 2017");
	}

	@Test
	public void a_partir_de_1_mois_sans_chgt_annee_depasse() {
		final Delai delai = new Delai(APARTIR, 1, 1, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("03", "2016");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("Le plus tôt possible");
	}

	// "A x mois/années"

	@Test
	public void a_1_mois_sans_chgt_annee() {
		final Delai delai = new Delai(SIMPLE, 1, 1, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("03", "2017");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("En février 2017");
	}

	@Test
	public void a_1_mois_sans_chgt_annee_depasse() {
		final Delai delai = new Delai(SIMPLE, 1, 1, MOIS);
		final MonthAndYear dateDepart = new MonthAndYear("03", "2016");

		final String result = userChecklistDelaiComputer.compute(delai, dateDepart);

		assertThat(result).isEqualTo("Le plus tôt possible");
	}
}
