package utils.engine.utils;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import utils.engine.DateProviderFake;

public class AgeCalculatorTest {

	private AgeCalculator ageCalculator;

	@Before
	public void setUp() {
		ageCalculator = new AgeCalculator(new DateProviderFake(24, 12, 2015));
	}

	@Test
	public void shouldReturnAge() {
		assertThat(ageCalculator.getAge("25/12/2014")).isEqualTo(0);
		assertThat(ageCalculator.getAge("22/12/2014")).isEqualTo(1);
		assertThat(ageCalculator.getAge("12/11/1954")).isEqualTo(61);
	}

}
