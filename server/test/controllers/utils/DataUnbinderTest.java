package controllers.utils;

import static org.fest.assertions.Assertions.assertThat;
import static utils.engine.data.enums.UserStatus.STATUS_CONJOINT_COLLABORATEUR;
import static utils.engine.data.enums.UserStatus.STATUS_INVALIDITE_RSI;
import static utils.engine.data.enums.UserStatus.STATUS_SA;

import java.util.List;

import org.junit.Test;

import utils.engine.data.enums.UserStatus;

public class DataUnbinderTest {

	@Test
	public void test() {

		final String userStatusStr = "[ STATUS_SA , STATUS_CONJOINT_COLLABORATEUR,STATUS_INVALIDITE_RSI]";

		final List<UserStatus> userStatus = new DataUnbinder().unbind(userStatusStr);

		assertThat(userStatus).containsExactly(STATUS_SA, STATUS_CONJOINT_COLLABORATEUR, STATUS_INVALIDITE_RSI);

	}

}
