package utils.engine.intern;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.CHEF_EXPLOITATION_AGRICOLE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_INDEP_CONJOINT_AUTRE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DIVERSES_SITUATIONS_CNAV_RSI;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.DIVERSES_SITUATIONS_RSI_MSA;
import static utils.engine.data.enums.LiquidateurQuestionDescriptor.ORGA_FRAIS_SANTE_CPAM_RSI;
import static utils.engine.data.enums.QuestionChoiceValue.AUTRE;
import static utils.engine.data.enums.QuestionChoiceValue.CPAM;
import static utils.engine.data.enums.QuestionChoiceValue.DEUX_ACTIVITES;
import static utils.engine.data.enums.QuestionChoiceValue.HORS_TERRITOIRE_FRANCAIS;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP;
import static utils.engine.data.enums.QuestionChoiceValue.INDEP_AVANT_73;
import static utils.engine.data.enums.QuestionChoiceValue.NON;
import static utils.engine.data.enums.QuestionChoiceValue.OUI;
import static utils.engine.data.enums.QuestionChoiceValue.PENIBILITE;
import static utils.engine.data.enums.QuestionChoiceValue.SA;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.MSA;
import static utils.engine.data.enums.RegimeAligne.RSI;

import org.junit.Before;
import org.junit.Test;

import utils.engine.data.LiquidateurReponses;
import utils.engine.data.enums.ChecklistName;
import utils.engine.data.enums.LiquidateurQuestionDescriptor;
import utils.engine.data.enums.RegimeAligne;

public class ChecklistNameSelectorTest {

	private ChecklistNameSelector checklistNameSelector;

	@Before
	public void setUp() throws Exception {
		checklistNameSelector = new ChecklistNameSelector();
	}

	@Test
	public void regime_unique_CNAV() {
		assertThat(checklistNameSelector.select(regimes(CNAV), null)).isEqualTo(ChecklistName.CNAV);
	}

	@Test
	public void regime_unique_RSI() {
		assertThat(checklistNameSelector.select(regimes(RSI), new LiquidateurReponses())).isEqualTo(ChecklistName.RSI);
	}

	@Test
	public void regime_unique_MSA() {
		assertThat(checklistNameSelector.select(regimes(MSA), null)).isEqualTo(ChecklistName.MSA);
	}

	@Test
	public void regimes_CNAV_MAS_choix_NSA() {
		final LiquidateurReponses liquidateurReponses = new LiquidateurReponses();
		liquidateurReponses.getReponses().put(CHEF_EXPLOITATION_AGRICOLE, asList(OUI));

		assertThat(checklistNameSelector.select(regimes(CNAV, MSA), liquidateurReponses)).isEqualTo(ChecklistName.MSA);
	}

	@Test
	public void regimes_CNAV_MAS_choix_SA() {
		final LiquidateurReponses liquidateurReponses = new LiquidateurReponses();
		liquidateurReponses.getReponses().put(CHEF_EXPLOITATION_AGRICOLE, asList(NON));
		liquidateurReponses.getReponses().put(DERN_ACT_SA_CONJOINT_AUTRE_DOUBLE, asList(SA));// Déterminant

		assertThat(checklistNameSelector.select(regimes(CNAV, MSA), liquidateurReponses)).isEqualTo(ChecklistName.MSA);
	}

	@Test
	public void regimes_RSI_MAS_choix_Rsi_avant_73() {
		final LiquidateurReponses liquidateurReponses = new LiquidateurReponses();
		liquidateurReponses.getReponses().put(DIVERSES_SITUATIONS_RSI_MSA, asList(PENIBILITE, INDEP_AVANT_73));

		assertThat(checklistNameSelector.select(regimes(CNAV, MSA), liquidateurReponses)).isEqualTo(ChecklistName.RSI);
	}

	@Test
	public void regimes_CNAV_RSI_choix_AUTRE_puis_INDEP() {
		final LiquidateurReponses liquidateurReponses = new LiquidateurReponses();
		liquidateurReponses.getReponses().put(LiquidateurQuestionDescriptor.DIVERSES_SITUATIONS_CNAV_RSI, asList(AUTRE));
		liquidateurReponses.getReponses().put(LiquidateurQuestionDescriptor.DERN_ACT_INDEP_CONJOINT_AUTRE, asList(INDEP));

		assertThat(checklistNameSelector.select(regimes(CNAV, RSI), liquidateurReponses)).isEqualTo(ChecklistName.RSI);
	}

	@Test
	public void cas_bug_1() {

		// utils.RetraiteException: Situation anormale : impossible de déterminer la checklist :
		// regimesAlignes=[CNAV, RSI],
		// liquidateurReponses={"reponses":{
		// . "DIVERSES_SITUATIONS_CNAV_RSI":["HORS_TERRITOIRE_FRANCAIS","AUTRE"],
		// . "DERN_ACT_INDEP_CONJOINT_AUTRE":["DEUX_ACTIVITES"],
		// . "ORGA_FRAIS_SANTE_CPAM_RSI":["CPAM"]}}
		// --> ChecklistSelectors=[SELECT_CNAV_SAUF_SI_RSI_AVANT_73_OU_INVALIDITE, SELECT_CNAV]

		final LiquidateurReponses liquidateurReponses = new LiquidateurReponses();
		liquidateurReponses.getReponses().put(DIVERSES_SITUATIONS_CNAV_RSI, asList(HORS_TERRITOIRE_FRANCAIS, AUTRE));
		liquidateurReponses.getReponses().put(DERN_ACT_INDEP_CONJOINT_AUTRE, asList(DEUX_ACTIVITES));
		liquidateurReponses.getReponses().put(ORGA_FRAIS_SANTE_CPAM_RSI, asList(CPAM));

		assertThat(checklistNameSelector.select(regimes(CNAV, RSI), liquidateurReponses)).isEqualTo(ChecklistName.CNAV);
	}

	// TODO utils.RetraiteException: Situation anormale : impossible de déterminer la checklist : regimesAlignes=[CNAV, RSI],
	// liquidateurReponses={"reponses":{"DIVERSES_SITUATIONS_CNAV_RSI":["AUTRE"],"DERN_ACT_INDEP_CONJOINT_AUTRE":["INDEP"]}} -->
	// ChecklistSelectors=[SELECT_CNAV, SELECT_RSI]

	private RegimeAligne[] regimes(final RegimeAligne... regimes) {
		return regimes;
	}

}
