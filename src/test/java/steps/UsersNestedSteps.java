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

public class UsersNestedSteps {
    private static final Logger logger = LogManager.getLogger();
    private final ContextObjectContainer contextObjectContainer;

    public UsersNestedSteps(ContextObjectContainer contextObjectContainer) {
        this.contextObjectContainer = contextObjectContainer;
    }

    @Then("Get list of posts and verify properties")
    public void get_list_of_posts() {
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .get("/posts?page=1&per_page=10");
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getStatusCode()).isEqualTo(200);
        int postCount = Integer.parseInt(response.getBody().jsonPath().getString("size()"));
        for (int i = 0; i < postCount; i++) {
            softAssertions.assertThat(RestUtils.arePropertiesInJSONPaths(response,
                    String.format("[%s].id", i),
                    String.format("[%s].user_id", i),
                    String.format("[%s].title", i),
                    String.format("[%s].body", i))).isTrue();
        }
        softAssertions.assertAll();
    }

    @Then("Get list of comments and verify properties")
    public void get_list_of_comments() {
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .get("/comments");
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getStatusCode()).isEqualTo(200);
        int commentCount = Integer.parseInt(response.getBody().jsonPath().getString("size()"));
        for (int i = 0; i < commentCount; i++) {
            softAssertions.assertThat(RestUtils.arePropertiesInJSONPaths(response,
                    String.format("[%s].id", i),
                    String.format("[%s].post_id", i),
                    String.format("[%s].name", i),
                    String.format("[%s].email", i),
                    String.format("[%s].body", i))).isTrue();
        }
        softAssertions.assertAll();
    }

    @Then("Get list of todos and verify properties")
    public void get_list_of_todos() {
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .get("/todos");
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getStatusCode()).isEqualTo(200);
        response.getBody().prettyPrint();
        int todosCount = Integer.parseInt(response.getBody().jsonPath().getString("size()"));
        for (int i = 0; i < todosCount; i++) {
            softAssertions.assertThat(RestUtils.arePropertiesInJSONPaths(response,
                    String.format("[%s].id", i),
                    String.format("[%s].user_id", i),
                    String.format("[%s].title", i),
                    String.format("[%s].due_on", i),
                    String.format("[%s].status", i))).isTrue();
        }
        softAssertions.assertAll();
    }

    @Then("Create user")
    public void create_user() {
        JSONObject restBody = new JSONObject();
        restBody.put("name", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 0, 3));
        restBody.put("email", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 1, 3));
        restBody.put("gender", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 2, 3));
        restBody.put("status", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 3, 3));
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .header("Authorization", getApiProperty("token"))
                .body(restBody.toString())
                .post("/users");
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getStatusCode()).isEqualTo(201);
        softAssertions.assertAll();
        contextObjectContainer.usersDataObject.setId(response.jsonPath().getString("id"));
    }

    @Then("User creates new post")
    public void user_creates_new_post() {
        JSONObject restBody = new JSONObject();
        restBody.put("user", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 0, 3));
        restBody.put("title", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 4, 3));
        restBody.put("body", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 5, 3));
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .header("Authorization", getApiProperty("token"))
                .body(restBody.toString())
                .post("/users/" + contextObjectContainer.usersDataObject.getId() + "/posts");
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getStatusCode()).isEqualTo(201);
        softAssertions.assertThat(response.getBody().jsonPath().getString("user_id")).isEqualTo(contextObjectContainer.usersDataObject.getId());
        softAssertions.assertAll();
        response.getBody().prettyPrint();
        contextObjectContainer.userNestedDataObject.setPostId(response.jsonPath().getString("id"));
        contextObjectContainer.userNestedDataObject.setPostTitle(response.jsonPath().getString("title"));
        contextObjectContainer.userNestedDataObject.setPostBody(response.jsonPath().getString("body"));
    }

    @Then("User creates new comment")
    public void user_creates_new_comment() {
        JSONObject restBody = new JSONObject();
        restBody.put("post", contextObjectContainer.userNestedDataObject.getPostId());
        restBody.put("name", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 0, 3));
        restBody.put("email", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 1, 3));
        restBody.put("body", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 5, 3));
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .header("Authorization", getApiProperty("token"))
                .body(restBody.toString())
                .post("/posts/" + contextObjectContainer.usersDataObject.getId() + "/comments");
        logger.info(response.getBody().prettyPrint());
        assertThat(response.getStatusCode()).isEqualTo(201);
    }

    @Then("User creates new todo")
    public void user_creates_new_todo() {
        JSONObject restBody = new JSONObject();
        restBody.put("title", getCsvData(getApiProperty("test.data.path") + "/Users.csv", 7, 3));
        restBody.put("status", "pending");
        restBody.put("due_on", "2024-02-02");
        Response response = RestAssured
                .given()
                .baseUri(getApiProperty("base.uri"))
                .headers("Content-Type", "application/json", "Accept", "application/json")
                .header("Authorization", getApiProperty("token"))
                .body(restBody.toString())
                .post("/users/" + contextObjectContainer.usersDataObject.getId() + "/todos");
        logger.info(response.getBody().prettyPrint());
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.getStatusCode()).isEqualTo(201);
        softAssertions.assertThat(response.getBody().jsonPath().getString("title")).isNotEmpty();
        softAssertions.assertThat(response.getBody().jsonPath().getString("status")).isNotEmpty();
        softAssertions.assertThat(response.getBody().jsonPath().getString("due_on")).isNotEmpty();
        softAssertions.assertAll();
        contextObjectContainer.userNestedDataObject.setTodoTitle(response.getBody().jsonPath().getString("title"));
        contextObjectContainer.userNestedDataObject.setStatus(response.getBody().jsonPath().getString("status"));
        contextObjectContainer.userNestedDataObject.setDueOn(response.getBody().jsonPath().getString("due_on"));
    }
}
