#Author Daniel Mills (demills)

# This feature defines expected behavior for 
# UC20, "New User Types". It tests the the 
# "Admin Manager Users" page has the new functionality
# for adding HCPs with specialties.

Feature: Add HCP Users With Specialties
  As an Admin
  I want to add new HCP users with specialties
  So that specialized HCPs can use iTrust

Scenario Outline: Add HCP user with Ophthalmology 
Given The user <username> does not exist in the database
When I log in as admin
When I navigate to the Add User page
When I add <username> as a <role> with <specialty>
Then The user is created successfully
And  The new user <username> can login
# These expected tabs are defined in expected results of the UC20 system tests.
# They should be the same for each HealthCare provider, and can be hardcoded in
# to the step defs. 
And  The expected tabs are available to the user

Examples:
  | username  |        role         | specialty 		|
  | JaneDoe   | Healthcare Provider	| Ophthalmology |
  | JohnDoe   | Healthcare Provider | Optometry     |
  | JoeSchmoe | Healthcare Provider | None          |
  