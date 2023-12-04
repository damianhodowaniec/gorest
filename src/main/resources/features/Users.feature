Feature: Users

  @Smoke
  @Users
  Scenario: Verify list of Users
    Given Get list of users and verify properties

  @Crud
  @Users
  Scenario: Create new User
    When Create new user
    And Get and verify user by id
    And Update user
    And Get and verify user by id
    And Delete user
    Then User should be deleted

  @Users
  Scenario: Try to create User unauthorised
    Then Try to create new user when not authorised

  @Users
  Scenario: Try to create empty User
    Then Try to create empty user

  @Users
  Scenario: Try to get User that not exist
    Then Get user that not exist