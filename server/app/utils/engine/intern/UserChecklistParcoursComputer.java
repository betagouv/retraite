package utils.engine.intern;

import static utils.TextUtils.isLikeEmpty;

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

	public String compute(final String text, final UserChecklistGenerationData userChecklistGenerationData) {
		if (text == null) {
			return null;
		}
		if (isLikeEmpty(text)) {
			return null;
		}
		return replaceVars(replaceLinks(text), userChecklistGenerationData);
	}

	private String replaceVars(final String text, final UserChecklistGenerationData userChecklistGenerationData) {
		final Map<String, String> vars = userChecklistVarsProvider.provideVars(userChecklistGenerationData).getMapOfValues();
		try {
			return variablesReplacer.replaceVariables(text, vars);
		} catch (final RetraiteException e) {
			Logger.error(e, "Erreur lors du remplacement des variables");
			return text;
		}
	}

	private String replaceLinks(final String text) {
		return replaceLinks(text, 0);
	}

	private String replaceLinks(final String text, final int fromIndex) {
		final int beginIndex = searchBeginIndex(text, fromIndex);
		if (beginIndex == -1) {
			return text;
		}
		final int endIndex = searchEndIndex(text, beginIndex);
		final String beforeLink = text.substring(0, beginIndex);
		final String link = text.substring(beginIndex, endIndex);
		final String buildedLink = buildLink(link);
		final String afterLink = text.substring(endIndex);
		final String newText = beforeLink + buildedLink + afterLink;
		return replaceLinks(newText, beforeLink.length() + buildedLink.length());
	}

	private int searchBeginIndex(final String text, final int fromIndex) {
		int beginIndex = text.indexOf("http://", fromIndex);
		if (beginIndex == -1) {
			beginIndex = text.indexOf("https://", fromIndex);
		}
		return beginIndex;
	}

	private int searchEndIndex(final String text, final int beginIndex) {
		final int endIndex = RetraiteStringsUtils.getMinIndex(
				text.length(),
				text.indexOf(" ", beginIndex),
				text.indexOf("<", beginIndex));
		if (endIndex == -1) {
			return -1;
		}
		return skipSpecialCharAfterLink(text, endIndex);
	}

	private int skipSpecialCharAfterLink(final String text, final int endIndex) {
		final char lastCharInLink = text.charAt(endIndex - 1);
		if (lastCharInLink == '.') {
			return skipSpecialCharAfterLink(text, endIndex - 1);
		}
		return endIndex;
	}

	private String buildLink(final String link) {
		return "<a href='" + link + "' target='_blank' title='Nouvelle fenêtre'>" + convertTextForLink(link) + "</a>";
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

}
