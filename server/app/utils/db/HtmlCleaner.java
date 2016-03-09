package utils.db;

public class HtmlCleaner {

	public String clean(final String html) {
		if (html == null) {
			return html;
		}
		return html
				.replace("<span class=\"Apple-converted-space\"> </span>", "")
				.replace("<span class=\"Apple-converted-space\">", "");
	}

}
