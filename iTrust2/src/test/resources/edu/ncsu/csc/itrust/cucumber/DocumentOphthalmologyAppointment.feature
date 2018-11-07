#Author kpresle, demills

# This feature defines expected behavior for UC22 - General Ophthalmology 
# Appointment office vists. It is derived from and shares reusable
# step definitions with DocumentOfficeVisit.feature. Some step definitions
# in DocumentOfficeVisit, like documentOVWith*(), are not parameterized.
# This feature assumes that additional parameterized step definitions
# will be defined, instead of a refactoring of DocumentOfficeVisit.feature
# and DocumentOfficeVisitStepDefs.java. These will enable scenarios to reuse
# step definitions with arbitrary values defined in an "Examples" block.

Feature: Document Ophthalmology Appointment Office Visit
	As an OPTHCP or OPHHCP user in iTrust2
	I want to document an Ophthalmology Appointment office visit
	So that a record exists of a Patient visiting the doctor


Scenario Outline: OPTHCP or OPHHCP documents an Ophthalmology Appointment - Valid Input
Given There is an HCP <name> with <specialty> in the database
And The required facilities exist
When I log in to iTrust2 as <name>
When I navigate to the Document Office Visit page
# Some input values that aren't relevant to new functionality (like date,
# birth date, and notes) are omitted to limit the number of parameters,
# since the Cucumber wiki recommends that "The matcher has at most two 
# value parameters" for a good step definition.
When I document an ophthalmology appointment with values: <visActyODOS> <sphereODOS> <cylndrODOS> <axisODOS> <diagnosis>
Then The office visit is documented successfully
Examples: 
| name          | specialty               | visAcuityODOS | sphereODOS | cylinderODOS | axisODOS | diagnosis |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     15/20     |    -2.00   |     3.00     |    1.00  |    NULL   | 
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     15/20     |    -2.00   |     3.00     |    1.00  |    NULL   |
| SaltmanPepper | SPECIALTY_OPTOMETRY     |     15/20     |    -2.00   |     NULL     |    NULL  | cataracts |
| SaltmanPepper | SPECIALTY_OPTOMETRY     |     15/20     |    -2.00   |     NULL     |    NULL  | glaucoma  |


Scenario Outline: OPTHCP or OPHHCP documents an Ophthalmology Appointment - Invalid Input
Given There is an HCP <name> with <specialty> in the database
And The required facilities exist
When I log in to iTrust2 as <name>
When I navigate to the Document Office Visit page
# Some input values that aren't relevant to new functionality (like date,
# birth date, and notes) are omitted to limit the number of parameters,
# since the Cucumber wiki recommends that "The matcher has at most two 
# value parameters" for a good step definition.
When I document an ophthalmology appointment with values: <visAcuityODOS> <sphereODOS> <cylinderODOS> <axisODOS> <diagnosis>
Then The ophthalmology appointment is not documented
Examples: 
| name          | specialty               | visAcuityODOS | sphereODOS | cylinderODOS | axisODOS | diagnosis |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     1.23      |    -2.00   |     3.00     |    1.00  |    NULL   |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     15/20     |    "abc"   |     3.00     |    1.00  |    NULL   |
| SaltmanPepper | SPECIALTY_OPTOMETRY     |     15/20     |    -2.00   |     2.00     |    NULL  | cataracts |
| SaltmanPepper | SPECIALTY_OPTOMETRY     |     15/20     |    -2.00   |     NULL     |    NULL  | notValid  |