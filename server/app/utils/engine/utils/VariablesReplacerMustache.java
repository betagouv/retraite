package utils.engine.utils;

import java.util.Map;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import utils.RetraiteException;

public class VariablesReplacerMustache implements VariablesReplacer {

	@Override
	public String replaceVariables(final String text, final Map<String, Object> variables) {
		// Mustache
		// final StringWriter writer = new StringWriter();
		// final MustacheFactory mf = new DefaultMustacheFactory();
		// final Mustache mustache = mf.compile(new StringReader(text), "retraite");
		// mustache.execute(writer, variables);
		// writer.flush();
		// return writer.toString();

		// Handlebars
		try {

			final Context context = Context.newBuilder(variables).build();

			final Handlebars handlebars = new Handlebars();
			final Template template = handlebars.compileInline(text.replaceAll("(\\{\\{)(?!#|/|\\^)", "{{& ")); //pour escape html chars

			final String result = template.apply(context);
			context.destroy();
			return result;
		} catch (final Exception e) {
			throw new RetraiteException("Error replacing variables", e);
		}
	}

}
