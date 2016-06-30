package utils.engine.utils;

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class VariablesReplacerMustacheTest {

	private VariablesReplacer variablesReplacer;

	@Before
	public void setUp() {
		variablesReplacer = new VariablesReplacerMustache();
	}

	@Test
	public void should_replace_variables() {

		final String text = "avant {{une_variable}} apres";
		final Map<String, String> variables = new HashMap<>();
		variables.put("une_variable", "toto");

		final String result = variablesReplacer.replaceVariables(text, variables);

		assertThat(result).isEqualTo("avant toto apres");
	}

	@Test
	public void should_display_conditionnal_section() {

		final String text = "prefix {{#une_variable}}avant {{une_variable}} apres{{/une_variable}} suffixe";
		final Map<String, String> variables = new HashMap<>();
		variables.put("une_variable", "toto");

		final String result = variablesReplacer.replaceVariables(text, variables);

		assertThat(result).isEqualTo("prefix avant toto apres suffixe");
	}

	@Test
	public void should_not_display_conditionnal_section() {

		final String text = "prefix {{#une_variable}}avant {{une_variable}} apres{{/une_variable}} suffixe";
		final Map<String, String> variables = new HashMap<>();
		variables.put("une_autre_variable", "toto");

		final String result = variablesReplacer.replaceVariables(text, variables);

		assertThat(result).isEqualTo("prefix  suffixe");
	}
}
