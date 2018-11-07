#Author kpresle, demills

# This feature defines expected behavior for UC23 - Ophthalmology Surgery
# Appointment office vists. It is derived from and shares reusable
# step definitions with DocumentOfficeVisit.feature. Some step definitions
# in DocumentOfficeVisit, like documentOVWith*(), are not parameterized.
# This feature assumes that additional parameterized step definitions
# will be defined, instead of a refactoring of DocumentOfficeVisit.feature
# and DocumentOfficeVisitStepDefs.java. These will enable scenarios to reuse
# step definitions with arbitrary values defined in an "Examples" block.

Feature: Document Ophthalmology Surgery Office Visit
	As an OPHHCP user in iTrust2
	I want to document an Ophthalmology Surgery Appointment office visit
	So that a record exists of a Patient visiting the doctor

Scenario Outline: OPHHCP documents an Ophthalmology Surgery Appointment - Valid Input
Given There is an HCP <name> with <specialty> in the database
And The required facilities exist
When I log in to iTrust2 as <name>
When I navigate to the Document Office Visit page
# Some input values that aren't relevant to new functionality (like date,
# birth date, and notes) are omitted to limit the number of parameters,
# since the Cucumber wiki recommends that "The matcher has at most two 
# value parameters" for a good step definition.
When I document an ophthalmology surgery with values: <visAcuityODOS> <sphereODOS> <cylinderODOS> <axisODOS> <diagnosis>
Then The ophthalmology surgery is documented successfully
Examples: 
| name          | specialty               | visAcuityODOS | sphereODOS | cylinderODOS | axisODOS | surgeryType |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     15/20     |   -2.00    |     3.00     |   1.00   | CATARACT    |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     15/20     |   -2.00    |     NULL     |   NULL   | LASER       | 
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     15/20     |   -2.00    |     NULL     |   NULL   | REFRACTIVE  | 

Scenario Outline: OPHHCP documents an Ophthalmology Surgery Appointment - Invalid Input
Given There is an HCP <name> with <specialty> in the database
And The required facilities exist
When I log in to iTrust2 as <name>
When I navigate to the Document Office Visit page
# Some input values that aren't relevant to new functionality (like date,
# birth date, and notes) are omitted to limit the number of parameters,
# since the Cucumber wiki recommends that "The matcher has at most two 
# value parameters" for a good step definition.
When I document an ophthalmology surgery with values: <visAcuityODOS> <sphereODOS> <cylinderODOS> <axisODOS> <diagnosis>
Then The ophthalmology surgery is not documented
Examples: 
| name          | specialty               | visAcuityODOS | sphereODOS | cylinderODOS | axisODOS | surgeryType |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     1.23      |    -1.00   |     1.00     |    1.00  |   LASER     |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     15/20     |    "abc"   |     2.00     |    2.00  |   LASER     |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     15/20     |    -2.00   |     "abc"    |    3.00  |   LASER     |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     15/20     |    -3.00   |     3.00     |   "abc"  |   LASER     |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     15/20     |    -2.00   |     NULL     |   NULL   |   INVALID   | 
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY |     15/20     |    -2.00   |     NULL     |   NULL   |   NULL      | 