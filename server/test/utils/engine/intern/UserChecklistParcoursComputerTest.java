package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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

	@Test
	public void should_do_nothing_for_null() {

		final String after = userChecklistParcoursComputer.compute(null, null);

		assertThat(after).isNull();
	}

	@Test
	public void should_return_null_for_empty() {

		final String after = userChecklistParcoursComputer.compute("<p></p>", null);

		assertThat(after).isNull();
	}

	@Test
	public void should_do_nothing_for_simple_text() {

		final String before = "texte simple";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("texte simple");
	}

	@Test
	public void should_replace_one_http_link() {

		final String before = "xxx http://monsite.com/path/page.html yyy";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("xxx " + link("http://monsite.com/path/page.html") + " yyy");
	}

	@Test
	public void should_replace_one_https_link_and_nothing_after_link() {

		final String before = "xxx https://monsite.com/path/page.html";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("xxx " + link("https://monsite.com/path/page.html") + "");
	}

	@Test
	public void should_replace_multiple_http_links_and_followed_by_different_characters() {

		final String before = "soit http://a/a. ou <i>https://b/b</i> fin";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("soit " + link("http://a/a") + ". ou <i>" + link("https://b/b") + "</i> fin");
	}

	@Test
	public void should_replace_vars() {

		final String before = "toto";

		final String after = userChecklistParcoursComputer.compute(before, null);

		assertThat(after).isEqualTo("titi");
	}

	// Méthodes privées

	private String link(final String link) {
		return "<a href='" + link + "' target='_blank' title='Nouvelle fenêtre'>" + link + "</a>";
	}
}
