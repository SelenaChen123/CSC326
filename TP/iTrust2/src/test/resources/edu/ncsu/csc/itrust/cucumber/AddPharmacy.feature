#Author gchardin

Feature: Add a Pharmacy
    As an Admin
    I want to add a new pharmacy
    So that patients can send their prescriptions and have them filled

Scenario: Add new pharmacy
Given The desired pharmacy doesn't exist
When I log in as the admin
When I navigate to the Manage Locations page
When I fill in the values in the Add Pharmacy form
Then The Pharmacy is created successfully

Scenario: Delete a pharmacy
Given The desired pharmacy exists
When I log in as the admin
When I navigate to the Manage Locations page
When I find the pharmacy and I press the delete button
Then the pharmacy is deleted from the system

Scenario: Assign pharmacist
Given there are pharmacists and pharmacies in the system
When I log in as the admin
When I navigate to the Assign Pharmacist page
When I select the pharmacist and pharmacy
Then the pharmacist is assigned
