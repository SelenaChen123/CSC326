#Feature: Prescription Defaults
#	As a patient
#	I want to add my default prescription type and pharmacy
#	So that I can save time during office visits
	
#Scenario Outline: Select Default Prescription Type
#	Given I have logged in with username: <user>
#	When I choose to view my prescriptions
#	And choose <type> as my default prescription type
#	And submit my prescription defaults
#	Then a message indicates that my prescription defaults were successfully saved
	
#Examples:
#	| user  | type    |
#	| jbean | generic |
#	| schen | brand   |
	
	
	
#Scenario Outline: Select Default Pharmacy
#	Given I have logged in with username: <user>
#	When I choose to view my prescriptions
#	And select <pharmacy> as my default pharmacy
#	And submit my prescription defaults
#	Then a message indicates that my prescription defaults were successfully saved
	
#Examples:
#	| user  | pharmacy     |
#	| jbean | VCS Pharmacy |
#	| asnez | CSV Pharmacy |
	
	
	
#Scenario Outline: Select Both Defaults
#	Given I have logged in with username: <user>
#	When I choose to view my prescriptions
#	And choose <type> as my default prescription type
#	And select <pharmacy> as my default pharmacy
#	And submit my prescription defaults
#	Then a message indicates that my prescription defaults were successfully saved
	
#Examples:
#	| user  | type    | pharmacy     |
#	| jbean | generic | VCS Pharmacy |
#	| schen | brand   | VCS Pharmacy |
#	| asnez | brand   | CSV Pharmacy |