package utils.engine.utils;

import java.util.Map;

public interface VariablesReplacer {

	String replaceVariables(String text, Map<String, String> variables);
}
