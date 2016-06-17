package utils;

public class TextUtils {

	public static boolean isLikeEmpty(final String html) {
		final String htmlWithoutTags = html.replaceAll("\\<.*?>", "");
		return htmlWithoutTags.trim().isEmpty();
	}

}
