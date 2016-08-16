package utils;

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

}
