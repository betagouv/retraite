package utils.db;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class HtmlCleanerTest {

	private HtmlCleaner htmlCleaner;

	@Before
	public void setup() {
		htmlCleaner = new HtmlCleaner();
	}

	@Test
	public void should_do_nothing_if_null() {

		final String htmlCleaned = htmlCleaner.clean(null);

		assertThat(htmlCleaned).isNull();
	}

	@Test
	public void should_clean_html() {

		final String html = "<p><u><b>Pièces nécessaires :</b></u><span class=\"Apple-converted-space\"><u><b> </b></u></span><br/></p><ul><li>ma demande de retraite complétée et signée</li><li>une pièce justifiant de l’identité (carte nationale d’identité/passeport ou toute autre pièce justificative d’état civil) et de nationalité (livret de famille, copie de l’acte de naissance avec filiation, etc.)<span class=\"Apple-converted-space\"> </span><br/></li><li>le cas échéant, une pièce justifiant de l’identité des enfants (livret de famille/extrait d’acte de naissance/décision de justice confiant l’enfant) ;<span class=\"Apple-converted-space\"> </span><br/></li><li>un titre de séjour en cours de validité ou récépissé de demande (si l’assuré est de nationalité étrangère hors Union européenne - UE, Islande, Liechtenstein, Norvège ou Suisse) ;<span class=\"Apple-converted-space\"> </span><br/></li><li>un relevé d’identité bancaire (RIB) ou relevé d’identité de caisse d’épargne (RICE) ;<span class=\"Apple-converted-space\"> </span><br/></li><li>une photocopie du dernier avis d’impôt ;<span class=\"Apple-converted-space\"> </span><br/></li><li>une attestation de l’employeur ou bulletins de salaires de la dernière année ;<span class=\"Apple-converted-space\"> </span><br/></li><li>un décompte d’indemnités journalières (ou une attestation) délivré par la CPAM des deux dernières années ;<span class=\"Apple-converted-space\"> </span><br/></li><li>le cas échéant, une attestation de Pôle emploi des périodes de chômage pour la dernière année ;<span class=\"Apple-converted-space\"> </span><br/></li><li>une déclaration sur l’honneur de cessation d’activité pour les salariés du régime général et les salariés agricoles, une attestation de cessation d’activité délivrée par la MSA pour les exploitants agricoles, un certificat de radiation du répertoire des métiers et/ou du registre des commerces et des sociétés pour les artisans et commerçants.<span class=\"Apple-converted-space\"> </span></li></ul>";

		final String htmlCleaned = htmlCleaner.clean(html);

		final String expectedHtmlCleaned = "<p><u><b>Pièces nécessaires :</b></u><u><b> </b></u></span><br/></p><ul><li>ma demande de retraite complétée et signée</li><li>une pièce justifiant de l’identité (carte nationale d’identité/passeport ou toute autre pièce justificative d’état civil) et de nationalité (livret de famille, copie de l’acte de naissance avec filiation, etc.)<br/></li><li>le cas échéant, une pièce justifiant de l’identité des enfants (livret de famille/extrait d’acte de naissance/décision de justice confiant l’enfant) ;<br/></li><li>un titre de séjour en cours de validité ou récépissé de demande (si l’assuré est de nationalité étrangère hors Union européenne - UE, Islande, Liechtenstein, Norvège ou Suisse) ;<br/></li><li>un relevé d’identité bancaire (RIB) ou relevé d’identité de caisse d’épargne (RICE) ;<br/></li><li>une photocopie du dernier avis d’impôt ;<br/></li><li>une attestation de l’employeur ou bulletins de salaires de la dernière année ;<br/></li><li>un décompte d’indemnités journalières (ou une attestation) délivré par la CPAM des deux dernières années ;<br/></li><li>le cas échéant, une attestation de Pôle emploi des périodes de chômage pour la dernière année ;<br/></li><li>une déclaration sur l’honneur de cessation d’activité pour les salariés du régime général et les salariés agricoles, une attestation de cessation d’activité délivrée par la MSA pour les exploitants agricoles, un certificat de radiation du répertoire des métiers et/ou du registre des commerces et des sociétés pour les artisans et commerçants.</li></ul>";
		assertThat(htmlCleaned).isEqualTo(expectedHtmlCleaned);
	}

	@Test
	public void verifs_supp() {

		final String htmlCleaned = htmlCleaner.clean("actions 1");
		assertThat(htmlCleaned).isEqualTo("actions 1");
	}
}
