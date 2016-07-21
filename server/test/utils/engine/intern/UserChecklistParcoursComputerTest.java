package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import utils.RetraiteException;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.data.UserChecklistVars;
import utils.engine.utils.VariablesReplacer;

public class UserChecklistParcoursComputerTest {

	final UserChecklistVars vars = new UserChecklistVars();
	final Map<String, String> variables = vars.getMapOfValues();

	private UserChecklistVarsProvider userChecklistVarsProviderMock;
	private VariablesReplacer variablesReplacerMock;

	private UserChecklistParcoursComputer userChecklistParcoursComputer;

	@Before
	public void setUp() {
		userChecklistVarsProviderMock = mock(UserChecklistVarsProvider.class);
		variablesReplacerMock = mock(VariablesReplacer.class);
		userChecklistParcoursComputer = new UserChecklistParcoursComputer(userChecklistVarsProviderMock, variablesReplacerMock);

		when(userChecklistVarsProviderMock.provideVars(any(UserChecklistGenerationData.class))).thenReturn(vars);
		when(variablesReplacerMock.replaceVariables(any(String.class), eq(variables))).then(new Answer<String>() {

			@Override
			public String answer(final InvocationOnMock invocation) throws Throwable {
				final String text = invocation.getArgumentAt(0, String.class);
				// On renvoie le texte sauf dans le cas de "toto" pour les remplacements de variables
				return (text.equals("toto") ? "titi" : text);
			}
		});
	}

	// Opérations de base sur les textes

	@Test
	public void should_do_nothing_for_null() {

		final String after = userChecklistParcoursComputer.compute(null, null);

		assertThat(after).isNull();
	}

	@Test
	public void should_do_nothing_for_simple_text() {

		final String before = "texte simple";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("texte simple");
	}

	// Remplacement des liens

	@Test
	public void should_replace_one_http_link() {

		final String before = "xxx http://monsite.com/path/page.html yyy";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("xxx " + link("http://monsite.com/path/page.html", "monsite.com/path/page") + " yyy");
	}

	@Test
	public void should_replace_one_http_link_in_PDF_mode() {

		final String before = "xxx http://monsite.com/path/page.html yyy";

		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(null, null, null, null, false, true /* isPDF */);
		final String after = userChecklistParcoursComputer.compute(before, userChecklistGenerationData);

		assertThat(after).isEqualTo("xxx " + link("http://monsite.com/path/page.html", "http://monsite.com/path/page.html") + " yyy");
	}

	@Test
	public void should_replace_one_https_link_and_nothing_after_link() {

		final String before = "xxx https://www.monsite.com/path/page.htm";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("xxx " + link("https://www.monsite.com/path/page.htm", "monsite.com/path/page") + "");
	}

	@Test
	public void should_replace_multiple_http_links_and_followed_by_different_characters() {

		final String before = "soit https://a/a. ou <i>http://b/b.PDF</i> fin";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("soit " + link("https://a/a", "a/a") + ". ou <i>" + link("http://b/b.PDF", "b/b") + "</i> fin");
	}

	@Test
	public void should_replace_one_http_link_using_specified_text() {

		final String before = "xxx [[ ceci est mon texte http://monsite.com/path/page.html ]] yyy";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("xxx " + link("http://monsite.com/path/page.html", "ceci est mon texte") + " yyy");
	}

	@Test
	public void should_replace_one_http_link_using_specified_text_in_PDF_mode() {

		final String before = "xxx [[ ceci est mon texte http://monsite.com/path/page.html ]] yyy";

		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(null, null, null, null, false, true /* isPDF */);
		final String after = userChecklistParcoursComputer.compute(before, userChecklistGenerationData);

		assertThat(after).isEqualTo("xxx " + link("http://monsite.com/path/page.html", "ceci est mon texte") + " ("
				+ link("http://monsite.com/path/page.html", "http://monsite.com/path/page.html") + ") yyy");
	}

	@Test
	public void should_replace_one_http_link_using_specified_text_without_link() {

		final String before = "xxx [[ ceci est mon texte ]] yyy";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("xxx ceci est mon texte yyy");
	}

	@Test
	public void should_replace_one_http_link_using_specified_text_without_text() {

		final String before = "xxx [[ http://monsite.com/path/page.html ]] yyy";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("xxx " + link("http://monsite.com/path/page.html", "monsite.com/path/page") + " yyy");
	}

	// Remplacement des variables

	@Test
	public void should_replace_vars() {

		final String before = "toto";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("titi");
	}

	@Test
	public void should_log_and_continue_if_error_replacing_vars() {
		doThrow(new RetraiteException("xxx")).when(variablesReplacerMock).replaceVariables(any(String.class), eq(variables));

		final String before = "toto";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("toto");
	}

	// Méthodes privées

	private String link(final String link, final String text) {
		return "<a href='" + link + "' target='_blank' title='Nouvelle fenêtre'>" + text + "</a>";
	}
}
