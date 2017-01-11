package controllers;


import static org.fest.assertions.Assertions.assertThat;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ApplicationTest {

	private Application application;
	
	@Before
	public void setUp() {
		application = new Application();
	}
	
	
	@Test
	public void getImgDatasFromJsonStr_should_return_empty_with_null() {
		final String jsonStr = null;
		
		List<String> imgDataList = application.getImgDatasFromJsonStr(jsonStr);
		
		assertThat(imgDataList).isEmpty();
	}
	
	@Test
	public void getImgDatasFromJsonStr_should_return_list_with_2_elts() {
		final String imgPrefix = "data:image/png;base64,";
		final String img1 = "img13256464";
		final String img2 = "ismldkfmlskdfmlk";
		final String jsonStr = imgPrefix + img1 + "," + imgPrefix + img2;
		
		List<String> imgDataList = application.getImgDatasFromJsonStr(jsonStr);
		
		assertThat(imgDataList).isNotEmpty();
		assertThat(imgDataList).hasSize(2);
		assertThat(imgDataList).containsExactly(imgPrefix + img1, imgPrefix + img2);
		
	}
	
}
