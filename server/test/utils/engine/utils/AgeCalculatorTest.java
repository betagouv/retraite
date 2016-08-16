package utils.engine.utils;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import utils.RetraiteBadNaissanceFormatException;
import utils.engine.DateProviderFake;

public class AgeCalculatorTest {

	private AgeCalculator ageCalculator;

	@Before
	public void setUp() {
		ageCalculator = new AgeCalculator(new DateProviderFake(24, 12, 2015));
	}

	@Test
	public void should_return_age() {
		assertThat(ageCalculator.getAge("25/12/2014")).isEqualTo(0);
		assertThat(ageCalculator.getAge("22/12/2014")).isEqualTo(1);
		assertThat(ageCalculator.getAge("12/11/1954")).isEqualTo(61);
	}

	@Test
	public void should_throw_exception() {
		try {
			ageCalculator.getAge("2512/2014");
			fail("Devrait déclencher une exception");
		} catch (final RetraiteBadNaissanceFormatException e) {
		}
		try {
			ageCalculator.getAge("25/122014");
			fail("Devrait déclencher une exception");
		} catch (final RetraiteBadNaissanceFormatException e) {
		}
		try {
			ageCalculator.getAge("25122014");
			fail("Devrait déclencher une exception");
		} catch (final RetraiteBadNaissanceFormatException e) {
		}
		try {
			ageCalculator.getAge("31/11/2014");
			fail("Devrait déclencher une exception");
		} catch (final RetraiteBadNaissanceFormatException e) {
		}
		try {
			ageCalculator.getAge("00/12/2014");
			fail("Devrait déclencher une exception");
		} catch (final RetraiteBadNaissanceFormatException e) {
		}
		try {
			ageCalculator.getAge("22/00/2014");
			fail("Devrait déclencher une exception");
		} catch (final RetraiteBadNaissanceFormatException e) {
		}
		try {
			ageCalculator.getAge("12/11/0000");
			fail("Devrait déclencher une exception");
		} catch (final RetraiteBadNaissanceFormatException e) {
		}
	}

}
