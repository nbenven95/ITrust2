#Author Daniel Mills (demills)

# This feature defines expected behavior for viewing Ophthalmology 
# (Surgery) Appointments, as specified in UC22 and UC23, from 
# the patient's perspective.

Feature: View Ophthalmology (Surgery) Appointment Office Visit
	As a Patient
	I want to view documentation of past office visits
	So I can keep track of my health history

Scenario Outline: View Existing Appointment
Given There is an existing <type> of office visit documented
When I log in as patient
When I navigate to the View Office Visit page
Then I can view the <type> of office visit documented 
Examples:
| appointmentType           | 
| OPHTHALMOLOGY_APPOINTMENT | 
| OPHTHALMOLOGY_SURGERY     | 