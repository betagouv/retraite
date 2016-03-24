package controllers.utils;

import java.util.ArrayList;
import java.util.List;

import utils.engine.data.enums.UserStatus;

public class DataUnbinder {

	public static List<UserStatus> unbind(final String userStatusStr) {
		if (userStatusStr == null || userStatusStr.isEmpty()) {
			return null;
		}
		final String str = userStatusStr.replace("[", "").replace("]", "");
		final String[] statusStrs = str.split(",");
		final ArrayList<UserStatus> status = new ArrayList<>();
		for (final String aStatusStr : statusStrs) {
			status.add(UserStatus.valueOf(aStatusStr.trim()));
		}
		return status;
	}

}
