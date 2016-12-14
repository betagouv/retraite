package utils.engine.utils;

public class LinkConverter {
	
	public static String convertTextForLink(final String link, Boolean delSuffix) {
		if (link.toLowerCase().startsWith("http://")) {
			return convertTextForLink(link.substring("http://".length()), delSuffix);
		}
		if (link.toLowerCase().startsWith("https://")) {
			return convertTextForLink(link.substring("https://".length()), delSuffix);
		}
		if (link.toLowerCase().startsWith("www.")) {
			return convertTextForLink(link.substring("www.".length()), delSuffix);
		}
		if (delSuffix) {
			if (link.toLowerCase().endsWith(".html")) {
				return convertTextForLink(link.substring(0, link.length() - ".html".length()), delSuffix);
			}
			if (link.toLowerCase().endsWith(".htm")) {
				return convertTextForLink(link.substring(0, link.length() - ".htm".length()), delSuffix);
			}
			if (link.toLowerCase().endsWith(".pdf")) {
				return convertTextForLink(link.substring(0, link.length() - ".pdf".length()), delSuffix);
			}
		}
		return link;
	}
	
	public static String convertTextForLink(final String link) {
		return convertTextForLink(link, Boolean.TRUE);
	}
}
