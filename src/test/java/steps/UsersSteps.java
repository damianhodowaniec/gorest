package steps;

import context.ContextObjectContainer;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONObject;
import utils.RestUtils;

import static data.PropertiesData.getApiProperty;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.FileUtils.getCsvData;

public class UsersSteps {
    private static final Logger logger = LogManager.getLogger();
    private final ContextObjectContainer contextObjectContainer;

    public UsersSteps(ContextObjectContainer contextObjectContainer) {
        this.contextObjectContainer = contextObjectContainer;
    }

    @Then("Get list of users and verify properties")
    public void get_list_of_users() {
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .get("/users");
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getStatusCode()).isEqualTo(200);
        int userCount = Integer.parseInt(response.getBody().jsonPath().getString("size()"));
        for (int i = 0; i < userCount; i++) {
            softAssertions.assertThat(RestUtils.arePropertiesInJSONPaths(response,
                    String.format("[%s].id", i),
                    String.format("[%s].name", i),
                    String.format("[%s].email", i),
                    String.format("[%s].gender", i),
                    String.format("[%s].status", i))).isTrue();
        }
        softAssertions.assertAll();
    }

    @Then("Create new user")
    public void create_new_user() {
        JSONObject restBody = new JSONObject();
        restBody.put("name", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 0, 1));
        restBody.put("email", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 1, 1));
        restBody.put("gender", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 2, 1));
        restBody.put("status", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 3, 1));
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .header("Authorization", getApiProperty("token"))
                .body(restBody.toString())
                .post("/users");
        assertThat(response.getStatusCode()).isEqualTo(201);
        contextObjectContainer.usersDataObject.setId(response.jsonPath().getString("id"));
        contextObjectContainer.usersDataObject.setName(response.jsonPath().getString("name"));
        contextObjectContainer.usersDataObject.setEmail(response.jsonPath().getString("email"));
        contextObjectContainer.usersDataObject.setGender(response.jsonPath().getString("gender"));
        contextObjectContainer.usersDataObject.setStatus(response.jsonPath().getString("status"));
    }

    @Then("Get and verify user by id")
    public void get_user_by_id() {
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .header("Authorization", getApiProperty("token"))
                .get("/users/" + contextObjectContainer.usersDataObject.getId());
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getStatusCode()).isEqualTo(200);
        softAssertions.assertThat(response.getBody().jsonPath().getString("id")).isEqualTo(contextObjectContainer.usersDataObject.getId());
        softAssertions.assertThat(response.getBody().jsonPath().getString("name")).isEqualTo(contextObjectContainer.usersDataObject.getName());
        softAssertions.assertThat(response.getBody().jsonPath().getString("email")).isEqualTo(contextObjectContainer.usersDataObject.getEmail());
        softAssertions.assertThat(response.getBody().jsonPath().getString("gender")).isEqualTo(contextObjectContainer.usersDataObject.getGender());
        softAssertions.assertThat(response.getBody().jsonPath().getString("status")).isEqualTo(contextObjectContainer.usersDataObject.getStatus());
        softAssertions.assertAll();
    }

    @Then("Update user")
    public void update_user() {
        JSONObject restBody = new JSONObject();
        restBody.put("name", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 0, 2));
        restBody.put("email", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 1, 2));
        restBody.put("gender", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 2, 2));
        restBody.put("status", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 3, 2));
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .header("Authorization", getApiProperty("token"))
                .body(restBody.toString())
                .put("/users/" + contextObjectContainer.usersDataObject.getId());
        assertThat(response.getStatusCode()).isEqualTo(200);
        contextObjectContainer.usersDataObject.setName(response.jsonPath().getString("name"));
        contextObjectContainer.usersDataObject.setEmail(response.jsonPath().getString("email"));
        contextObjectContainer.usersDataObject.setGender(response.jsonPath().getString("gender"));
        contextObjectContainer.usersDataObject.setStatus(response.jsonPath().getString("status"));
    }

    @Then("Delete user")
    public void delete_user() {
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .header("Authorization", getApiProperty("token"))
                .delete("/users/" + contextObjectContainer.usersDataObject.getId());
        assertThat(response.getStatusCode()).isEqualTo(204);
    }

    @Then("User should be deleted")
    public void user_should_be_deleted() {
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .header("Authorization", getApiProperty("token"))
                .get("/users/" + contextObjectContainer.usersDataObject.getId());
        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Then("Try to create new user when not authorised")
    public void create_new_user_not_authorised() {
        JSONObject restBody = new JSONObject();
        restBody.put("name", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 0, 4));
        restBody.put("email", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 1, 4));
        restBody.put("gender", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 2, 4));
        restBody.put("status", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 3, 4));
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .body(restBody.toString())
                .post("/users");
        assertThat(response.getStatusCode()).isEqualTo(401);
    }

    @Then("Try to create empty user")
    public void create_empty_user() {
        JSONObject restBody = new JSONObject();
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .header("Authorization", getApiProperty("token"))
                .body(restBody.toString())
                .post("/users");
        assertThat(response.getStatusCode()).isEqualTo(422);
        logger.info(response.getBody().prettyPrint());

    }

    @Then("Get user that not exist")
    public void get_user_that_not_exist() {
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .get("/users/" + getApiProperty("wrong.id"));
        assertThat(response.getStatusCode()).isEqualTo(404);
    }
}
