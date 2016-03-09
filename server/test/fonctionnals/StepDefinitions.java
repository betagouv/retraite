package fonctionnals;

import static org.fest.assertions.Assertions.assertThat;
import cucumber.api.java.fr.Alors;
import cucumber.api.java.fr.Lorsque;
import cucumber.api.java.fr.Soit;

public class StepDefinitions {

	private String affiliation;

	@Soit("^Une personne avec l'affilitation (.*)$")
	public void une_personne_avec_l_affilitation_(final String affiliation) throws Throwable {
		this.affiliation = affiliation;
	}

	@Soit("^Une personne avec les affilitations (.*) et (.*)$")
	public void une_personne_avec_les_affilitations_x_et_x(final String regime1, final String regime2) throws Throwable {
		this.affiliation = regime1;
	}

	@Soit("^Le statut (.*)$")
	public void le_statut_x(final String statut) throws Throwable {
	}

	@Lorsque("^On choisit sa checklist$")
	public void on_choisit_sa_checklist() throws Throwable {
	}

	@Alors("^La checklist (.*) est sélectionnée$")
	public void la_checklist_x_est_sélectionnée(final String checklist) throws Throwable {
		assertThat(checklist).isEqualTo(affiliation);
	}

	@Alors("^La condition (.*) est activée$")
	public void la_condition_x_est_activée(final String condition) throws Throwable {
	}
}
