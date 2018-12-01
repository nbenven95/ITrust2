package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.Specialty;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.persistent.Personnel;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;

/**
 * StepDefs to implement the scenarios in the RequestAppointmentWithSpecialist
 * feature file.
 *
 * Note: remember to run HibernateDataGenerator.refreshDB() before each local
 * run of the associated cucumber feature.
 *
 * @author Noah Benveniste
 */
public class RequestAppointmentWithSpecialistStepDefs extends CucumberTest {

    /** So I don't have to keep typing out the url */
    private final String baseUrl         = "http://localhost:8080/iTrust2";
    /** Username for patient user who requests appointments */
    private final String patientUsername = "patient";
    /** Password for patient user who requests appointments */
    private final String patientPassword = "123456";

    /**
     * Refreshes the database before the scenarios for the feature run
     */
    @Given ( "^the database has been nuked$" )
    public void nukeDB () {
        // Refresh the database
        try {
            HibernateDataGenerator.refreshDB();
        }
        catch ( final NumberFormatException e ) {
            e.printStackTrace();
            fail();
        }
        catch ( final ParseException e ) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Creates an HCP with the specified name and specialty and adds them to the
     * database.
     *
     * Corresponds with "Given There is an HCP <name> with <specialty> in the
     * database"
     *
     * @param name
     *            the HCP's name
     * @param specialty
     *            the HCP's specialty,
     */
    @Given ( "^There is an HCP (.+) with (.+) in the database$" )
    public void createSpecializedHCP ( final String name, final String specialty ) {
        attemptLogout();

        // Create an HCP user (password is 123456)
        final User specialHCPUser = new User( name, "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_HCP, 1 );
        specialHCPUser.save();

        // Create the personnel object associated with the above User object
        // with the specified specialty
        final Personnel specialHCPPersonnel = new Personnel( specialHCPUser );
        specialHCPPersonnel.setFirstName( "First" );
        specialHCPPersonnel.setLastName( "Last" );
        specialHCPPersonnel.setEmail( "someEmail@gmail.com" );
        specialHCPPersonnel.setAddress1( "123 Somestreet St." );
        specialHCPPersonnel.setCity( "Somecity" );
        specialHCPPersonnel.setState( State.NC );
        specialHCPPersonnel.setZip( "12345" );
        specialHCPPersonnel.setPhone( "111-222-3333" );
        specialHCPPersonnel.setSpecialty( Specialty.parse( specialty ) );
        specialHCPPersonnel.save();

        assertNotNull( Personnel.getByName( specialHCPUser ) );
    }

    /**
     * Method that verifies that there are no existing appointment requests in
     * the DB.
     */
    @And ( "^There are no existing appointment requests in the database$" )
    public void verifyNoAppointmentRequestInDB () {
        attemptLogout();

        assertEquals( 0, AppointmentRequest.getAppointmentRequests().size() );
    }

    /**
     * Used to log in as a patient user. Based on loginPatientDiaries from
     * FoodDiaryEntryStepDefs.
     */
    @When ( "^I sign in as a patient user$" )
    public void patientLogIn () {
        attemptLogout();

        driver.get( baseUrl );
        waitForAngular();
        final WebElement username = driver.findElement( By.name( "username" ) );
        waitForAngular();
        username.clear();
        waitForAngular();
        username.sendKeys( patientUsername );
        waitForAngular();
        final WebElement password = driver.findElement( By.name( "password" ) );
        waitForAngular();
        password.clear();
        waitForAngular();
        password.sendKeys( patientPassword );
        waitForAngular();
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        waitForAngular();
        submit.click();
        waitForAngular();

        assertEquals( "iTrust2: Patient Home", driver.getTitle() );

    }

    /**
     * Used to navigate to the request appointment page as a patient. Based on
     * requestPage from AppointmentRequestStepDefs.
     */
    @When ( "^I go to the Request Appointment page$" )
    public void patientNavigateToAppointmentRequestPage () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('requestappointment').click();" );
        waitForAngular();
        assertEquals( "http://localhost:8080/iTrust2/patient/appointmentRequest/manageAppointmentRequest",
                driver.getCurrentUrl() );
    }

    /**
     * Used to schedule an appointment of a specific type with a specific HCP on
     * a given date.
     *
     * @param appointmentType
     * @param name
     * @param date
     */
    @When ( "^I schedule an (.+) request with (.+) and (.+)$" )
    public void patientScheduleAppointment ( final String appointmentType, final String name, final String date ) {
        // Hardcoded time string, used by every test.
        final String time = "12:00 pm";

        // Select appointment type
        waitForAngular();
        final Select typeDropdown = new Select( driver.findElement( By.name( "type" ) ) );
        waitForAngular();
        typeDropdown.selectByVisibleText( appointmentType );

        // Select HCP
        waitForAngular();
        final Select hcpDropdown = new Select( driver.findElement( By.name( "hcp" ) ) );
        waitForAngular();
        hcpDropdown.selectByVisibleText( name );

        // Input the date
        waitForAngular();
        final WebElement dateInput = driver.findElement( By.id( "date" ) );
        waitForAngular();
        dateInput.clear();
        waitForAngular();
        dateInput.sendKeys( date );

        // Input the time
        waitForAngular();
        final WebElement timeInput = driver.findElement( By.id( "time" ) );
        waitForAngular();
        timeInput.clear();
        waitForAngular();
        timeInput.sendKeys( time );

        // Input comments
        waitForAngular();
        final WebElement comments = driver.findElement( By.id( "comments" ) );
        waitForAngular();
        comments.clear();
        waitForAngular();
        comments.sendKeys( "Test appointment please ignore" );

        // Submit the request
        waitForAngular();
        driver.findElement( By.name( "submit" ) ).click();
    }

    /**
     * Check for confirmation that the appointment request went through
     */
    @Then ( "^I am notified that the appointment was requested successfully$" )
    public void patientVerifySuccessfulRequest () {
        waitForAngular();
        assertTrue( driver.getPageSource().contains( "Your appointment has been requested successfully" ) );

        // Delete the appointment request before the next test runs
        assertEquals( 1, AppointmentRequest.getAppointmentRequests().size() );
        AppointmentRequest.getAppointmentRequests().get( 0 ).delete();
        assertEquals( 0, AppointmentRequest.getAppointmentRequests().size() );
    }

}
