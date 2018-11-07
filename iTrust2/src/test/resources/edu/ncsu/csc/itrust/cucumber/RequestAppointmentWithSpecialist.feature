#Author Daniel Mills (demills)

# This feature is defined so that it can be extended if new 
# specialties and appointment types are added to the system.

# Currently it tests that general checkups, ophthalmology 
# appointments, and ophthalmology surgery appointments can 
# only be scheduled with HCPs authorized to do so. 

# General appointment request functionality for deleting,
# accepting, and rejecting appointment requests is defined
# in AppointmentRequest.feature.

Feature: Request Specialized Appointment As Patient 
	As a Patient
	I want to request appointments with specialized doctors
	So that I have a scheduled time to visit the doctor

Scenario Outline: Request Valid Appointment Type For HCP
Given There is an HCP <name> with <specialty> in the database
And There are no existing appointment requests in the database
When I log in as patient
When I navigate to the Request Appointment page
When I schedule an <appointmentType> request with <name> and <date>
Then The appointment is requested successfully
And The appointment on <date> can be found in the list

Examples:
| name          | specialty               | appointmentType           | date       |
| SaltmanPepper | SPECIALTY_OPTOMETRY     | OPHTHALMOLOGY_APPOINTMENT | 01/01/2019 |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY | OPHTHALMOLOGY_APPOINTMENT | 01/02/2019 |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY | OPHTHALMOLOGY_SURGERY     | 01/03/2019 |
| SaltmanPepper | SPECIALTY_OPTOMETRY     | GENERAL_CHECKUP           | 01/04/2019 |
| YuriZhivago   | SPECIALTY_OPHTHALMOLOGY | GENERAL_CHECKUP           | 01/05/2019 |

Scenario Outline: Request Invalid Appointment Type For HCP
Given There is an HCP <name> with <specialty> in the database
And There are no existing appointment requests in the database
When I log in as patient
When I navigate to the Request Appointment page
When I schedule an <appointmentType> request with <name> and <date>
Then An error message appears telling me what is wrong
Examples: 
| name          | specialty               | appointmentType           | date       |
| SaltmanPepper | SPECIALTY_OPTOMETRY     | OPHTHALMOLOGY_SURGERY     | 01/06/2019 |
| DrJenkins     | SPECIALTY_NONE          | OPHTHALMOLOGY_APPOINTMENT | 01/07/2019 |
| DrJenkins     | SPECIALTY_NONE          | OPHTHALMOLOGY_SURGERY     | 01/08/2019 |
