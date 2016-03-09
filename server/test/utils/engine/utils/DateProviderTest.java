package utils.engine.utils;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import utils.DateUtils;

public class DateProviderTest {

	@Test
	@Ignore("Test temporel, non valable tout le temps (dépend de l'année en cours)")
	public void should_return_current_year() {
		assertThat(new DateProvider().getCurrentYear()).isEqualTo(2016);
	}

	@Test
	@Ignore("Test temporel, non valable tout le temps (dépend de l'année en cours)")
	public void should_return_current_date() {
		assertThat(DateUtils.format(new DateProvider().getCurrentDate())).isEqualTo("17/02/2016");
	}

}
