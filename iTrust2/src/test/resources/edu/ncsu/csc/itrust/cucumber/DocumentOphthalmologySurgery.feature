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
	I want to document Ophthalmology Surgery Appointments 
	So that a record exists of the Patient's surgery 

# This scenario corresponds to UC23 acceptance scenarios 1 and 2.
Scenario Outline: OPHHCP documents an Ophthalmology Surgery Appointment - Valid Input
Given There is an HCP <name> with <specialty> in the database
And The required facilities exist
And I am logged in to iTrust2 as <name>
When I navigate to the Document Office Visit page
And I document an ophthalmology surgery with values: <patientName> <date> <time> <AMorPM> <visAcuityODOS> <sphereODOS> <cylinderODOS> <axisODOS> <surgeryType>
Then The ophthalmology surgery is documented successfully
Examples: 
| name       | specialty               | patientName     |    date    | time  | AMorPM | visAcuityODOS | sphereODOS | cylinderODOS | axisODOS | surgeryType |
| SeanMurphy | SPECIALTY_OPHTHALMOLOGY | AliceThirteen   | 10/31/2018 | 1:00  |   PM  |   15/20       |   -2.00    |     3.00     |   1.00   | CATARACT    |
| SeanMurphy | SPECIALTY_OPHTHALMOLOGY | AliceThirteen   | 10/31/2018 | 2:00  |   PM  |   15/20       |   -2.00    |     NULL     |   NULL   | LASER       | 
| SeanMurphy | SPECIALTY_OPHTHALMOLOGY | AliceThirteen   | 10/31/2018 | 3:00  |   PM  |   15/20       |   -2.00    |     NULL     |   NULL   | REFRACTIVE  | 


Scenario Outline: OPHHCP documents an Ophthalmology Surgery Appointment - Invalid Input
Given There is an HCP <name> with <specialty> in the database
And The required facilities exist
And I am logged in to iTrust2 as <name>
When I navigate to the Document Office Visit page
And I document an ophthalmology surgery with values: <patientName> <date> <time> <AMorPM> <visAcuityODOS> <sphereODOS> <cylinderODOS> <axisODOS> <surgeryType>
Then The ophthalmology surgery is not documented
Examples: 
| name       | specialty               | patientName     |    date    | time | AMorPM   | visAcuityODOS | sphereODOS | cylinderODOS | axisODOS | surgeryType |
| SeanMurphy | SPECIALTY_OPHTHALMOLOGY | AliceThirteen   | 10/30/2018 | 1:00 | PM       |     "abc"     |    -1.00   |     1.00     |    1.00  |   LASER     |
| SeanMurphy | SPECIALTY_OPHTHALMOLOGY | AliceThirteen   | 10/30/2018 | 2:00 | PM       |     15/20     |    "abc"   |     2.00     |    2.00  |   LASER     |
| SeanMurphy | SPECIALTY_OPHTHALMOLOGY | AliceThirteen   | 10/30/2018 | 3:00 | PM       |     15/20     |    -2.00   |     "abc"    |    3.00  |   LASER     |
| SeanMurphy | SPECIALTY_OPHTHALMOLOGY | AliceThirteen   | 10/30/2018 | 4:00 | PM       |     15/20     |    -3.00   |     3.00     |   "abc"  |   LASER     |
| SeanMurphy | SPECIALTY_OPHTHALMOLOGY | AliceThirteen   | 10/30/2018 | 5:00 | PM       |     15/20     |    -2.00   |     NULL     |   NULL   |   NULL      | 

# This scenario corresponds to UC23's acceptance scenario 2.
Scenario Outline: OPHHCP updates an existing Ophthalmology Surgery Appointment
Given There is an HCP <name> with <specialty> in the database
And The required facilities exist
And An office visit with <name> on <date> at <time> <AMorPM> exists for <patientName> in the database with empty notes
When I am logged in to iTrust2 as <name>
And I navigate to the Edit Office Visit page and select the appointment for <patientName> on <date> at <time> <AMorPM>
# Just hardcode in notes less than 500 characters. Gherkin 
# doesn't work well for parameterized sentences.
And I add notes for the office visit and submit the update form
Then A message confirms the surgery visit was updated
Examples:
| name       | specialty               | patientName     |    date    | time   | AMorPM |
| SeanMurphy | SPECIALTY_OPHTHALMOLOGY | AliceThirteen   | 10/31/2018 | 1:00   | PM     |