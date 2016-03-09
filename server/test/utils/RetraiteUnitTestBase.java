package utils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fest.assertions.Assertions;
import org.fest.assertions.BooleanAssert;
import org.fest.assertions.CollectionAssert;
import org.fest.assertions.IntAssert;
import org.fest.assertions.ListAssert;
import org.fest.assertions.LongAssert;
import org.fest.assertions.MapAssert;
import org.fest.assertions.ObjectAssert;
import org.fest.assertions.StringAssert;

import models.Chapitre;
import models.Checklist;
import play.test.Fixtures;
import play.test.UnitTest;

public abstract class RetraiteUnitTestBase extends UnitTest {

	// Utilitaires

	protected void loadAndLinkChecklistData(final String ymlFilename) {
		Fixtures.loadModels(ymlFilename);
		final List<Chapitre> chapitres = Chapitre.findAll();
		for (final Chapitre chapitre : chapitres) {
			final String checklistName = chapitre.titre.substring(0, chapitre.titre.indexOf(" "));
			final Checklist checklist = Checklist.find("byNom", checklistName).first();
			chapitre.checklist = checklist;
			checklist.chapitres.add(chapitre);
			checklist.save();
		}
	}

	// Raccourcis pour Assertions.assertThat

	protected static LongAssert assertThat(final Long value) {
		return Assertions.assertThat(value);
	}

	protected static BooleanAssert assertThat(final Boolean value) {
		return Assertions.assertThat(value);
	}

	protected static MapAssert assertThat(final Map value) {
		return Assertions.assertThat(value);
	}

	protected static ListAssert assertThat(final List<?> value) {
		return Assertions.assertThat(value);
	}

	protected static CollectionAssert assertThat(final Set<?> value) {
		return Assertions.assertThat(value);
	}

	protected static IntAssert assertThat(final Integer value) {
		return Assertions.assertThat(value);
	}

	protected static StringAssert assertThat(final String value) {
		return Assertions.assertThat(value);
	}

	protected static ObjectAssert assertThat(final Date value) {
		return Assertions.assertThat(value);
	}

	protected static ObjectAssert assertThat(final Object value) {
		return Assertions.assertThat(value);
	}

}
