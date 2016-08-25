package utils.engine;

import java.util.List;

import utils.engine.data.enums.RegimeAligne;
import utils.engine.data.enums.UserStatus;

public class EngineUtils {

	public static boolean areRegimes(final RegimeAligne[] regimesAlignes, final RegimeAligne... regimesToSearch) {
		if (regimesAlignes.length != regimesToSearch.length) {
			return false;
		}
		for (final RegimeAligne regimeToSearch : regimesToSearch) {
			if (!contains(regimesAlignes, regimeToSearch)) {
				return false;
			}

		}
		return true;
	}

	public static <T> boolean contains(final T[] array, final T... elts) {
		for (final T elt : elts) {
			if (!contains(array, elt)) {
				return false;
			}
		}
		return true;
	}

	public static <T> boolean contains(final T[] array, final T elt) {
		if (array != null) {
			for (final T regimeAligne : array) {
				if (regimeAligne.equals(elt)) {
					return true;
				}
			}
		}
		return false;
	}

	public static <T> boolean contains(final List<UserStatus> list, final UserStatus elt) {
		return list == null ? false : list.contains(elt);
	}

	public static <T> T firstNotNull(final T... objects) {
		String bestStringResult = null;
		for (final T object : objects) {
			if (object != null) {
				if (isStringEmpty(object) && bestStringResult == null) {
					bestStringResult = "";
					continue;
				}
				return object;
			}
		}
		if (bestStringResult != null) {
			return (T) bestStringResult;
		}
		return null;
	}

	private static <T> boolean isStringEmpty(final T object) {
		return object.getClass().equals(String.class) && ((String) object).isEmpty();
	}

}
