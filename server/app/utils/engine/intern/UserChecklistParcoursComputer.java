package utils.engine.intern;

import java.util.Map;

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

	public String compute(final String parcours, final UserChecklistGenerationData userChecklistGenerationData) {
		if (parcours == null) {
			return null;
		}
		if (isLikeEmpty(parcours)) {
			return null;
		}
		return replaceVars(replaceLinks(parcours), userChecklistGenerationData);
	}

	private boolean isLikeEmpty(final String html) {
		final String htmlWithoutTags = html.replaceAll("\\<.*?>", "");
		return htmlWithoutTags.trim().isEmpty();
	}

	private String replaceVars(final String text, final UserChecklistGenerationData userChecklistGenerationData) {
		final Map<String, String> vars = userChecklistVarsProvider.provideVars(userChecklistGenerationData).getMapOfValues();
		return variablesReplacer.replaceVariables(text, vars);
	}

	private String replaceLinks(final String parcours) {
		return replaceLinks(parcours, 0);
	}

	private String replaceLinks(final String parcours, final int fromIndex) {
		final int beginIndex = searchBeginIndex(parcours, fromIndex);
		if (beginIndex == -1) {
			return parcours;
		}
		final int endIndex = searchEndIndex(parcours, beginIndex);
		final String beforeLink = parcours.substring(0, beginIndex);
		final String link = parcours.substring(beginIndex, endIndex);
		final String buildedLink = buildLink(link);
		final String afterLink = parcours.substring(endIndex);
		final String newParcours = beforeLink + buildedLink + afterLink;
		return replaceLinks(newParcours, beforeLink.length() + buildedLink.length());
	}

	private int searchBeginIndex(final String parcours, final int fromIndex) {
		int beginIndex = parcours.indexOf("http://", fromIndex);
		if (beginIndex == -1) {
			beginIndex = parcours.indexOf("https://", fromIndex);
		}
		return beginIndex;
	}

	private int searchEndIndex(final String parcours, final int beginIndex) {
		final int endIndex = RetraiteStringsUtils.getMinIndex(
				parcours.length(),
				parcours.indexOf(" ", beginIndex),
				parcours.indexOf("<", beginIndex));
		if (endIndex == -1) {
			return -1;
		}
		return skipSpecialCharAfterLink(parcours, endIndex);
	}

	private int skipSpecialCharAfterLink(final String parcours, final int endIndex) {
		final char lastCharInLink = parcours.charAt(endIndex - 1);
		if (lastCharInLink == '.') {
			return skipSpecialCharAfterLink(parcours, endIndex - 1);
		}
		return endIndex;
	}

	private String buildLink(final String link) {
		return "<a href='" + link + "' target='_blank' title='Nouvelle fenêtre'>" + convertTextForLink(link) + "</a>";
	}

	private String convertTextForLink(final String link) {
		if (link.startsWith("http://")) {
			return convertTextForLink(link.substring("http://".length()));
		}
		if (link.startsWith("https://")) {
			return convertTextForLink(link.substring("https://".length()));
		}
		if (link.startsWith("www.")) {
			return convertTextForLink(link.substring("www.".length()));
		}
		if (link.endsWith(".html")) {
			return convertTextForLink(link.substring(0, link.length() - ".html".length()));
		}
		if (link.endsWith(".htm")) {
			return convertTextForLink(link.substring(0, link.length() - ".htm".length()));
		}
		return link;
	}

}
