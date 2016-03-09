package poc.mustache;

import static org.fest.assertions.Assertions.assertThat;

import java.util.HashMap;

import org.junit.Test;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

public class PocHandlebarsTest {

	@Test
	public void test_mustache() throws Exception {

		final HashMap<String, Object> data = new HashMap<>();
		data.put("name", "Jhon");

		final Context context = Context.newBuilder(data).build();

		final Handlebars handlebars = new Handlebars();
		final Template template = handlebars.compileInline("Hello {{name}}, !");

		final String result = template.apply(context);
		context.destroy();

		assertThat(result).isEqualTo("Hello Jhon, !");
	}
}
