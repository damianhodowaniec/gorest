Feature: Users Nested

  @Smoke
  @Users
  @Nested
  Scenario: Verify list of Posts
    Then Get list of posts and verify properties

  @Smoke
  @Users
  @Nested
  Scenario: Verify list of Comments
    Then Get list of comments and verify properties

  @Smoke
  @Users
  @Nested
  Scenario: Verify list of Todos
    Then Get list of todos and verify properties

  @Crud
  @Users
  @Nested
  Scenario: User create  Post and Todo
    When Create user
    Then User creates new todo
    And User creates new post
    And Delete user

