package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;


public abstract class AbstractTestRunner extends AbstractTestNGCucumberTests {
    protected static final Logger logger = LogManager.getLogger();

    @BeforeClass
    public static void beforeAll() {
        logger.info("TESTS STARTED");

    }

    @AfterClass
    public static void afterAll() {
        logger.info("TESTS ENDED");
    }
}
