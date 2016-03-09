package utils;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.min;

public class RetraiteStringsUtils {

	public static int getMinIndex(final int... indexes) {
		int min = MAX_VALUE;
		for (final int index : indexes) {
			if (index >= 0) {
				min = min(min, index);
			}
		}
		return min == MAX_VALUE ? -1 : min;
	}
}
