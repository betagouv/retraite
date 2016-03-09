package controllers.utils;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import models.Checklist;

public class ChecklistFilterTest {

	private ChecklistFilter checklistFilter;

	private List<Checklist> checklists;
	private Checklist clMsa;
	private Checklist clRsi;
	private Checklist clCnav;

	@Before
	public void setUp() {
		checklistFilter = new ChecklistFilter();

		clCnav = createChecklist("CNAV");
		clRsi = createChecklist("RSI");
		clMsa = createChecklist("MSA");
		checklists = asList(clCnav, clRsi, clMsa);
	}

	@Test
	public void should_filter_checklist_list() {

		final String authorizedChecklists = "CNAV,RSI";

		final List<Checklist> checklistsAfter = checklistFilter.filter(checklists, authorizedChecklists);

		assertThat(checklistsAfter).containsExactly(clCnav, clRsi);
	}

	@Test
	public void should_filter_checklist_list_even_if_spaces_in_authorized_list() {

		final String authorizedChecklists = " CNAV , RSI,";

		final List<Checklist> checklistsAfter = checklistFilter.filter(checklists, authorizedChecklists);

		assertThat(checklistsAfter).containsExactly(clCnav, clRsi);
	}

	private Checklist createChecklist(final String nom) {
		final Checklist checklist = new Checklist();
		checklist.nom = nom;
		return checklist;
	}
}
