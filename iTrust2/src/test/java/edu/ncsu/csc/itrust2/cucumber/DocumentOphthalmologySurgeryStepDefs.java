package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.fail;

import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;

/**
 * Extra stepdefs needed to implement feature for testing Ophthalmology Surgery
 * functionality
 *
 * @author Noah Benveniste
 * @author Daniel Mills
 */
public class DocumentOphthalmologySurgeryStepDefs extends CucumberTest {

    // Not sure what this is for, just copying it over from
    // DocumentOfficeVisitStepDefs
    static {
        java.util.logging.Logger.getLogger( "com.gargoylesoftware" ).setLevel( Level.OFF );
    }

    private final String baseUrl = "http://localhost:8080/iTrust2";

    /**
     * Private helper method for logging in as a user.
     *
     * @param name
     *            username of user to login as
     */
    private void logInAsOph ( final String name ) {
        // final User currentHCP = User.getByName( LoggerUtil.currentUser() );
        // if ( currentHCP.getUsername().equals( name ) ) {
        // System.out.println( "TEST" );
        // }
        attemptLogout();
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( name );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
        waitForAngular();
    }

    /**
     * Fills out the document ophthalmology surgery form and submits it
     *
     * @param patientName
     *            the name of the patient
     * @param date
     *            the date of the surgery
     * @param time
     *            the time of the surgery
     * @param amOrPm
     *            either AM or PM
     * @param visAcuityODOS
     *            visual acuity for left and right eyes
     * @param sphereODOS
     *            sphere for left and right eyes
     * @param cylinderODOS
     *            cylinder for left and right eyes
     * @param axisODOS
     *            axis for left and right eyes
     * @param surgeryType
     *            the type of surgery
     */
    @And ( "^I document an ophthalmology surgery with values: (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+)$" )
    public void documentOphSurgery ( final String patientName, final String date, final String time,
            final String amOrPm, final String visAcuityODOS, final String sphereODOS, final String cylinderODOS,
            final String axisODOS, final String surgeryType ) {

        // Select hospital
        waitForAngular();
        final WebElement hospital = driver.findElement( By.name( "hospital" ) );
        hospital.click();

        // Input date
        final WebElement dateEle = driver.findElement( By.name( "date" ) );
        dateEle.clear();
        dateEle.sendKeys( date );

        // Input time
        final WebElement timeEle = driver.findElement( By.name( "time" ) );
        timeEle.clear();
        timeEle.sendKeys( time + " " + amOrPm );

        // Enter notes
        waitForAngular();
        final WebElement notes = driver.findElement( By.name( "notes" ) );
        notes.clear();
        notes.sendKeys( "Patient appears pretty much alive" );

        // Select patient name from radio button list
        waitForAngular();
        final WebElement patientNameEle = driver
                .findElement( By.cssSelector( "input[value=\"" + patientName + "\"]" ) );
        patientNameEle.click();

        // Select correct appointment type (oph surgery) from radio button list
        waitForAngular();
        final WebElement typeEle = driver.findElement(
                By.cssSelector( "input[value=\"" + AppointmentType.OPHTHALMOLOGY_SURGERY.toString() + "\"]" ) );
        // By.cssSelector( "input[value=\"" +
        // AppointmentType.OPHTHALMOLOGY_SURGERY.toString() + "\"]" ) );
        typeEle.click();

        // Basic Eye Metrics
        waitForAngular();
        final WebElement visualAcuityODEle = driver.findElement( By.name( "visualAcuityOD" ) );
        visualAcuityODEle.clear();
        visualAcuityODEle.sendKeys( visAcuityODOS );
        final WebElement visualAcuityOSEle = driver.findElement( By.name( "visualAcuityOS" ) );
        visualAcuityOSEle.clear();
        visualAcuityOSEle.sendKeys( visAcuityODOS );
        if ( !cylinderODOS.equals( "NULL" ) ) {
            final WebElement cylinderODEle = driver.findElement( By.name( "cylinderOD" ) );
            cylinderODEle.clear();
            cylinderODEle.sendKeys( cylinderODOS );
            final WebElement cylinderOSEle = driver.findElement( By.name( "cylinderOS" ) );
            cylinderOSEle.clear();
            cylinderOSEle.sendKeys( cylinderODOS );
        }

        final WebElement sphereODEle = driver.findElement( By.name( "sphereOD" ) );
        sphereODEle.clear();
        sphereODEle.sendKeys( sphereODOS );
        final WebElement sphereOSEle = driver.findElement( By.name( "sphereOS" ) );
        sphereOSEle.clear();
        sphereOSEle.sendKeys( sphereODOS );

        if ( !axisODOS.equals( "NULL" ) ) {
            final WebElement axisODEle = driver.findElement( By.name( "axisOD" ) );
            axisODEle.clear();
            axisODEle.sendKeys( axisODOS );
            final WebElement axisOSEle = driver.findElement( By.name( "axisOS" ) );
            axisOSEle.clear();
            axisOSEle.sendKeys( axisODOS );
        }

        // Surgery type
        waitForAngular();
        if ( !surgeryType.equals( "NULL" ) ) {
            final WebElement surgeryTypeEle = driver
                    .findElement( By.cssSelector( "input[value=\"" + surgeryType + "\"]" ) );
            surgeryTypeEle.click();
        }

        // Submit
        waitForAngular();
        final WebElement submit = driver.findElement( By.name( "submit" ) );
        submit.click();

    }

    /**
     * Verifies that office visit documentation was submitted successfully
     */
    @Then ( "^The ophthalmology surgery is documented successfully$" )
    public void verifySuccessfulSubmit () {
        final WebElement successMsg = driver.findElement( By.name( "success" ) );
        if ( !successMsg.isDisplayed() && successMsg.getText().equals( "Office visit created successfully" ) ) {
            fail();
        }
    }

    /**
     * Verifies that an invalid documentation form was not submitted
     */
    @Then ( "^The ophthalmology surgery is not documented$" )
    public void verifyUnsuccessfulSubmit () {
        final WebElement errorMsg = driver.findElement( By.name( "success" ) );
        if ( !errorMsg.isDisplayed() && errorMsg.getText().equals( "Error occurred creating office visit" ) ) {
            fail();
        }
    }

    /**
     * Navigates to the edit office visit page and selects a specific office
     * visit to edit
     *
     * @param patientName
     *            the name of the patient
     * @param date
     *            the date of the office visit
     * @param time
     *            the time of the office visit
     * @param amOrPm
     *            either AM or PM
     */
    @And ( "^I navigate to the Edit Office Visit page and select the appointment for (.+) on (.+) at (.+) (.+)$" )
    public void navigateToEditOfficeVisit ( final String patientName, final String date, final String time,
            final String amOrPm ) {
        driver.get( "http://localhost:8080/iTrust2/hcp/editOfficeVisit" );
    }

    /**
     * Creates an office visit matching the given values, with some values
     * hard-coded in.
     *
     * @param hcpName
     *            healthcare provider's username
     * @param date
     *            date as MM/DD/YYYY
     * @param time
     *            time as MM:HH
     * @param amOrPm
     *            either AM or PM
     * @param patientName
     *            patient's username
     */
    @And ( "^An office visit with (.+) on (.+) at (.+) (.+) exists for (.+) in the database with empty notes$" )
    public void checkIfOfficeExists ( final String hcpName, final String date, final String time, final String amOrPm,
            final String patientName ) {
        logInAsOph( hcpName );
        waitForAngular();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('documentOfficeVisit').click();" );
        waitForAngular();
        documentOphSurgery( patientName, date, time, amOrPm, "15/20", "2.00", "3.00", "3.00", "LASER" );

    }

    /**
     * Updates the selected office visit with notes
     */
    @And ( "^I add notes for the office visit and submit the update form$" )
    public void addNotesAndSubmitUpdate () {
        final WebElement notes = driver.findElement( By.name( "notes" ) );
        notes.clear();
        notes.sendKeys( "Patient appears pretty much alive" );

        waitForAngular();
        final WebElement submit = driver.findElement( By.name( "submit" ) );
        submit.click();
    }

    /**
     * Confirms that the office visit was edited and submitted successfully
     */
    @Then ( "^A message confirms the surgery visit was updated$" )
    public void confirmSuccessfulOfficeVisitUpdate () {
        final WebElement errorMsg = driver.findElement( By.name( "success" ) );
        if ( !errorMsg.isDisplayed() && !errorMsg.getText().equals( "Office visit edited successfully" ) ) {
            fail();
        }
    }
}
