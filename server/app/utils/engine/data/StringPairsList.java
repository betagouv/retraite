package utils.engine.data;

import java.util.ArrayList;

public class StringPairsList extends ArrayList<StringPair> {

	public void add(final String key, final String value) {
		add(new StringPair(key, value));
	}
}
