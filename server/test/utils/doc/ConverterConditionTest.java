package utils.doc;

import static org.fest.assertions.Assertions.assertThat;
import static utils.TestsUtilsCondition.createConditionDelai;
import static utils.TestsUtilsCondition.createConditionRegimeDetecte;
import static utils.TestsUtilsCondition.createConditionStatut;
import static utils.TestsUtilsCondition.createConditionWithType;

import org.junit.Before;
import org.junit.Test;

public class ConverterConditionTest {

	private ConverterCondition converterCondition;

	@Before
	public void setUp() throws Exception {
		converterCondition = new ConverterCondition();
	}

	@Test
	public void should_convert() {
		assertThat(converterCondition.convert(createConditionDelai("PLUS", "3", "MOIS"))).isEqualTo(
				"Si le délai restant avant la date de départ prévue est supérieur à 3 mois");
		assertThat(converterCondition.convert(createConditionDelai("MOINS", "1", "MOIS"))).isEqualTo(
				"Si le délai restant avant la date de départ prévue est inférieur à 1 mois");
		assertThat(converterCondition.convert(createConditionDelai("MOINS", "8", "ANNEE"))).isEqualTo(
				"Si le délai restant avant la date de départ prévue est inférieur à 8 années");
		assertThat(converterCondition.convert(createConditionDelai("MOINS", "1", "ANNEE"))).isEqualTo(
				"Si le délai restant avant la date de départ prévue est inférieur à 1 année");

		assertThat(converterCondition.convert(createConditionRegimeDetecte("agirc-arrco"))).isEqualTo("Si régimes AGIRC-ARRCO détectés");
		assertThat(converterCondition.convert(createConditionRegimeDetecte("regimes-base-hors-alignés"))).isEqualTo("Si régimes de base hors alignés détectés");
		assertThat(converterCondition.convert(createConditionRegimeDetecte("regimes-complémentaires-hors-agirc-arrco"))).isEqualTo(
				"Si régimes complémentaires hors AGIRC-ARRCO détectés");

		assertThat(converterCondition.convert(createConditionStatut("nsa"))).isEqualTo("Si l’assuré appartient à la catégorie NSA");
		assertThat(converterCondition.convert(createConditionStatut("sa"))).isEqualTo("Si l’assuré appartient à la catégorie SA");
		assertThat(converterCondition.convert(createConditionStatut("conjoint-collaborateur"))).isEqualTo("Si l’assuré est un conjoint collaborateur");
		assertThat(converterCondition.convert(createConditionStatut("chef-entreprise"))).isEqualTo("Si l’assuré est un chef d'entreprise");
		assertThat(converterCondition.convert(createConditionStatut("statut-inconnu")))
				.isEqualTo("<b><font size='5' color='red'>!!! Statut non géré 'statut-inconnu' !!!</font></b>");

		assertThat(converterCondition.convert(createConditionWithType("carriere-a-reconstituer")))
				.isEqualTo("Si l’assuré doit reconstituer sa carrière");

		assertThat(converterCondition.convert(createConditionWithType("carriere-longue-non")))
				.isEqualTo("Si carrière longue : non");
		assertThat(converterCondition.convert(createConditionWithType("carriere-longue-oui")))
				.isEqualTo("Si carrière longue : oui (a une attestation)");

		assertThat(converterCondition.convert(createConditionWithType("type-inconnu")))
				.isEqualTo("<b><font size='5' color='red'>!!! Condition non gérée 'type-inconnu' !!!</font></b>");

	}

}
