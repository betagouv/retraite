package utils.engine.intern;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import utils.engine.DateProviderFake;
import utils.engine.data.ConditionDelai;
import utils.engine.data.MonthAndYear;

public class UserChecklistConditionDelaiEvaluatorTest {

	private DateProviderFake dateProviderFake;

	private UserChecklistConditionDelaiEvaluator userChecklistConditionDelaiEvaluator;

	@Before
	public void setUp() {

		dateProviderFake = new DateProviderFake(0, 0, 0);

		userChecklistConditionDelaiEvaluator = new UserChecklistConditionDelaiEvaluator(dateProviderFake);
	}

	@Test
	public void test_moins_de_x_mois_true() {

		whenTheCurrentDateIs(2, 6, 2016);
		final ConditionDelai conditionDelai = new ConditionDelai("MOINS", "3", "MOIS");
		final MonthAndYear dateDepart = new MonthAndYear("9", "2016");

		final boolean isVerified = userChecklistConditionDelaiEvaluator.isVerified(conditionDelai, dateDepart);

		assertThat(isVerified).isTrue();
	}

	@Test
	public void test_moins_de_x_mois_false() {

		whenTheCurrentDateIs(30, 5, 2016);
		final ConditionDelai conditionDelai = new ConditionDelai("MOINS", "3", "MOIS");
		final MonthAndYear dateDepart = new MonthAndYear("9", "2016");

		final boolean isVerified = userChecklistConditionDelaiEvaluator.isVerified(conditionDelai, dateDepart);

		assertThat(isVerified).isFalse();
	}

	@Test
	public void test_plus_de_x_mois_false() {

		whenTheCurrentDateIs(2, 6, 2016);
		final ConditionDelai conditionDelai = new ConditionDelai("plus", "3", "mois");
		final MonthAndYear dateDepart = new MonthAndYear("9", "2016");

		final boolean isVerified = userChecklistConditionDelaiEvaluator.isVerified(conditionDelai, dateDepart);

		assertThat(isVerified).isFalse();
	}

	@Test
	public void test_plus_de_x_mois_true() {

		whenTheCurrentDateIs(30, 5, 2016);
		final ConditionDelai conditionDelai = new ConditionDelai("PLUS", "3", "MOIS");
		final MonthAndYear dateDepart = new MonthAndYear("9", "2016");

		final boolean isVerified = userChecklistConditionDelaiEvaluator.isVerified(conditionDelai, dateDepart);

		assertThat(isVerified).isTrue();
	}

	@Test
	public void test_moins_de_x_annees_true() {

		whenTheCurrentDateIs(2, 6, 2016);
		final ConditionDelai conditionDelai = new ConditionDelai("MOINS", "1", "ANNEES");
		final MonthAndYear dateDepart = new MonthAndYear("6", "2017");

		final boolean isVerified = userChecklistConditionDelaiEvaluator.isVerified(conditionDelai, dateDepart);

		assertThat(isVerified).isTrue();
	}

	@Test
	public void test_moins_de_x_annees_false() {

		whenTheCurrentDateIs(30, 5, 2016);
		final ConditionDelai conditionDelai = new ConditionDelai("moins", "1", "annees");
		final MonthAndYear dateDepart = new MonthAndYear("6", "2017");

		final boolean isVerified = userChecklistConditionDelaiEvaluator.isVerified(conditionDelai, dateDepart);

		assertThat(isVerified).isFalse();
	}

	private void whenTheCurrentDateIs(final int dayOfMonth, final int month, final int year) {
		dateProviderFake.setCurrentDate(dayOfMonth, month, year);
	}
}
