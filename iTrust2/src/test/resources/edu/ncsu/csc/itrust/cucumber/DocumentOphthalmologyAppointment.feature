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

# This scenario covers behavior described in UC22 acceptance scenarios 1, 2, and 3.
Scenario Outline: OPTHCP or OPHHCP documents an Ophthalmology Appointment - Valid Input
Given There is an HCP user with <name> and <specialty> in the database
And The required facilities exist
And A patient with <patientName> exists with no documented office visits
And I am logged in to iTrust2 as <username>
When I navigate to the Document Office Visit page
And I document an ophthalmology appointment with values: <patientName> <date> <time> <visAcuityODOS> <sphereODOS> <cylinderODOS> <axisODOS> <diagnosis> 
Then The office visit is documented successfully

# This corresponds to UC22's acc. scenario 3. It requires logging in as the 
# patient, navigating to the View Office Visit page and checking values.
And  <patientName> can view the appointment on <date> at <time> with values: <visAcuityODOS> <sphereODOS> <cylinderODOS> <axisODOS> <diagnosis>
Examples: 
| username      | specialty               | patientName   | date       | time     | visAcuityODOS  | sphereODOS | cylinderODOS | axisODOS | diagnosis | 
| SeanMurphy    | SPECIALTY_OPHTHALMOLOGY | TomRiddle     | 10/26/1993 |  6:00 AM |    15/20       |    -2.00   |     3.00     |    1.00  |    NULL   |
| SeanMurphy    | SPECIALTY_OPHTHALMOLOGY | TomRiddle     | 10/26/1993 |  7:00 AM |    15/20       |    -2.00   |     3.00     |    1.00  |    NULL   |
| MeredithGray  | SPECIALTY_OPTOMETRY     | HarryPotter   | 10/26/1993 |  8:00 PM |    15/20       |    -2.00   |     NULL     |    NULL  | cataracts |
| MeredithGray  | SPECIALTY_OPTOMETRY     | HarryPotter   | 10/26/1993 |  9:00 PM |    15/20       |    -2.00   |     NULL     |    NULL  | glaucoma  |

# This scenario doesn't correspond to an acceptance scenario, 
# but covers the behavior described in UC22.4 [E1].
Scenario Outline: OPTHCP or OPHHCP documents an Ophthalmology Appointment - Invalid Input
Given There is an HCP <name> with <specialty> in the database
And The required facilities exist
And A patient with <patientName> exists with no documented office visits
And I am logged in to iTrust2 as <name>
When I navigate to the Document Office Visit page

# Note: In the step defs, for the date field, the regex should match 
# two strings to account for the space between the time and AM/PM.
And I document an ophthalmology appointment with values: <patientName> <date> <time> <visAcuityODOS> <sphereODOS> <cylinderODOS> <axisODOS> <diagnosis>
Then The ophthalmology appointment is not documented
Examples: 
| username      | specialty               | patientName |    date    | time     | visAcuityODOS | sphereODOS | cylinderODOS | axisODOS | diagnosis |
| SeanMurphy    | SPECIALTY_OPHTHALMOLOGY | TomRiddle   | 10/26/1993 |  1:00 PM |    1.23       |    -2.00   |     3.00     |    1.00  |    NULL   |
| SeanMurphy    | SPECIALTY_OPHTHALMOLOGY | TomRiddle   | 10/26/1993 |  2:00 PM |   15/20       |    "abc"   |     3.00     |    1.00  |    NULL   |
| MeredithGray  | SPECIALTY_OPTOMETRY     | HarryPotter | 10/26/1993 |  3:00 PM |   15/20       |    -2.00   |     2.00     |    NULL  | cataracts |
| MeredithGray  | SPECIALTY_OPTOMETRY     | HarryPotter | 10/26/1993 |  4:00 PM |   15/20       |    -2.00   |     NULL     |    NULL  | notValid  |
