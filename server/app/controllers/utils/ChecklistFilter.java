package controllers.utils;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import models.Checklist;

public class ChecklistFilter {

	public List<Checklist> filter(final List<Checklist> checklists, final String authorizedChecklistsStr) {
		final List<String> authorizedChecklists = splitAndTrimAsList(authorizedChecklistsStr);
		final ArrayList<Checklist> newList = new ArrayList<>();
		for (final Checklist checklist : checklists) {
			if (authorizedChecklists.contains(checklist.nom)) {
				newList.add(checklist);
			}
		}
		return newList;
	}

	private List<String> splitAndTrimAsList(final String authorizedChecklistsStr) {
		return asList(trim(authorizedChecklistsStr.split(",")));
	}

	private String[] trim(final String[] src) {
		final String[] dest = new String[src.length];
		for (int i = 0; i < src.length; i++) {
			dest[i] = src[i].trim();
		}
		return dest;
	}

}
