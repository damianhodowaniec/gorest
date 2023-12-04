package utils;

import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import org.testng.log4testng.Logger;

import java.util.ArrayList;

public class RestUtils {
    private RestUtils() {
    }

    private static final Logger logger = Logger.getLogger(RestUtils.class);

    public static boolean arePropertiesInJSONPaths(Response restResponse, String... jsonPaths) {
        ArrayList notPresentProperties = new ArrayList();

        try {
            String[] var3 = jsonPaths;
            int var4 = jsonPaths.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String jsonPath = var3[var5];
                if (restResponse.getBody().jsonPath().get(jsonPath) == null) {
                    notPresentProperties.add(jsonPath);
                }
            }

            notPresentProperties.forEach((s) -> {
                logger.error(String.format("There is no \"%s\" property", s));
            });
            return notPresentProperties.isEmpty();
        } catch (JsonPathException var7) {
            throw new IllegalStateException("You are trying to get jsonBody while response does not contain JSON.");
        }
    }
}

