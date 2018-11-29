package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Step definitions for Cucumber features AddUser and AddHCPWithSpecialty
 *
 * @author Daniel Mills (demills)
 */
public class AddUserStepDefs extends CucumberTest {

    private final String baseUrl      = "http://localhost:8080/iTrust2";
    private final String jenkinsUname = "jenkins" + ( new Random() ).nextInt();

    /**
     * Checks that a User with the randomly generated username "jenkinsUname"
     * doesn't exist. If the User does exist, they are deleted. Test fails if
     * the User can't be deleted.
     */
    @Given ( "The user does not already exist in the database" )
    public void noUserRandom () {
        attemptLogout();

        final List<User> users = User.getUsers();
        for ( final User user : users ) {
            if ( user.getUsername().equals( jenkinsUname ) ) {
                try {
                    user.delete();
                }
                catch ( final Exception e ) {
                    Assert.fail();
                }
            }
        }
    }

    /**
     * Checks that a User with the specified username doesn't exist. Deletes the
     * User if they do exist. Test fails if the User exists and they can't be
     * deleted.
     *
     * @param username
     *            User to check for existence
     */
    @Given ( "^The user (.+) does not exist in the database" )
    public void noUserParameterized ( final String username ) {
        attemptLogout();

        final List<User> users = User.getUsers();
        for ( final User user : users ) {
            if ( user.getUsername().equals( username ) ) {
                try {
                    user.delete();
                }
                catch ( final Exception e ) {
                    Assert.fail();
                }
            }
        }
    }

    /**
     * Adds a new HCP User with a specified Role and specialty.
     *
     * @param username
     *            Username for the new User
     * @param specialty
     *            Personnel associated with new User's Specialty
     */
    @Given ( "I add (.+) as an HCP with (.+)" )
    public void addUser ( final String username, final String specialty ) {
        final WebElement userField = driver.findElement( By.id( "username" ) );
        userField.clear();
        userField.sendKeys( username );

        final WebElement pwField = driver.findElement( By.id( "password" ) );
        pwField.clear();
        pwField.sendKeys( "123456" );

        final WebElement pwField2 = driver.findElement( By.id( "password2" ) );
        pwField2.clear();
        pwField2.sendKeys( "123456" );

        final Select roleField = new Select( driver.findElement( By.id( "role" ) ) );
        roleField.selectByVisibleText( "Healthcare Provider" );

        waitForAngular();
        final Select specialtyField = new Select( driver.findElement( By.id( "specialty" ) ) );
        specialtyField.selectByVisibleText( specialty );

        final WebElement enabled = driver.findElement( By.name( "enabled" ) );
        enabled.click();

        driver.findElement( By.id( "submit" ) ).click();
    }

    /**
     * Logs in as "Admin" user.
     */
    @When ( "I log in as admin" )
    public void loginAdmin () {
        driver.get( baseUrl );
        waitForAngular();
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "admin" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    /**
     * Navigate to add user page
     */
    @When ( "I navigate to the Add User page" )
    public void addUserPage () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('users').click();" );
    }

    /**
     * Fill in add user values
     */
    @When ( "I fill in the values in the Add User form" )
    public void fillFields () {
        final WebElement username = driver.findElement( By.id( "username" ) );
        username.clear();
        username.sendKeys( jenkinsUname );

        final WebElement password = driver.findElement( By.id( "password" ) );
        password.clear();
        password.sendKeys( "123456" );

        final WebElement password2 = driver.findElement( By.id( "password2" ) );
        password2.clear();
        password2.sendKeys( "123456" );

        final Select role = new Select( driver.findElement( By.id( "role" ) ) );
        role.selectByVisibleText( "Healthcare Provider" );

        final WebElement enabled = driver.findElement( By.name( "enabled" ) );
        enabled.click();

        driver.findElement( By.id( "submit" ) ).click();

    }

    /**
     * Create user
     */
    @Then ( "The user is created successfully" )
    public void createdSuccessfully () {
        assertTrue( driver.getPageSource().contains( "User added successfully" ) );
    }

    /**
     * Attempts logging in as randomly-generated "jenkinsUname".
     */
    @Then ( "The new user can login" )
    public void tryLoginRandomUser () {
        waitForAngular();
        driver.findElement( By.id( "logout" ) ).click();

        waitForAngular();

        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( jenkinsUname );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        /**
         * Not an assert statement in the typical sense, but we know that we can
         * log in if we can find the "iTrust" button in the top-left after
         * attempting to do so.
         */
        try {
            waitForAngular();
            driver.findElement( By.linkText( "iTrust2" ) );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Attempts logging in as the specified user "username".
     *
     * @param username
     *            User to login as
     */
    @Then ( "^The new user (.+) can login$" )
    public void tryLoginParameterizedUser ( final String username ) {
        driver.findElement( By.id( "logout" ) ).click();

        final WebElement usernameField = driver.findElement( By.name( "username" ) );
        usernameField.clear();
        usernameField.sendKeys( username );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        /**
         * Not an assert statement in the typical sense, but we know that we can
         * log in if we can find the "iTrust" button in the top-left after
         * attempting to do so.
         */
        try {
            waitForAngular();
            driver.findElement( By.linkText( "iTrust2" ) );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Checks that once a user has logged in, the correct page was loaded with
     * all the expected tabs. Fails if a tab can't be found.
     */
    @Then ( "^The expected tabs are available to the user$" )
    public void checkForExpectedTabs () {
        try {
            driver.findElement( By.id( "viewrequests" ) );
            driver.findElement( By.id( "HCPFoodDiary" ) );
            driver.findElement( By.id( "editPatientDemographics" ) );
            driver.findElement( By.id( "documentOfficeVisit" ) );
            driver.findElement( By.id( "HCPpersonalreps" ) );
            driver.findElement( By.id( "HCPrecords" ) );
        }
        catch ( final NoSuchElementException e ) {
            Assert.fail( "Missing expected HCP page elements!" );
        }

        driver.findElement( By.id( "logout" ) ).click();
    }
}
