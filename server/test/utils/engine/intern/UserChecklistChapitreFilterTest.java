package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import models.Chapitre;
import models.Condition;
import utils.engine.data.UserChecklistGenerationData;

public class UserChecklistChapitreFilterTest {

	private static final UserChecklistGenerationData userChecklistGenerationData = UserChecklistGenerationData.create().get();

	private UserChecklistConditionEvaluator userChecklistConditionEvaluatorMock;

	private UserChecklistChapitreFilter userChecklistChapitreFilter;

	@Before
	public void setUp() {

		userChecklistConditionEvaluatorMock = mock(UserChecklistConditionEvaluator.class);

		userChecklistChapitreFilter = new UserChecklistChapitreFilter(userChecklistConditionEvaluatorMock);
	}

	@Test
	public void should_display_if_no_conditions() {

		final Chapitre chapitre = new Chapitre();

		final boolean toBeDisplayed = userChecklistChapitreFilter.isToBeDisplayed(chapitre, userChecklistGenerationData);

		assertThat(toBeDisplayed).isTrue();
	}

	@Test
	public void should_display_if_all_conditions_are_true() {

		final Chapitre chapitre = new Chapitre();
		chapitre.conditions = new ArrayList<>();
		chapitre.conditions.add(new Condition());
		chapitre.conditions.add(new Condition());
		chapitre.conditions.add(new Condition());

		when(userChecklistConditionEvaluatorMock.isVerified(any(Condition.class), any(UserChecklistGenerationData.class))).thenReturn(true, true, true);

		final boolean toBeDisplayed = userChecklistChapitreFilter.isToBeDisplayed(chapitre, userChecklistGenerationData);

		assertThat(toBeDisplayed).isTrue();
	}

	@Test
	public void should_not_display_if_one_or_more_conditions_are_false() {

		final Chapitre chapitre = new Chapitre();
		chapitre.conditions = new ArrayList<>();
		chapitre.conditions.add(new Condition());
		chapitre.conditions.add(new Condition());
		chapitre.conditions.add(new Condition());

		when(userChecklistConditionEvaluatorMock.isVerified(any(Condition.class), any(UserChecklistGenerationData.class))).thenReturn(true, false, true);

		final boolean toBeDisplayed = userChecklistChapitreFilter.isToBeDisplayed(chapitre, userChecklistGenerationData);

		assertThat(toBeDisplayed).isFalse();
	}
}
