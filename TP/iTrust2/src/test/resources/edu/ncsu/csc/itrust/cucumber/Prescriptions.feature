Feature: Manage Prescriptions
	As an HCP
	I want to record prescriptions for patients
	So that I have record of past prescriptions and they can be fulfilled
	As a patient
	I want to view my past prescriptions
	So that I can make sure they match my expectations
	As an admin
	I want to manage the list of available medications
	So that all records are up to date and HCPs can prescribe the latest medications
	As a pharmacist
	I want to fill prescriptions
	So that patients can pick up their medications


Scenario Outline: View Patient Prescription Defaults
	Given I have logged in with username: <user>
	When I choose to view my prescriptions
	Then I see the user has a default type of <type> and a default pharmacy of <pharmacy>

Examples:
	| user    | type       | pharmacy   |
	| patient | Generic    | Big pharma |
	| jbeanz  | Brand Name | null       |



Scenario Outline: Change Patient Prescription Defaults
	Given I have logged in with username: <user>
	When I choose to view my prescriptions
	And I submit changes to the default type to <type> and the default Pharmacy to <pharmacy>
	Then the defaults are successfully changed

Examples:
	| user    | type       | pharmacy     |
	| patient | Brand-name | VCS Pharmacy |
	| jbean   | Generic    | VCS Pharmacy |



Scenario Outline: Add Prescription to an Office Visit
	Given I have logged in with username: <user>
	When I start documenting an office visit for user <patient> with name: <first> <last> and date of birth: <dob>
	And fill in the office visit with date: <date>, hospital: <hospital>, notes: <notes>, weight: <weight>, height: <height>, blood pressure: <pressure>, household smoking status: <hss>, patient smoking status: <pss>, hdl: <hdl>, ldl: <ldl>, and triglycerides: <triglycerides>
	And add a prescription for <prescription> of the <type> type from pharmacy <pharmacy> with a dosage of <dosage> starting on <start> and ending on <end> with <renewals> renewals and submit the office visit
	Then A message indicates the visit was submitted successfully

Examples:
	| user  | patient        | first         | last     | dob        | date       | hospital         | notes                                                                 | weight | height | pressure | hss        | pss   | hdl | ldl | triglycerides | prescription      | type       | pharmacy     | dosage | start      | end        | renewals |
	| svang | JosephStalling | Joseph        | Stalling | 04/12/1995 | 12/15/2017 | General Hospital | Joseph appears to be depressed. He also needs to eat more vegetables. | 130.4  | 73.1   | 160/80   | OUTDOOR    | NEVER | 50  | 123 | 148           | Quetiane Fumarate | Generic    | CSV Pharmacy | 100    | 12/15/2017 | 12/14/2018 | 12       |
	| svang | JillBob        | Jill          | Bob      | 05/13/2000 | 12/16/2017 | General Hospital | Jill appears to be depressed. She also needs to eat more vegetables.  | 128.0  | 64.0   | 120/80   | NONSMOKING | NEVER | 60  | 90  | 148           | Quetiane Fumarate | Brand Name | VCS Pharmacy | 100    | 12/16/2017 | 12/15/2018 | 12       |



Scenario Outline: View Prescriptions
	Given I have logged in with username: <user>
	When I choose to view my prescriptions
	Then I see a prescription for <prescription> of the <type> type from pharmacy <pharmacy> with a dosage of <dosage> starting on <start> and ending on <end> with <renewals> renewals

Examples:
	| user           | prescription      | type       | pharmacy     | dosage | start      | end        | renewals |
	| JillBob        | Quetiane Fumarate | Brand Name | VCS Pharmacy | 100    | 12/16/2017 | 12/15/2018 | 12       |
	| JosephStalling | Quetiane Fumarate | Generic    | CSV Pharmacy | 100    | 12/15/2017 | 12/14/2018 | 12       |



Scenario Outline: Add New Drug
	Given I have logged in with username: <user>
	When I choose to add a new drug
	And submit the values for NDC <ndc>, name <name>, and description <description>
	Then the drug <name> is successfully added to the system
	
Examples:
	| user  | ndc          | name         | description       |
	| admin | 1234-5678-90 | Test Product | Strong antiseptic |




Scenario Outline: Fill Prescriptions With Specified Type
	Given user <user> is a pharmacist
	And a <prescription> prescription has been requested for <patient>
	And I have logged in with username: <user>
	When I choose to fill prescriptions sent to my pharmacy
	And I choose to fill the <prescription> prescription for patient: <patient>
	And I determine that the requested type is available
	Then the prescription is successfully filled
	
Examples:
	| user           | prescription      | patient |
	| testpharmacist | Quetiane Fumarate | JillBob |




Scenario Outline: Fill Prescriptions With Other Type
	Given user <user> is a pharmacist
	And a <prescription> prescription has been requested for <patient>
	And I have logged in with username: <user>
	When I choose to fill prescriptions sent to my pharmacy
	And I choose to fill the <prescription> prescription for patient: <patient>
	And I determine that the requested type isn't available
	And I determine that the alternate type is available
	Then the prescription is successfully filled
	
Examples:
	| user           | prescription      | patient |
	| testpharmacist | Quetiane Fumarate | JillBob |




Scenario Outline: Unable to Fill Prescriptions
	Given user <user> is a pharmacist
	And a <prescription> prescription has been requested for <patient>
	And I have logged in with username: <user>
	When I choose to fill prescriptions sent to my pharmacy
	And I choose to fill the <prescription> prescription for patient: <patient>
	And I determine that the requested type isn't available
	And I determine that the alternate type isn't available
	Then the prescription is not filled
	
Examples:
	| user           | prescription      | patient |
	| testpharmacist | Quetiane Fumarate | JillBob |




Scenario Outline: Record Prescriptions
	Given user <user> is a pharmacist
	And a <prescription> prescription has been filled for <patient>
	And I have logged in with username: <user>
	When I choose to record filled prescriptions sent to my pharmacy
	And I choose to record the filled <prescription> prescription for patient: <patient>
	Then the filled prescription is successfully recorded
	
Examples:
	| user           | prescription      | patient |
	| testpharmacist | Quetiane Fumarate | JillBob |