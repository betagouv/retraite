package controllers.utils;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.engine.data.enums.RegimeAligne.CNAV;
import static utils.engine.data.enums.RegimeAligne.RSI;
import static utils.engine.data.enums.UserStatus.STATUS_CONJOINT_COLLABORATEUR;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controllers.data.ApiUserChecklistParams;
import utils.engine.data.MonthAndYear;
import utils.engine.data.RenderData;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.enums.ChecklistName;
import utils.engine.data.enums.Regime;
import utils.engine.data.enums.RegimeAligne;
import utils.engine.intern.CalculateurRegimeAlignes;
import utils.engine.intern.UserChecklistGenerationDataBuilder;
import utils.engine.intern.UserChecklistGenerator;

public class ApiChecklistGeneratorTest {

	private UserChecklistGenerationDataBuilder userChecklistGenerationDataBuilderMock;
	private UserChecklistGenerator userChecklistGeneratorMock;
	private CalculateurRegimeAlignes calculateurRegimeAlignesMock;

	private ApiChecklistGenerator apiRestUserChecklistGenerator;

	@Before
	public void setUp() {
		userChecklistGenerationDataBuilderMock = mock(UserChecklistGenerationDataBuilder.class);
		userChecklistGeneratorMock = mock(UserChecklistGenerator.class);
		calculateurRegimeAlignesMock = mock(CalculateurRegimeAlignes.class);
		apiRestUserChecklistGenerator = new ApiChecklistGenerator(userChecklistGenerationDataBuilderMock, userChecklistGeneratorMock,
				calculateurRegimeAlignesMock);
	}

	@Test
	public void should_generate_error_messages() {

		ApiUserChecklistParams apiUserChecklistParams;
		RenderData data;

		apiUserChecklistParams = new ApiUserChecklistParams();
		// On ne renseigne pas ce champ :
		// apiUserChecklistParams.departement = "971";
		apiUserChecklistParams.departMois = "11";
		apiUserChecklistParams.departAnnee = "2017";
		apiUserChecklistParams.dateNaissance = "07/10/1954";
		apiUserChecklistParams.regimes = new Regime[0];
		apiUserChecklistParams.regimeLiquidateur = CNAV;
		data = apiRestUserChecklistGenerator.generate(apiUserChecklistParams);
		assertThat(data.errorMessage).isEqualTo("Le paramètre 'departement' est manquant");

		apiUserChecklistParams = new ApiUserChecklistParams();
		// On ne renseigne pas ce champ :
		apiUserChecklistParams.departement = "971";
		// apiUserChecklistParams.departMois = "11";
		apiUserChecklistParams.departAnnee = "2017";
		apiUserChecklistParams.dateNaissance = "07/10/1954";
		apiUserChecklistParams.regimes = new Regime[0];
		apiUserChecklistParams.regimeLiquidateur = CNAV;
		data = apiRestUserChecklistGenerator.generate(apiUserChecklistParams);
		assertThat(data.errorMessage).isEqualTo("Le paramètre 'departMois' est manquant");

		apiUserChecklistParams = new ApiUserChecklistParams();
		apiUserChecklistParams.departement = "971";
		apiUserChecklistParams.departMois = "11";
		// On ne renseigne pas ce champ :
		// apiUserChecklistParams.departAnnee = "2017";
		apiUserChecklistParams.dateNaissance = "07/10/1954";
		apiUserChecklistParams.regimes = new Regime[0];
		apiUserChecklistParams.regimeLiquidateur = CNAV;
		data = apiRestUserChecklistGenerator.generate(apiUserChecklistParams);
		assertThat(data.errorMessage).isEqualTo("Le paramètre 'departAnnee' est manquant");

		apiUserChecklistParams = new ApiUserChecklistParams();
		apiUserChecklistParams.departement = "971";
		apiUserChecklistParams.departMois = "11";
		apiUserChecklistParams.departAnnee = "2017";
		// On ne renseigne pas ce champ :
		// apiUserChecklistParams.dateNaissance = "07/10/1954";
		apiUserChecklistParams.regimes = new Regime[0];
		apiUserChecklistParams.regimeLiquidateur = CNAV;
		data = apiRestUserChecklistGenerator.generate(apiUserChecklistParams);
		assertThat(data.errorMessage).isEqualTo("Le paramètre 'dateNaissance' est manquant");

		apiUserChecklistParams = new ApiUserChecklistParams();
		apiUserChecklistParams.departement = "971";
		apiUserChecklistParams.departMois = "11";
		apiUserChecklistParams.departAnnee = "2017";
		apiUserChecklistParams.dateNaissance = "07/10/1954";
		apiUserChecklistParams.regimes = new Regime[0];
		// On ne renseigne pas ce champ :
		// apiUserChecklistParams.regimeLiquidateur = "CNAV";
		data = apiRestUserChecklistGenerator.generate(apiUserChecklistParams);
		assertThat(data.errorMessage).isEqualTo("Le paramètre 'regimeLiquidateur' est manquant");

		apiUserChecklistParams = new ApiUserChecklistParams();
		apiUserChecklistParams.departement = "971";
		apiUserChecklistParams.departMois = "11";
		apiUserChecklistParams.departAnnee = "2017";
		apiUserChecklistParams.dateNaissance = "07/10/1954";
		apiUserChecklistParams.regimes = new Regime[0];
		apiUserChecklistParams.regimeLiquidateur = CNAV;
		data = apiRestUserChecklistGenerator.generate(apiUserChecklistParams);
		assertThat(data.errorMessage).isNull();

	}

	@Test
	public void should_generate_data() {
		final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create().get();
		when(userChecklistGenerationDataBuilderMock.build(any(MonthAndYear.class), anyString(), any(Regime[].class), any(RegimeAligne[].class),
				any(RegimeAligne.class), anyBoolean(), anyBoolean(), any(List.class), anyBoolean(), anyString()))
						.thenReturn(userChecklistGenerationData);
		when(calculateurRegimeAlignesMock.getRegimesAlignes(new Regime[] { Regime.CNAV })).thenReturn(new RegimeAligne[] { RegimeAligne.CNAV });

		final ApiUserChecklistParams apiUserChecklistParams = new ApiUserChecklistParams();
		apiUserChecklistParams.nom = "DURAND";
		apiUserChecklistParams.dateNaissance = "07/10/1954";
		apiUserChecklistParams.nir = "1 54 10 14 123 456";
		apiUserChecklistParams.departement = "973";
		apiUserChecklistParams.departMois = "11";
		apiUserChecklistParams.departAnnee = "2017";
		apiUserChecklistParams.regimes = new Regime[] { Regime.CNAV };
		apiUserChecklistParams.isCarriereLongue = true;
		apiUserChecklistParams.userStatus = STATUS_CONJOINT_COLLABORATEUR;
		apiUserChecklistParams.published = false;
		apiUserChecklistParams.regimeLiquidateur = RSI;

		final RenderData data = apiRestUserChecklistGenerator.generate(apiUserChecklistParams);

		assertThat(data.errorMessage).isNull();
		assertThat(data.hidden_nom).isEqualTo("DURAND");
		assertThat(data.hidden_naissance).isEqualTo("07/10/1954");
		assertThat(data.hidden_nir).isEqualTo("1 54 10 14 123 456");
		assertThat(data.hidden_departMois).isEqualTo("11");
		assertThat(data.hidden_departAnnee).isEqualTo("2017");

		verify(userChecklistGenerationDataBuilderMock).build(
				new MonthAndYear("11", "2017"),
				"973",
				new Regime[] { Regime.CNAV },
				new RegimeAligne[] { RegimeAligne.CNAV },
				RSI,
				false /* published */,
				true /* isCarriereLongue */,
				asList(STATUS_CONJOINT_COLLABORATEUR),
				false /* isPDF */,
				null /* regimesInfosJsonStr : Pour l'instant, pas besoin de tester les autres interlocuteurs */);
		verify(userChecklistGeneratorMock).generate(eq(ChecklistName.RSI), eq(userChecklistGenerationData));
	}

}
