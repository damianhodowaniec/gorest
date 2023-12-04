package data;

import utils.ConfigurationUtils;

public class PropertiesData {
    private PropertiesData() {
    }

    public static final String PROPERTIES_PATH = "src/main/resources/api.properties";

    public static String getApiProperty(String property) {
        return ConfigurationUtils.getProperty(PROPERTIES_PATH, property);
    }
}
