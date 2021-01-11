#Author: seelder, Alex Snezhko (avsnezhk), Dominic Brown (dtbrown5), Selena Chen (schen53)

Feature: Add Individual Inventory
	As an Admin
	I want to add individual Inventory to the Coffee Machine
	So that supplies are not depleted
	
Scenario Outline: Valid Inventory added
Given I am on the Update Inventory Page
Given there is <originalIngredientAmt> of <ingredient> in the CoffeeMaker
When I add <amtIngredient> of <ingredient>
Then the inventory of the CoffeeMaker is added successfully

Examples:
  | originalIngredientAmt | ingredient | amtIngredient |
  | 10                    | Coffee     | 5             |
  | 20                    | Milk       | 6             |
  | 30                    | Sugar      | 7             |
  | 40                    | Chocolate  | 8             |
	
Scenario Outline: Invalid Inventory added
Given there is <originalIngredientAmt> of <ingredient> in the CoffeeMaker
When I attempt to add <amtIngredient> of <ingredient>
Then an error occurs for the <error>
And the inventory is not updated

Examples:
  | originalIngredientAmt | ingredient | amtIngredient | error     |
  | 10                    | Coffee     | -1            | coffee    |
  | 20                    | Milk       | -1            | milk      |
  | 30                    | Sugar      | -1            | sugar     |
  | 40                    | Chocolate  | -1            | chocolate |
	