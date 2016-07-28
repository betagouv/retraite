package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static utils.engine.data.enums.Regime.AGIRC_ARRCO;
import static utils.engine.data.enums.Regime.CARCD;
import static utils.engine.data.enums.Regime.CNAV;
import static utils.engine.data.enums.Regime.IRCANTEC;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import models.Caisse;
import models.Chapitre;
import models.Checklist;
import utils.dao.CaisseDao;
import utils.engine.data.UserChecklist;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.ChecklistName;
import utils.engine.data.enums.Regime;

public class UserChecklistComputerTest {

	private UserChecklistChapitreFilter userChecklistChapitreFilterMock;
	private UserChecklistChapitreComputer userChecklistChapitreComputerMock;
	private AutreRegimeComputer autreRegimeComputerMock;
	private CaisseDao caisseDaoMock;

	private UserChecklistComputer userChecklistComputer;

	@Before
	public void setUp() throws Exception {
		userChecklistChapitreFilterMock = mock(UserChecklistChapitreFilter.class);
		userChecklistChapitreComputerMock = mock(UserChecklistChapitreComputer.class);
		autreRegimeComputerMock = mock(AutreRegimeComputer.class);
		caisseDaoMock = mock(CaisseDao.class);
		userChecklistComputer = new UserChecklistComputer(userChecklistChapitreFilterMock, userChecklistChapitreComputerMock, caisseDaoMock,
				autreRegimeComputerMock);
	}

	@Test
	public void test() {

		final Caisse caisse = new Caisse();

		when(caisseDaoMock.find(ChecklistName.CNAV, "972")).thenReturn(caisse);
		// Mock pour le filtrage des chapitres : on exclut le 2e
		when(userChecklistChapitreFilterMock.isToBeDisplayed(any(Chapitre.class), any(UserChecklistGenerationData.class))).thenReturn(true, false, true);

		// Création de la Checklist pour le test
		final Checklist checklist = new Checklist();
		checklist.nom = "CNAV";
		final List<Chapitre> chapitres = checklist.chapitres = new ArrayList<>();
		chapitres.add(new Chapitre());
		chapitres.add(new Chapitre());// Filtré
		chapitres.add(new Chapitre());

		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create()
				.withDepartement("972")
				.withRegimes(new Regime[] { AGIRC_ARRCO, CNAV, IRCANTEC, CARCD })
				.withRegimesInfosJsonStr("[{\"nom\":\"CNAV\",\"adresse\":\"addr CNAV\",\"tel1\":\"tel CNAV\",\"email1\":\"mail CNAV\"}]")
				.get();

		// Test
		final UserChecklist userChecklist = userChecklistComputer.compute(checklist, userChecklistGenerationData);

		// Vérifications
		assertThat(userChecklist.nom).isEqualTo(checklist.nom);
		assertThat(userChecklist.caisse).isEqualTo(caisse);
		assertThat(userChecklist.chapitres).hasSize(2);

		verify(autreRegimeComputerMock).compute(userChecklist, userChecklistGenerationData.regimesInfosJsonStr);

		verify(userChecklistChapitreComputerMock, times(2)).compute(any(Chapitre.class), any(UserChecklistGenerationData.class));
		verifyNoMoreInteractions(userChecklistChapitreComputerMock);
	}

}
