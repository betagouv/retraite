package utils;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class JsonUtils {

	private static Gson gson = new GsonBuilder().setExclusionStrategies(new GsonProjectExclusionStrategy()).serializeSpecialFloatingPointValues()
			.create();

	public static String toJson(final Object object) {
		if (object instanceof Map) {
			final Type typeOfMap = new TypeToken<Map>() {
			}.getType();
			return gson.toJson(object, typeOfMap);
		}
		return gson.toJson(object);
	}

	public static <T> T fromJson(final String jsonString, final Class<T> typeToCreate) {
		return new Gson().fromJson(jsonString, typeToCreate);
	}

	public static <T> T fromJson(final String jsonString, final Type typeToCreate) {
		try {
			return new Gson().fromJson(jsonString, typeToCreate);
		} catch (final JsonSyntaxException e) {
			throw new RetraiteException("Error deconding JSON '" + jsonString + "'", e);
		}
	}

	public static String convertQuotesForJson(final String text) {
		return text.replace("\\'", "£££").replace("'", "\"").replace("£££", "'");
	}

	private static class GsonProjectExclusionStrategy implements ExclusionStrategy {

		@Override
		public boolean shouldSkipField(final FieldAttributes f) {
			return f.getAnnotation(JsonExclude.class) != null;
		}

		@Override
		public boolean shouldSkipClass(final Class<?> clazz) {
			return false;
		}

	}

}
