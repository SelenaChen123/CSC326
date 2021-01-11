#Author gchardin

Feature: Add a Pharmacist
    As an Admin
    I want to add a new pharmacist
    So that they can perform their job roles in the system

Scenario Outline: Add new pharmacist
Given The user does not already exist in the database
When I log in as admin
When I navigate to the Add User page
When I enter <name>, <password>, <confirmPassword>, and <role> in the Add User form
Then The user is created successfully
And The new user can login with <name> and <password>

    Examples: 
      | name     | password | confirmPassword | role |
      | sechen | csciTrust2 | csciTrust2 | Pharmacist HCP |
      | gchardin | csciTrust2 | csciTrust2 | Pharmacist HCP |

Scenario Outline: Invalid username
Given The user does not already exist in the database
When I log in as admin
When I navigate to the Add User page
When I enter <name>, <password>, <confirmPassword>, and <role> in the Add User form
Then The user is not created
And An error message is displayed about <problem>

    Examples: 
      | name     | password | confirmPassword | role | problem |
      | s | csciTrust2 | csciTrust2 | Pharmacist HCP | username |
      | sec | csciTrust2 | csciTrust2 | Pharmacist HCP | username |
      | morethantwentycharacters | csciTrust2 | csciTrust2 | Pharmacist HCP | username |
      
Scenario Outline: Invalid password
Given The user does not already exist in the database
When I log in as admin
When I navigate to the Add User page
When I enter <name>, <password>, <confirmPassword>, and <role> in the Add User form
Then The user is not created
And An error message is displayed about <problem>

    Examples: 
      | name     | password | confirmPassword | role | problem |
      | asnezhko | morethantwentycharacters | morethantwentycharacters | Pharmacist HCP | password |
      | asnezhko | m | m | Pharmacist HCP | password |

Scenario Outline: Passwords don't match
Given The user does not already exist in the database
When I log in as admin
When I navigate to the Add User page
When I enter <name>, <password>, <confirmPassword>, and <role> in the Add User form
Then The user is not created
And An error message is displayed about <problem>

    Examples: 
      | name     | password | confirmPassword | role | problem |
      | asnezhko | csciTrust2 | csciTrust | Pharmacist HCP | match |
      | asnezhko | csciTrust | csciDontTrust | Pharmacist HCP | match |