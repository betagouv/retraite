package utils.engine.intern;

import static utils.TextUtils.isLikeEmpty;

import java.util.List;
import java.util.Map;

import play.Logger;
import utils.RetraiteException;
import utils.RetraiteStringsUtils;
import utils.engine.data.UserChecklistGenerationData;
import utils.engine.utils.VariablesReplacer;

public class UserChecklistParcoursComputer {

	private final UserChecklistVarsProvider userChecklistVarsProvider;
	private final VariablesReplacer variablesReplacer;

	public UserChecklistParcoursComputer(final UserChecklistVarsProvider userChecklistVarsProvider, final VariablesReplacer variablesReplacer) {
		this.userChecklistVarsProvider = userChecklistVarsProvider;
		this.variablesReplacer = variablesReplacer;
	}

	public String compute(final String text, final UserChecklistGenerationData userChecklistGenerationData, final List<String> links) {
		if (text == null) {
			return null;
		}
		if (isLikeEmpty(text)) {
			return null;
		}
		final boolean isPDF = userChecklistGenerationData != null && userChecklistGenerationData.isPDF;
		final String result = replaceVars(replaceLinks(text, isPDF, links), userChecklistGenerationData);

		if (isLikeEmpty(result)) {
			return null;
		}
		return result;
	}

	private String replaceVars(final String text, final UserChecklistGenerationData userChecklistGenerationData) {
		final Map<String, Object> vars = userChecklistVarsProvider.provideVars(userChecklistGenerationData).getMapOfValues();
		try {
			return variablesReplacer.replaceVariables(text, vars);
		} catch (final RetraiteException e) {
			Logger.error(e, "Erreur lors du remplacement des variables");
			return text;
		}
	}

	private String replaceLinks(final String text, final boolean isPDF, final List<String> links) {
		return replaceLinks(text, 0, isPDF, links);
	}

	private String replaceLinks(final String text, final int fromIndex, final boolean isPDF, final List<String> links) {
		final BeginIndex beginIndex = searchBeginIndex(text, fromIndex);
		if (beginIndex == BeginIndex.NONE) {
			return text;
		}
		final int endIndex = searchEndIndexForLink(beginIndex.type, text, beginIndex.index);
		final String beforeLink = text.substring(0, beginIndex.index);
		final String link = text.substring(beginIndex.type == BeginIndexType.SIMPLE ? beginIndex.index : beginIndex.index + 2, endIndex);
		final String buildedLink = buildLink(beginIndex.type, link, isPDF, links);
		final String afterLink = text.substring(beginIndex.type == BeginIndexType.SIMPLE ? endIndex : endIndex + 2);
		final String newText = beforeLink + buildedLink + afterLink;
		return replaceLinks(newText, beforeLink.length() + buildedLink.length(), isPDF, links);
	}

	private int searchEndIndexForLink(final BeginIndexType type, final String text, final int beginIndex) {
		if (type == BeginIndexType.SIMPLE) {
			return searchEndIndexForSimpleLink(text, beginIndex);
		}
		return searchEndIndexForAdvancedLink(text, beginIndex);
	}

	private BeginIndex searchBeginIndex(final String text, final int fromIndex) {
		final int beginIndexHttp = text.indexOf("http://", fromIndex);
		final int beginIndexHttps = text.indexOf("https://", fromIndex);
		final int beginIndexBrackets = text.indexOf("[[", fromIndex);
		final int beginIndex = minIndex(minIndex(beginIndexHttp, beginIndexHttps), beginIndexBrackets);
		if (beginIndex != -1) {
			if (beginIndex == beginIndexBrackets) {
				return new BeginIndex(BeginIndexType.ADVANCED, beginIndex);
			}
			return new BeginIndex(BeginIndexType.SIMPLE, beginIndex);
		}
		return BeginIndex.NONE;
	}

	private int minIndex(final int index1, final int index2) {
		if (index1 == -1) {
			return index2;
		}
		if (index2 == -1) {
			return index1;
		}
		return Math.min(index1, index2);
	}

	private int searchEndIndexForSimpleLink(final String text, final int beginIndex) {
		final int endIndex = RetraiteStringsUtils.getMinIndex(
				text.length(),
				text.indexOf(" ", beginIndex),
				text.indexOf("<", beginIndex));
		if (endIndex == -1) {
			return -1;
		}
		return skipSpecialCharAfterLink(text, endIndex);
	}

	private int searchEndIndexForAdvancedLink(final String text, final int beginIndex) {
		return text.indexOf("]]", beginIndex);
	}

	private int skipSpecialCharAfterLink(final String text, final int endIndex) {
		final char lastCharInLink = text.charAt(endIndex - 1);
		if (lastCharInLink == '.') {
			return skipSpecialCharAfterLink(text, endIndex - 1);
		}
		return endIndex;
	}

	private String buildLink(final BeginIndexType type, final String link, final boolean isPDF, final List<String> links) {
		if (type == BeginIndexType.SIMPLE) {
			return buildLinkSimple(link, isPDF, links);
		}
		final String linkTrimed = link.trim();
		final int index = linkTrimed.indexOf("http");
		if (index == -1) {
			// Pas d'URL : on renvoie le texte
			return linkTrimed;
		}
		if (index == 0) {
			// Pas de texte, on traite comme un lien simple
			return buildLinkSimple(linkTrimed, isPDF, links);
		}
		final String textForLink = linkTrimed.substring(0, index).trim();
		final String url = linkTrimed.substring(index);
		String htmlLink = buildLink(url, textForLink);
		//if (isPDF) {
			links.add(url);
			htmlLink += "<sup>" + links.size() + "</sup>";
		//}
		return htmlLink;
	}

	private String buildLinkSimple(final String link, final boolean isPDF, final List<String> links) {
		String result = buildLink(link, convertTextForLink(link));
		if (isPDF) {
			links.add(link);
			result += "<sup>" + links.size() + "</sup>";
		}
		return result;
	}

	private String buildLink(final String url, final String textForLink) {
		return "<a href='" + url + "' target='_blank' title='Nouvelle fenêtre'>" + textForLink + "</a>";
	}

	private String convertTextForLink(final String link) {
		if (link.toLowerCase().startsWith("http://")) {
			return convertTextForLink(link.substring("http://".length()));
		}
		if (link.toLowerCase().startsWith("https://")) {
			return convertTextForLink(link.substring("https://".length()));
		}
		if (link.toLowerCase().startsWith("www.")) {
			return convertTextForLink(link.substring("www.".length()));
		}
		if (link.toLowerCase().endsWith(".html")) {
			return convertTextForLink(link.substring(0, link.length() - ".html".length()));
		}
		if (link.toLowerCase().endsWith(".htm")) {
			return convertTextForLink(link.substring(0, link.length() - ".htm".length()));
		}
		if (link.toLowerCase().endsWith(".pdf")) {
			return convertTextForLink(link.substring(0, link.length() - ".pdf".length()));
		}
		return link;
	}

	private static enum BeginIndexType {
		SIMPLE, ADVANCED
	}

	private static class BeginIndex {

		public static final BeginIndex NONE = new BeginIndex();

		private final BeginIndexType type;
		private final int index;

		public BeginIndex(final BeginIndexType type, final int index) {
			this.type = type;
			this.index = index;
		}

		private BeginIndex() {
			this(null, -1);
		}

	}

}
