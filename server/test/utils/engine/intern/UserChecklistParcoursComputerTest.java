package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
	final Map<String, Object> variables = vars.getMapOfValues();

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
				// On renvoie le texte sauf dans le cas de "{{toto}}" et de "vide" pour les remplacements de variables
				return (text.replaceAll("\\{\\{toto\\}\\}", "titi").replaceAll("\\{\\{vide\\}\\}", ""));
			}
		});
	}

	// Opérations de base sur les textes

	@Test
	public void should_do_nothing_for_null() {

		final List<String> urls = new ArrayList<>();

		final String result = userChecklistParcoursComputer.compute(null, null, urls);

		assertThat(result).isNull();
	}

	@Test
	public void should_do_nothing_for_simple_text() {

		final String before = "texte simple";
		final List<String> urls = new ArrayList<>();

		final String result = userChecklistParcoursComputer.compute(before, null, urls);

		assertThat(result).isEqualTo("texte simple");
	}

	// Remplacement des liens

	@Test
	public void should_replace_one_http_link() {

		final String before = "xxx http://monsite.com/path/page.html yyy";
		final List<String> urls = new ArrayList<>();

		final String result = userChecklistParcoursComputer.compute(before, null, urls);

		assertThat(result).isEqualTo("xxx " + link("http://monsite.com/path/page.html", "monsite.com/path/page") + " yyy");
	}

	@Test
	public void should_replace_one_http_link_in_PDF_mode() {

		final String before = "xxx http://monsite.com/path/page.html yyy";
		final List<String> urls = new ArrayList<>();

		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(null, null, null, null, false, true /* isPDF */, "");
		final String result = userChecklistParcoursComputer.compute(before, userChecklistGenerationData, urls);

		assertThat(result).isEqualTo("xxx " + link("http://monsite.com/path/page.html", "monsite.com/path/page") + "<sup>1</sup> yyy");
		assertThat(urls).containsExactly("http://monsite.com/path/page.html");
	}

	@Test
	public void should_replace_one_https_link_and_nothing_after_link() {

		final String before = "xxx https://www.monsite.com/path/page.htm";
		final List<String> urls = new ArrayList<>();

		final String result = userChecklistParcoursComputer.compute(before, null, urls);

		assertThat(result).isEqualTo("xxx " + link("https://www.monsite.com/path/page.htm", "monsite.com/path/page") + "");
	}

	@Test
	public void should_replace_multiple_http_links_and_followed_by_different_characters() {

		final String before = "soit https://a/a. ou <i>http://b/b.PDF</i> fin";
		final List<String> urls = new ArrayList<>();

		final String result = userChecklistParcoursComputer.compute(before, null, urls);

		assertThat(result).isEqualTo("soit " + link("https://a/a", "a/a") + ". ou <i>" + link("http://b/b.PDF", "b/b") + "</i> fin");
	}

	@Test
	public void should_replace_one_http_link_using_specified_text() {

		final String before = "xxx [[ ceci est mon texte http://monsite.com/path/page.html ]] yyy";
		final List<String> urls = new ArrayList<>();

		final String result = userChecklistParcoursComputer.compute(before, null, urls);

		assertThat(result).isEqualTo("xxx " + link("http://monsite.com/path/page.html", "ceci est mon texte") + "<sup>1</sup> yyy");
		assertThat(urls).containsExactly("http://monsite.com/path/page.html");
		//assertThat(result).isEqualTo("xxx " + link("http://monsite.com/path/page.html", "ceci est mon texte") + " yyy");
		//assertThat(urls).isNullOrEmpty();
	}

	@Test
	public void should_replace_one_http_link_using_specified_text_in_PDF_mode() {

		final String before = "xxx [[ ceci est mon texte http://monsite.com/path/page.html ]] yyy";
		final List<String> urls = new ArrayList<>();

		final UserChecklistGenerationData userChecklistGenerationData = new UserChecklistGenerationData(null, null, null, null, false, true /* isPDF */, "");
		final String result = userChecklistParcoursComputer.compute(before, userChecklistGenerationData, urls);

		assertThat(result).isEqualTo("xxx " + link("http://monsite.com/path/page.html", "ceci est mon texte") + "<sup>1</sup> yyy");
		assertThat(urls).containsExactly("http://monsite.com/path/page.html");
	}

	@Test
	public void should_replace_one_http_link_using_specified_text_without_link() {

		final String before = "xxx [[ ceci est mon texte ]] yyy";
		final List<String> urls = new ArrayList<>();

		final String result = userChecklistParcoursComputer.compute(before, null, urls);

		assertThat(result).isEqualTo("xxx ceci est mon texte yyy");
	}

	@Test
	public void should_replace_one_http_link_using_specified_text_without_text() {

		final String before = "xxx [[ http://monsite.com/path/page.html ]] yyy";
		final List<String> urls = new ArrayList<>();

		final String result = userChecklistParcoursComputer.compute(before, null, urls);

		assertThat(result).isEqualTo("xxx " + link("http://monsite.com/path/page.html", "monsite.com/path/page") + " yyy");
	}

	// Remplacement des variables

	@Test
	public void should_replace_vars() {

		final String before = "{{toto}}";
		final List<String> urls = new ArrayList<>();

		final String result = userChecklistParcoursComputer.compute(before, null, urls);

		assertThat(result).isEqualTo("titi");
	}

	@Test
	public void should_log_and_continue_if_error_replacing_vars() {
		doThrow(new RetraiteException("xxx")).when(variablesReplacerMock).replaceVariables(any(String.class), eq(variables));

		final String before = "{{toto}}";
		final List<String> urls = new ArrayList<>();

		final String result = userChecklistParcoursComputer.compute(before, null, urls);

		assertThat(result).isEqualTo("{{toto}}");
	}


	@Test
	public void should_return_empty_if_empty_vars_only() {

		final String before = "<p>{{vide}}</p>";
		final List<String> urls = new ArrayList<>();

		final String result = userChecklistParcoursComputer.compute(before, null, urls);

		assertThat(result).isNull();
	}

	// Méthodes privées

	private String link(final String link, final String text) {
		return "<a href='" + link + "' target='_blank' title='Nouvelle fenêtre'>" + text + "</a>";
	}
}
