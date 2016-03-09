package utils.engine.utils;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import models.AnneesEtMois;
import models.PeriodeDepartLegal;
import utils.dao.PeriodeDepartLegalDao;

public class AgeLegalEvaluatorTest {

	private PeriodeDepartLegalDao periodeDepartLegalDaoMock;

	private AgeLegalEvaluator ageLegalEvaluator;

	@Before
	public void setUp() {
		periodeDepartLegalDaoMock = mock(PeriodeDepartLegalDao.class);
		ageLegalEvaluator = new AgeLegalEvaluator(periodeDepartLegalDaoMock);

		final List<PeriodeDepartLegal> listPeriodeDepartLegal = asList(
				p("01/01/1954", 61, 7, 66, 7),
				p("01/01/1955", 62, 0, 67, 0),
				p("01/01/1952", 60, 9, 65, 9),
				p("01/01/1953", 61, 2, 66, 2),
				p("01/07/1951", 60, 4, 65, 4),
				p("", 60, 0, 65, 0));
		when(periodeDepartLegalDaoMock.findAll()).thenReturn(listPeriodeDepartLegal);
	}

	@Test
	public void tests_findPeriodeDepartLegal() {

		{
			final PeriodeDepartLegal periodeDepartLegal = ageLegalEvaluator.findPeriodeDepartLegal("03/11/1958");
			assertThat(periodeDepartLegal).isEqualTo(p("01/01/1955", 62, 0, 67, 0));
		}
		{
			final PeriodeDepartLegal periodeDepartLegal = ageLegalEvaluator.findPeriodeDepartLegal("01/01/1955");
			assertThat(periodeDepartLegal).isEqualTo(p("01/01/1955", 62, 0, 67, 0));
		}
		{
			final PeriodeDepartLegal periodeDepartLegal = ageLegalEvaluator.findPeriodeDepartLegal("31/12/1954");
			assertThat(periodeDepartLegal).isEqualTo(p("01/01/1954", 61, 7, 66, 7));
		}
		{
			final PeriodeDepartLegal periodeDepartLegal = ageLegalEvaluator.findPeriodeDepartLegal("03/06/1951");
			assertThat(periodeDepartLegal).isEqualTo(p("", 60, 0, 65, 0));
		}
	}

	@Test
	public void tests_isAgeLegal() {

		{
			// 03/11/1958 + 62 années = 03/11/2020
			assertThat(ageLegalEvaluator.isAgeLegal("03/11/1958", "11", "2020")).isFalse();
			assertThat(ageLegalEvaluator.isAgeLegal("03/11/1958", "12", "2020")).isTrue();
		}
		{
			// 31/12/1954 + 61 années + 7 mois = 03/07/2016
			assertThat(ageLegalEvaluator.isAgeLegal("31/12/1954", "07", "2016")).isFalse();
			assertThat(ageLegalEvaluator.isAgeLegal("31/12/1954", "08", "2016")).isTrue();
		}
		{
			// 03/06/1951 + 60 années = 03/06/2011
			assertThat(ageLegalEvaluator.isAgeLegal("03/06/1951", "06", "2011")).isFalse();
			assertThat(ageLegalEvaluator.isAgeLegal("03/06/1951", "07", "2011")).isTrue();
		}
	}

	private PeriodeDepartLegal p(final String dateNaissanceDebut, final int ageDepartLegalAnnees, final int ageDepartLegalMois, final int ageTauxPleinAnnees,
			final int ageTauxPleinMois) {
		return new PeriodeDepartLegal(
				dateNaissanceDebut,
				new AnneesEtMois(ageDepartLegalAnnees, ageDepartLegalMois),
				new AnneesEtMois(ageTauxPleinAnnees, ageTauxPleinMois));
	}

}
