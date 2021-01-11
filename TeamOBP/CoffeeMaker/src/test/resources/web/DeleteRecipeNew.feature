#Authors: Alex Snezhko (avsnezhk), Dominic Brown (dtbrown5), Selena Chen (schen53)

Feature: Delete a recipe
	As a user
	I want to be able to delete recipes from the CoffeeMaker
	So that only the best recipes are available for our customers

Scenario Outline: Get to delete recipe
	Given there are <numRecipes> recipes in the CoffeeMaker
	When I navigate to the Delete Recipe page
	Then I see <numRecipes> recipes shown
	
Examples:
	| numRecipes |
	| 0          |
	| 1          |
	| 10         |
	
Scenario Outline: Delete a recipe
	Given there is a recipe named <recipeName> in the CoffeeMaker
	And I am on the Delete Recipe page
	When I select to delete this recipe
	Then the recipe is deleted

Examples:
	| recipeName |
	| Coffee     |
	| Beans      |

Scenario Outline: Delete all recipes
	Given there are <numRecipes> recipes in the CoffeeMaker
	And I am on the Delete Recipe page
	When I attempt to delete all recipes
	Then all recipes in the recipe book are deleted
	
Examples:
	| numRecipes |
	| 1          |
	| 10         |
	