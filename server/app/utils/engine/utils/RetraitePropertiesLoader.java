package utils.engine.utils;

import java.util.Properties;

import cnav.architech.usilog.socle.framework.base.properties.PropertiesLoader;

public final class RetraitePropertiesLoader {

	private static RetraitePropertiesLoader instance;
	
	private Properties properties;
	
	private RetraitePropertiesLoader() {

		PropertiesLoader propertiesLoader = new PropertiesLoader();
		properties = propertiesLoader.loadProperties(RetraiteConstants.PROPERTIES_FILENAME);
		
	}
	
	public static synchronized RetraitePropertiesLoader getInstance() {
		if (null == instance) {
			instance = new RetraitePropertiesLoader();
		}
		return instance;
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
}
