package utils;

import static org.fest.assertions.Assertions.assertThat;
import static utils.StringsTestsUtils.assertThatString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class JsonUtilsTest {

	@Test
	public void toJson_should_return_json_string_for_basic_object() throws Exception {

		final DataTest data = new DataTest("Dupont", 98);

		final String jsonString = JsonUtils.toJson(data);

		assertThat(jsonString).isEqualTo("{\"name\":\"Dupont\",\"age\":98}");
	}

	@Test
	public void toJson_should_ignore_JsonExclude() throws Exception {

		final ExtendedDataTest data = new ExtendedDataTest("Dupont", 98);

		final String jsonString = JsonUtils.toJson(data);

		assertThat(jsonString).isEqualTo("{\"extraProp\":\"98Dupont\",\"name\":\"Dupont\",\"age\":98}");
	}

	@Test
	public void toJson_should_return_json_string_for_map() throws Exception {

		final Map<String, DataTest> map = new HashMap<String, DataTest>();
		map.put("i", new DataTest("Dupont", 98));
		map.put("j", new DataTest("Durand", 97));

		final String jsonString = JsonUtils.toJson(map);

		assertThatString(jsonString)
				.contains("{")
				.contains("\"i\":{\"name\":\"Dupont\",\"age\":98}")
				.contains("\"j\":{\"name\":\"Durand\",\"age\":97}")
				.contains(",")
				.andFinallyContains("}");
	}

	@Test
	public void toJson_should_return_json_string_for_list() throws Exception {

		final List<DataTest> list = new ArrayList<DataTest>();
		list.add(new DataTest("Dupont", 98));
		list.add(new DataTest("Durand", 97));

		final String jsonString = JsonUtils.toJson(list);

		assertThat(jsonString).isEqualTo("[{\"name\":\"Dupont\",\"age\":98},{\"name\":\"Durand\",\"age\":97}]");
	}

	@Test
	public void fromJson_should_return_basic_object() throws Exception {

		final DataTest result = JsonUtils.fromJson("{\"name\":\"Dupont\",\"age\":98}", DataTest.class);

		assertThat(result).isEqualTo(new DataTest("Dupont", 98));
	}

	@Test
	public void fromJson_should_return_map() throws Exception {

		final Map result = JsonUtils.fromJson("{\"i\":{\"name\":\"Dupont\",\"age\":98},\"j\":{\"name\":\"Durand\",\"age\":97}}", MapDataTest.class);

		final Map<String, DataTest> map = new HashMap<String, DataTest>();
		map.put("i", new DataTest("Dupont", 98));
		map.put("j", new DataTest("Durand", 97));
		assertThat(result).isEqualTo(map);
	}

	@Test
	public void fromJson_should_return_list() throws Exception {

		final List result = JsonUtils.fromJson("[{\"name\":\"Dupont\",\"age\":98},{\"name\":\"Durand\",\"age\":97}]", ListDataTest.class);

		final List<DataTest> list = new ArrayList<DataTest>();
		list.add(new DataTest("Dupont", 98));
		list.add(new DataTest("Durand", 97));
		assertThat(result).isEqualTo(list);
	}

	@Test
	public void convertQuotesForJson_should_convert_quotes() throws Exception {

		final String result = JsonUtils.convertQuotesForJson("a'e\"c");

		assertThat(result).isEqualTo("a\"e\"c");
	}

	// Méthodes privées

	private static final class ExtendedDataTest extends DataTest {

		@SuppressWarnings("unused")
		private final String extraProp;

		@JsonExclude
		private final String propToIgnore;

		public ExtendedDataTest(final String name, final int age) {
			super(name, age);
			this.propToIgnore = name + age;
			this.extraProp = age + name;
		}

	}

	private static class DataTest {
		private final String name;
		private final int age;

		public DataTest(final String name, final int age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + age;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final DataTest other = (DataTest) obj;
			if (age != other.age)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

	}

	private static final class MapDataTest extends HashMap<String, DataTest> {
	}

	private static final class ListDataTest extends ArrayList<DataTest> {
	}
}
