package utils;

import static utils.engine.EngineUtils.firstNotNull;

import java.lang.reflect.Field;

public class ObjectsUtils {

	public static void copyFields(final Object from, final Object to) {
		final Field[] fields = from.getClass().getFields();
		for (final Field fieldFrom : fields) {
			try {
				final Field fieldTo = to.getClass().getField(fieldFrom.getName());
				fieldTo.set(to, fieldFrom.get(from));
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			}
		}
	}

	public static void copyHiddenFieldsExceptStep(final Object from, final Object to) {
		try {
			for (final Field field : from.getClass().getFields()) {
				final String fieldName = field.getName();
				if (fieldName.startsWith("hidden_") && !fieldName.toLowerCase().contains("step")) {
					final Object hiddenData = field.get(from);
					final Object noHiddenData = tryToGetNoHiddenData(from, fieldName);
					field.set(to, firstNotNull(noHiddenData, hiddenData));
				}
			}
		} catch (final Exception e) {
			throw new RetraiteException("Error copying hidden fields", e);
		}
	}

	public static void synchronizedHiddenAndNotHiddenFields(final Object o) {
		try {
			for (final Field field : o.getClass().getFields()) {
				final String fieldName = field.getName();
				if (fieldName.startsWith("hidden_")) {
					final String destFieldName = fieldName.substring("hidden_".length());
					copyValueItDestNotNull(o, field, destFieldName);
				} else {
					copyValueItDestNotNull(o, field, "hidden_" + fieldName);
				}
			}
		} catch (final Exception e) {
			throw new RetraiteException("Error synchronizing fields and hidden fields", e);
		}
	}

	private static void copyValueItDestNotNull(final Object o, final Field field, final String destFieldName) throws IllegalAccessException {
		try {
			final Field destField = o.getClass().getField(destFieldName);
			final Object currentValue = destField.get(o);
			if (currentValue == null || currentValue.equals("")) {
				final Object value = field.get(o);
				destField.set(o, value);
			}
		} catch (final NoSuchFieldException e) {
			// Le champ n'existe pas, pas de copie
		}
	}

	// Méthodes privées

	private static Object tryToGetNoHiddenData(final Object data, final String fieldName) throws IllegalAccessException {
		final String noHiddenFieldName = fieldName.substring("hidden_".length());
		try {
			final Field fieldInPostData = data.getClass().getField(noHiddenFieldName);
			return fieldInPostData.get(data);
		} catch (final NoSuchFieldException e) {
			return null;
		}
	}

}
