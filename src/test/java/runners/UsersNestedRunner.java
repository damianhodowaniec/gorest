package runners;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src/main/resources/features/UsersNested.feature"},
        glue = "steps",
        plugin = {
                "json:target/UsersNestedRunner/UsersNestedRunner.json",
                "html:target/UsersNestedRunner/cucumber.html",
                "summary"},
        monochrome = true
)
public class UsersNestedRunner extends AbstractTestRunner {
}
