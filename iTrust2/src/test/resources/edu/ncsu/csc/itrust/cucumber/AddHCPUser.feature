#Author Daniel Mills (demills)

# Some of the glued step definitions are already provided in AddUserStepDefs
# but they aren't reusable. Kai hardcoded in values that should have been in 
# AddUser.feature into AddUserStepDefs.java. I've written this feature so 
# that the reusable step definitions from AddUserStepDefs.java can be used, 
# but some step defs will need to be implemented in a separate AddHCPUserStepDefs 
# file. Alternatively, AddUser.feature and AddUserStepDefs.java can be refactored, 
# but I think that would be more work than is neccessary.
#   ~ Daniel

Feature: Add HCP Users With and Without Specialties
  As an Admin
  I want to add new HCP users with and without specialties
  So that someone new can use iTrust

Scenario Outline: Add HCP user with Ophthalmology 
Given The user <username> does not exist in the database
When I log in as admin
When I navigate to the Add User page
When I add <username> as a <role> with <specialty>
Then The user is created successfully
Then The new user <username> can login
# These expected tabs are defined in expected results of the UC20 system tests.
# They should be the same for each HealthCare provider. 
And  The expected tabs are available to the user

Examples:
  | username  |        role         | specialty 		|
  | JaneDoe   | Healthcare Provider	| Ophthalmology |
  | JohnDoe   | Healthcare Provider | Optometry     |
  | JoeSchmoe | Healthcare Provider | None          |
  