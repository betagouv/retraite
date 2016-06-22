package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import models.Chapitre;
import models.Delai;
import utils.engine.data.MonthAndYear;
import utils.engine.data.UserChapitre;
import utils.engine.data.UserChecklistGenerationData;

public class UserChecklistChapitreComputerTest {

	private static final MonthAndYear DATE_DEPART = new MonthAndYear();

	private UserChecklistDelaiComputer userChecklistDelaiComputerMock;
	private UserChecklistParcoursComputer userChecklistParcoursComputerMock;

	private UserChecklistChapitreComputer userChecklistChapitreComputer;

	@Before
	public void setUp() throws Exception {
		userChecklistDelaiComputerMock = mock(UserChecklistDelaiComputer.class);
		userChecklistParcoursComputerMock = mock(UserChecklistParcoursComputer.class);
		userChecklistChapitreComputer = new UserChecklistChapitreComputer(userChecklistDelaiComputerMock, userChecklistParcoursComputerMock);
	}

	@Test
	public void should_copy_standard_fields() {

		when(userChecklistDelaiComputerMock.compute(any(Delai.class), any(MonthAndYear.class))).thenReturn("Dès que possible");
		when(userChecklistParcoursComputerMock.compute(any(String.class), any(UserChecklistGenerationData.class))).thenAnswer(new Answer<String>() {

			@Override
			public String answer(final InvocationOnMock invocation) throws Throwable {
				return toString(invocation.getArguments()[0]) + " computed";
			}

			private String toString(final Object object) {
				return object == null ? "null" : object.toString();
			}
		});

		final Chapitre chapitre = createChapitre("chap1");

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create().get();

		final UserChapitre userChapitre = userChecklistChapitreComputer.compute(chapitre, userChecklistGenerationData);

		assertThat(userChapitre.titre).isEqualTo("chap1");
		assertThat(userChapitre.delai).isEqualTo("Dès que possible");
		assertThat(userChapitre.texteActions).isEqualTo("actions chap1 computed");
		assertThat(userChapitre.texteModalites).isEqualTo("modalités chap1 computed");
		assertThat(userChapitre.texteInfos).isEqualTo("infos chap1 computed");
	}

	@Test
	public void should_choice_SA_delai_if_necessary() {

		final Chapitre chapitre = createChapitre("chap1");
		chapitre.delai = new Delai();
		chapitre.delaiSA = new Delai();

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withDateDepart(DATE_DEPART)
				.get();
		userChecklistGenerationData.isSA = true;

		when(userChecklistDelaiComputerMock.compute(same(chapitre.delai), same(DATE_DEPART))).thenReturn("Conversion delai");
		when(userChecklistDelaiComputerMock.compute(same(chapitre.delaiSA), same(DATE_DEPART))).thenReturn("Conversion delai SA");

		final UserChapitre userChapitre = userChecklistChapitreComputer.compute(chapitre, userChecklistGenerationData);

		assertThat(userChapitre.delai).isEqualTo("Conversion delai SA");
	}

	private Chapitre createChapitre(final String titre) {
		final Chapitre chapitre = new Chapitre();
		chapitre.titre = titre;
		chapitre.delai = new Delai();
		chapitre.texteActions = "actions " + titre;
		chapitre.texteModalites = "modalités " + titre;
		chapitre.texteInfos = "infos " + titre;
		return chapitre;
	}

}
