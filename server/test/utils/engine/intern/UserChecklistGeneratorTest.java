package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.engine.data.enums.Regime.AGIRC_ARRCO;
import static utils.engine.data.enums.Regime.CNAV;
import static utils.engine.data.enums.Regime.RSI;

import org.junit.Before;
import org.junit.Test;

import models.Checklist;
import utils.dao.DaoChecklist;
import utils.engine.data.MonthAndYear;
import utils.engine.data.UserChecklist;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.ChecklistName;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;

public class UserChecklistGeneratorTest {

	private static final Regime[] REGIMES = new Regime[] { RSI, AGIRC_ARRCO, CNAV };

	private static final RegimeAligne[] REGIMES_ALIGNES = new RegimeAligne[] { RegimeAligne.CNAV };

	private DaoChecklist daoChecklistMock;
	private ChecklistNameSelector checklistNameSelectorMock;
	private UserChecklistComputer userChecklistComputerMock;
	private UserChecklistGenerator userChecklistGenerator;

	@Before
	public void setUp() {
		daoChecklistMock = mock(DaoChecklist.class);
		checklistNameSelectorMock = mock(ChecklistNameSelector.class);
		userChecklistComputerMock = mock(UserChecklistComputer.class);
		userChecklistGenerator = new UserChecklistGenerator(checklistNameSelectorMock, daoChecklistMock, userChecklistComputerMock);
	}

	@Test
	public void should_generate_checklist_using_collaborators_for_published() {

		final MonthAndYear dateDepart = new MonthAndYear();
		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(dateDepart, "973", REGIMES, REGIMES_ALIGNES,
				true, false, "");
		final Checklist checklist = new Checklist();
		final UserChecklist userChecklist = new UserChecklist();

		when(checklistNameSelectorMock.select(REGIMES_ALIGNES, RegimeAligne.CNAV)).thenReturn(ChecklistName.CNAV);
		when(daoChecklistMock.find("CNAV", true)).thenReturn(checklist);
		when(userChecklistComputerMock.compute(checklist, userChecklistGenerationData)).thenReturn(userChecklist);

		final UserChecklist userChecklistGenerated = userChecklistGenerator.generate(userChecklistGenerationData, RegimeAligne.CNAV);

		assertThat(userChecklistGenerated).isSameAs(userChecklist);
	}

	@Test
	public void should_generate_checklist_using_collaborators_for_not_published() {

		final MonthAndYear dateDepart = new MonthAndYear();
		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(dateDepart, "973", REGIMES, REGIMES_ALIGNES,
				false, false, "");
		final Checklist checklist = new Checklist();
		final UserChecklist userChecklist = new UserChecklist();

		when(checklistNameSelectorMock.select(REGIMES_ALIGNES, RegimeAligne.CNAV)).thenReturn(ChecklistName.CNAV);
		when(daoChecklistMock.find("CNAV", false)).thenReturn(checklist);
		when(userChecklistComputerMock.compute(checklist, userChecklistGenerationData)).thenReturn(userChecklist);

		final UserChecklist userChecklistGenerated = userChecklistGenerator.generate(userChecklistGenerationData, RegimeAligne.CNAV);

		assertThat(userChecklistGenerated).isSameAs(userChecklist);
	}

}
