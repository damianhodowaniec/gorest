package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationUtils {
    private ConfigurationUtils() {
    }

    public static String getProperty(String path, String property) {
        try {
            FileInputStream propFile = new FileInputStream(path);
            Properties prop;
            try {
                prop = new Properties();
                prop.load(propFile);
            } catch (Throwable var7) {
                try {
                    propFile.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
                throw var7;
            }
            propFile.close();
            return prop.getProperty(property);
        } catch (IOException var8) {
            System.out.println("Couldn't read property file!");
            System.out.println(var8.getMessage());
            return null;
        }
    }
}
