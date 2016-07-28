package controllers.utils;

import static java.util.Arrays.asList;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import models.Checklist;
import play.test.Fixtures;
import utils.RetraiteAlreayPublishedChecklistException;
import utils.RetraiteUnitTestBase;
import utils.dao.DaoChecklist;
import utils.db.HtmlCleaner;

public class ChecklistPublisherTest extends RetraiteUnitTestBase {

	private DaoChecklist daoChecklist;

	private ChecklistPublisher checklistPublisher;

	@Before
	public void setUp() throws Exception {
		daoChecklist = new DaoChecklist(new HtmlCleaner());
		checklistPublisher = new ChecklistPublisher(daoChecklist);

		Fixtures.deleteDatabase();
		loadAndLinkChecklistData("utils/dao/Checklists.yml");
	}

	@Test
	public void should_throw_exception_if_publishing_checklist_published() {
		final Checklist checklist = daoChecklist.find("RSI", true);

		try {
			checklistPublisher.publish(checklist.id);
			fail("Devrait déclencher une exception");
		} catch (final RetraiteAlreayPublishedChecklistException e) {
			// OK
		}

	}

	@Test
	public void publish_should_copy_and_save_full_checklist() {

		final Checklist checklist = daoChecklist.find("RSI", false);
		// new ChecklistDumper().dump(checklist);
		assertThat(checklist.published).isFalse();
		checklist.modifiedButNotPublished = true;

		final long idForPublishedChecklist = checklistPublisher.publish(checklist.id);

		// Vérifications de la checklist d'origine
		final Checklist checklistOrigine = daoChecklist.findById(checklist.id);
		assertThat(checklistOrigine.modifiedButNotPublished).isFalse();

		// Vérifications de la checklist publiée
		final Checklist checklistPublishedRetrievedById = daoChecklist.findById(idForPublishedChecklist);
		assertThatPublishedObjectIsEqual(checklist, checklistPublishedRetrievedById);
		assertThat(checklistPublishedRetrievedById.id).isNotEqualTo(checklist.id);
		assertThat(checklistPublishedRetrievedById.id).isEqualTo(idForPublishedChecklist);
		assertThat(checklistPublishedRetrievedById.published).isTrue();

		// Pour vérifier qu'il n'y en a qu'une seule checklist publiée avec ce nom
		final Checklist checklistPublishedRetrievedByName = daoChecklist.find("RSI", true);
		assertThat(checklistPublishedRetrievedByName).isNotNull();
	}

	// Méthodes privées

	private void assertThatPublishedObjectIsEqual(final Object obj, final Object objPublished) {
		final List<String> fieldsToIgnore = asList(
				"id",
				"published",
				"modifiedButNotPublished",
				"checklist", // Pour les chapitres
				"chapitre" // Pour les conditions
		);
		try {
			final Field[] fields = obj.getClass().getFields();
			for (final Field field : fields) {
				if (fieldsToIgnore.contains(field.getName())) {
					continue;
				}
				final Object value = field.get(obj);
				final Object valuePublished = field.get(objPublished);
				if (field.getType().isAssignableFrom(List.class)) {
					final Object[] list = ((List) value).toArray();
					final Object[] listPublished = ((List) valuePublished).toArray();
					assertThatPublishedArrayIsEqual(list, listPublished);
				} else if (field.getType().isArray()) {
					final Object[] list = (Object[]) value;
					final Object[] listPublished = (Object[]) valuePublished;
					assertThatPublishedArrayIsEqual(list, listPublished);
				} else {
					assertThat(value)
							.overridingErrorMessage("Value '" + field.getName() + "' is not equal : " + value + " != " + valuePublished)
							.isEqualTo(valuePublished);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void assertThatPublishedArrayIsEqual(final Object[] list, final Object[] listPublished) {
		assertThat(list.length)
				.overridingErrorMessage("Arrays do not have same length : " + list.length + " != " + listPublished.length)
				.isEqualTo(listPublished.length);
		for (int i = 0; i < list.length; i++) {
			final Object subobj = list[i];
			final Object subobjPublished = listPublished[i];
			assertThatPublishedObjectIsEqual(subobj, subobjPublished);
		}
	}
}
