package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import utils.engine.DateProviderFake;
import utils.engine.data.Departement;
import utils.engine.data.ValueAndText;

public class StepFormsDataProviderTest {

	private static final int CURRENT_YEAR = 2031;
	private StepFormsDataProvider stepFormsDataProvider;

	@Before
	public void setUp() throws Exception {
		stepFormsDataProvider = new StepFormsDataProvider(new DateProviderFake(1, 1, CURRENT_YEAR));
	}

	@Test
	public void should_return_full_list_of_departements() {

		final List<Departement> departements = stepFormsDataProvider.getListDepartements();

		assertThat(departements).contains(
				new Departement("01", "Ain"),
				new Departement("02", "Aisne"),
				new Departement("2A", "Corse-du-Sud"),
				new Departement("2B", "Haute-Corse"),
				new Departement("971", "Guadeloupe"));
	}

	@Test
	public void should_return_list_months() {

		final List<String> mois = stepFormsDataProvider.getListMois();

		assertThat(mois).containsExactly(
				"Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
				"Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre");
	}

	@Test
	public void should_return_list_months_with_1er() {

		final List<ValueAndText> mois = stepFormsDataProvider.getListMoisAvecPremier();

		assertThat(mois).containsExactly(vet("1", "1er Janvier"), vet("2", "1er Février"), vet("3", "1er Mars"), vet("4", "1er Avril"), vet("5", "1er Mai"),
				vet("6", "1er Juin"), vet("7", "1er Juillet"), vet("8", "1er Août"), vet("9", "1er Septembre"), vet("10", "1er Octobre"),
				vet("11", "1er Novembre"), vet("12", "1er Décembre"));
	}

	@Test
	public void should_return_list_annees_depart() {

		final List<String> annees = stepFormsDataProvider.getListAnneesDepart();

		final int count = 20;
		final List<String> expectedAnnees = createExpectedAnneesList(CURRENT_YEAR, count);
		assertThat(annees).isEqualTo(expectedAnnees);

	}

	@Test
	public void should_return_department_name() {
		assertThat(stepFormsDataProvider.getDepartementName("14")).isEqualTo("Calvados");
		assertThat(stepFormsDataProvider.getDepartementName("973")).isEqualTo("Guyane");
	}

	private List<String> createExpectedAnneesList(final int currentYear, final int count) {
		final ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			list.add(String.valueOf(currentYear + i));
		}
		return list;
	}

	private ValueAndText vet(final String value, final String text) {
		return new ValueAndText(value, text);
	}
}
