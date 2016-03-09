package utils.engine.data;

import java.util.HashMap;
import java.util.Map;

public class UserChecklistVars {

	private final Map<String, String> map = new HashMap<String, String>();

	public void put(final String key, final String value) {
		map.put(key, value);
	}

	public Map<String, String> getMapOfValues() {
		return map;
	}

}
