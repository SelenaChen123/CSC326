#Authors: seelder, Alex Snezhko (avsnezhk), Dominic Brown (dtbrown5), Selena Chen (schen53)

Feature: Add Inventory
	As an Administrator
	I want to add Inventory to the Coffee Machine
	So that supplies are not depleted and consumers can still drink coffee (it makes life easier for all of us)

Scenario Outline: Valid Inventory added
Given there is <originalCoffee> coffee, <originalWater> water, and <originalChai> chai in the CoffeeMaker
When I add <amtCoffee> coffee, <amtWater> water, and <amtChai> chai
Then the inventory of the CoffeeMaker is successfully added

Examples:
  | originalCoffee | originalWater | originalChai | amtCoffee | amtWater | amtChai |
  | 0              | 0             | 0            | 0         | 5        | 3       |
  | 20             | 10            | 15           | 22        | 5        | 3       |

Scenario Outline: Invalid Inventory data input
Given there is <originalCoffee> coffee, <originalWater> water, and <originalChai> chai in the CoffeeMaker
When I attempt to add <amtCoffee> coffee, <amtWater> water, and <amtChai> chai
Then an error occurs for <error>
And the inventory of the CoffeeMaker is not updated

Examples:
  | originalCoffee | originalWater | originalChai | amtCoffee | amtWater | amtChai | error  |
  | 25             | 10            | 15           | -1        | 5        | 7       | coffee |
  | 25             | 10            | 15           | 5         | -1       | 7       | water  |
  | 25             | 10            | 15           | 5         | 5        | -1      | chai   |
