package utils.doc;

import static models.Delai.Unite.ANNEES;
import static models.Delai.Unite.MOIS;
import static org.fest.assertions.Assertions.assertThat;
import static utils.TestsUtilsDelai.createDelaiAPartir;
import static utils.TestsUtilsDelai.createDelaiAuPlus;
import static utils.TestsUtilsDelai.createDelaiAucun;
import static utils.TestsUtilsDelai.createDelaiDesQuePossible;
import static utils.TestsUtilsDelai.createDelaiEntre;
import static utils.TestsUtilsDelai.createDelaiSimple;

import org.junit.Before;
import org.junit.Test;

public class ConverterDelaiTest {

	private ConverterDelai converterDelai;

	@Before
	public void setUp() throws Exception {
		converterDelai = new ConverterDelai();
	}

	@Test
	public void should_convert() {

		assertThat(converterDelai.convert(null)).isEqualTo("Non défini !");

		assertThat(converterDelai.convert(createDelaiAucun())).isEqualTo("Aucun");

		assertThat(converterDelai.convert(createDelaiDesQuePossible())).isEqualTo("Dès que possible");

		assertThat(converterDelai.convert(createDelaiAuPlus(3, MOIS))).isEqualTo("Au plus tard 3 mois avant la date de départ prévue");
		assertThat(converterDelai.convert(createDelaiAuPlus(8, ANNEES))).isEqualTo("Au plus tard 8 années avant la date de départ prévue");
		assertThat(converterDelai.convert(createDelaiAuPlus(1, ANNEES))).isEqualTo("Au plus tard 1 année avant la date de départ prévue");

		assertThat(converterDelai.convert(createDelaiEntre(2, 3, MOIS))).isEqualTo("Entre 2 et 3 mois avant la date de départ prévue");
		assertThat(converterDelai.convert(createDelaiEntre(8, 9, ANNEES))).isEqualTo("Entre 8 et 9 années avant la date de départ prévue");

		assertThat(converterDelai.convert(createDelaiAPartir(3, MOIS))).isEqualTo("A partir de 3 mois avant la date de départ prévue");
		assertThat(converterDelai.convert(createDelaiAPartir(1, MOIS))).isEqualTo("A partir de 1 mois avant la date de départ prévue");
		assertThat(converterDelai.convert(createDelaiAPartir(3, ANNEES))).isEqualTo("A partir de 3 années avant la date de départ prévue");
		assertThat(converterDelai.convert(createDelaiAPartir(1, ANNEES))).isEqualTo("A partir de 1 année avant la date de départ prévue");

		assertThat(converterDelai.convert(createDelaiSimple(3, MOIS))).isEqualTo("A 3 mois avant la date de départ prévue");
		assertThat(converterDelai.convert(createDelaiSimple(3, ANNEES))).isEqualTo("A 3 années avant la date de départ prévue");
		assertThat(converterDelai.convert(createDelaiSimple(1, ANNEES))).isEqualTo("A 1 année avant la date de départ prévue");
	}
}
