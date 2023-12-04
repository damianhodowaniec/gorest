package runners;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = {"src/main/resources/features/Users.feature"},
        glue = "steps",
        plugin = {
                "json:target/UsersRunner/UsersRunner.json",
                "html:target/UsersRunner/cucumber.html",
                "summary"},
        monochrome = true
)
public class UsersRunner extends AbstractTestRunner {
}
