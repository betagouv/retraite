package utils.engine.data;

import java.util.HashMap;
import java.util.Map;

public class UserChecklistVars {

	private final Map<String, Object> map = new HashMap<String, Object>();

	public void put(final String key, final Object value) {
		map.put(key, value);
	}

	public Map<String, Object> getMapOfValues() {
		return map;
	}

}
