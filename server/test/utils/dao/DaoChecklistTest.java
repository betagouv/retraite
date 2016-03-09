package utils.dao;

import static org.fest.assertions.MapAssert.entry;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.TestsUtils.quote;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import models.Chapitre;
import models.Checklist;
import models.Condition;
import play.test.Fixtures;
import utils.RetraiteMoreThanOneChecklist;
import utils.RetraiteUnitTestBase;
import utils.db.HtmlCleaner;

public class DaoChecklistTest extends RetraiteUnitTestBase {

	private HtmlCleaner htmlCleanerMock;
	private DaoChecklist daoChecklist;

	@Before
	public void setupAndCleanDataBase() {
		Fixtures.deleteDatabase();
		loadAndLinkChecklistData("utils/dao/Checklists.yml");

		htmlCleanerMock = mock(HtmlCleaner.class);
		daoChecklist = new DaoChecklist(htmlCleanerMock);
	}

	@Test
	public void findAll_should_return_all_not_published() {

		final List<Checklist> checkLists = daoChecklist.findAll(false);

		assertThat(checkLists).hasSize(3);
		final Checklist checklistRsi = checkLists.get(1);
		assertThat(checklistRsi.nom).isEqualTo("RSI");
		assertThat(checklistRsi.chapitres).hasSize(2);
		final Chapitre rsiChapitre1 = checklistRsi.chapitres.get(0);
		assertThat(rsiChapitre1.conditions).hasSize(2);
	}

	@Test
	public void findAll_should_return_all_published() {

		final List<Checklist> checkLists = daoChecklist.findAll(true);

		assertThat(checkLists).hasSize(1);
	}

	@Test
	public void findById_should_return_all() {

		final Checklist checklist = new Checklist();
		checklist.nom = "checklist à trouver";
		final Checklist checklistSaved = checklist.save();

		final Checklist checkListFound = daoChecklist.findById(checklistSaved.id);

		assertThat(checkListFound.nom).isEqualTo("checklist à trouver");
	}

	@Test
	public void find_not_published_by_name() {

		final Checklist checkList = daoChecklist.find("RSI", false);

		assertThat(checkList.nom).isEqualTo("RSI");
		assertThat(checkList.published).isFalse();
		assertThat(checkList.chapitres).hasSize(2);
	}

	@Test
	public void find_published_by_name() {

		final Checklist checkList = daoChecklist.find("RSI", true);

		assertThat(checkList.nom).isEqualTo("RSI");
		assertThat(checkList.published).isTrue();
		assertThat(checkList.chapitres).hasSize(0);
	}

	@Test
	public void should_throw_exception_if_more_than_1() {

		final Checklist checklistToAdd = new Checklist();
		checklistToAdd.nom = "RSI";
		checklistToAdd.published = true;
		checklistToAdd.save();

		try {
			daoChecklist.find("RSI", true);
			fail("Devrait déclencher une exception");
		} catch (final RetraiteMoreThanOneChecklist e) {
		}

	}

	@Test
	public void find_not_published_by_id() {

		final Checklist checkList1 = daoChecklist.find("RSI", false);
		final Checklist checkList2 = daoChecklist.find(checkList1.id);

		assertThat(checkList2.published).isFalse();
		assertThat(checkList2).isSameAs(checkList1);
	}

	@Test
	public void find_published_by_id() {

		final Checklist checkList1 = daoChecklist.find("RSI", true);
		final Checklist checkList2 = daoChecklist.find(checkList1.id);

		assertThat(checkList2.published).isTrue();
		assertThat(checkList2).isSameAs(checkList1);
	}

	@Test
	public void update_should_update_checklist_in_base() {

		Checklist checklist = new Checklist();
		checklist.nom = "toto";
		checklist.modifiedButNotPublished = false;
		checklist = checklist.save();

		when(htmlCleanerMock.clean(anyString())).thenAnswer(new Answer() {
			@Override
			public Object answer(final InvocationOnMock invocation) {
				final Object[] args = invocation.getArguments();
				return args[0] + " cleaned";
			}
		});

		daoChecklist.update(quote("{"
				+ "id:" + checklist.id + ","
				+ "nom:'titi',"
				+ "chapitres:[{"
				+ "titre:'Je contacte',"
				+ "texteIntro:'texteIntro',"
				+ "parcours:'parcours',"
				+ "parcoursDemat:'parcoursDemat',"
				+ "texteComplementaire:'texteComplementaire'"
				+ "}]"
				+ "}"));

		final Checklist checklistAfter = Checklist.findById(checklist.id);
		assertThat(checklistAfter.nom).isEqualTo("titi");
		assertThat(checklistAfter.modifiedButNotPublished).isTrue();
		final Chapitre chapitre0 = checklistAfter.chapitres.get(0);
		assertThat(chapitre0.texteIntro).isEqualTo("texteIntro cleaned");
		assertThat(chapitre0.parcours).isEqualTo("parcours cleaned");
		assertThat(chapitre0.parcoursDemat).isEqualTo("parcoursDemat cleaned");
		assertThat(chapitre0.texteComplementaire).isEqualTo("texteComplementaire cleaned");
	}

	@Test
	public void update_should_update_checklist_in_base2() {

		Checklist checklist = new Checklist();
		checklist.nom = "toto";
		checklist.chapitres = new ArrayList<Chapitre>();
		final Chapitre chapitre1 = createAndAddChapitre(checklist, "a");
		final Chapitre chapitre2 = createAndAddChapitre(checklist, "b");
		checklist = checklist.save();

		// @formatter:off
		final Checklist checklistUpdated = daoChecklist
				.update(quote("{"
						+ "'nom':'RSI',"
						+ "'chapitres':["
						+ "{'titre':'Je contacte mes autres régimes (hors régimes alignés)',"
						+ "'parcoursDematDifferent':true,"
						+ "'id':" + chapitre1.id + ","
						+ "'conditions':["
						+ "{'props':"
						+ "{"
						+ "'plusOuMoins': 'plus',"
						+ "'nombre': '9',"
						+ "'unite': 'annees',"
						+ "'type': 'delai'"
						+ "}"
						+ "}"
						+ "],"
						+ "'texteIntro':'<p>rsi 1</p>',"
						+ "'texteComplementaire':'abc'"
						+ "},{"
						+ "'titre':'J’obtiens ma retraite',"
						+ "'parcoursDematDifferent':false,"
						+ "'id':" + chapitre2.id + ","
						+ "'texteIntro':'qsd',"
						+ "'texteComplementaire':'wxc'"
						+ "}],"
						+ "'id':" + checklist.id + "}"));
		// @formatter:on

		final Checklist checklistAfter = Checklist.findById(checklist.id);
		assertThat(checklistUpdated).isEqualTo(checklistAfter);

		assertThat(checklistAfter.nom).isEqualTo("RSI");
		assertThat(checklistAfter.chapitres).hasSize(2);
		final Chapitre[] chapitres = checklistAfter.chapitres.toArray(new Chapitre[0]);
		final Chapitre chapitre1After = chapitres[0];
		assertThat(chapitre1After.titre).isEqualTo("Je contacte mes autres régimes (hors régimes alignés)");
		assertThat(chapitre1After.parcoursDematDifferent).isTrue();
		assertThat(chapitre1After.checklist).isSameAs(checklistAfter);
		assertThat(chapitre1After.conditions).hasSize(1);
		final Condition condition0 = chapitre1After.conditions.get(0);
		assertThat(condition0.chapitre).isSameAs(chapitre1After);
		assertThat(condition0.props).hasSize(4);
		assertThat(condition0.props).includes(entry("plusOuMoins", "plus"));
		assertThat(condition0.props).includes(entry("nombre", "9"));
		assertThat(condition0.props).includes(entry("unite", "annees"));
		assertThat(condition0.props).includes(entry("type", "delai"));
	}

	// Méthodes privées

	private Chapitre createAndAddChapitre(final Checklist checklist, final String titre) {
		final Chapitre chapitre = new Chapitre();
		chapitre.titre = titre;
		chapitre.checklist = checklist;
		checklist.chapitres.add(chapitre);
		return chapitre;
	}

}
