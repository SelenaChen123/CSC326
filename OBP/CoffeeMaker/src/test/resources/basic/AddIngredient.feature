#Authors: Alex Snezhko (avsnezhk), Dominic Brown (dtbrown5), Selena Chen (schen53)

Feature: Add an ingredient
	As a user
	I want to be able to add ingredients to the CoffeeMaker
	So that consumers have variety in their diet
	
Scenario Outline: Valid ingredient
	Given the CoffeeMaker already has <existingIngredients> ingredients
	When I create a valid ingredient with name: <ingredientName>, and initial amount: <ingredientAmount>
	Then the ingredient is successfully added
	
Examples:
	| existingIngredients | ingredientName | ingredientAmount |
	| 0                   | Espresso       | 10               |
	| 1                   | Special Sauce  | 12               |
	| 2                   | Milk v2.0      | 40               |
	
Scenario Outline: Invalid ingredient
	Given the CoffeeMaker already has <existingIngredients> ingredients
	When I attempt to create an ingredient with name: <ingredientName>, and initial amount: <ingredientAmount>
	Then the ingredient is not added
	
Examples:
	| existingIngredients | ingredientName | ingredientAmount |
	| 0                   | Espresso       | -1               |
	| 1                   | Special Sauce  | 1.23             |
	| 2                   | Milk v2.0      | dude what        |
