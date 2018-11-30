package edu.ncsu.csc.itrust2.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import edu.ncsu.csc.itrust2.forms.admin.ICDCodeForm;
import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.Specialty;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;
import edu.ncsu.csc.itrust2.models.persistent.Drug;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.ICDCode;
import edu.ncsu.csc.itrust2.models.persistent.LOINC;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Personnel;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Newly revamped Test Data Generator. This class is used to generate database
 * records for the various different types of persistent objects that exist in
 * the system. Takes advantage of Hibernate persistence. To use, instantiate the
 * type of object in question, set all of its parameters, and then call the
 * save() method on the object.
 *
 * @author Kai Presler-Marshall
 *
 */
public class HibernateDataGenerator {

    /**
     * Starts the data generator program.
     *
     * @param args
     *            command line arguments
     * @throws ParseException
     * @throws NumberFormatException
     */
    public static void main ( final String args[] ) throws NumberFormatException, ParseException {
        refreshDB();

        System.exit( 0 );
        return;
    }

    /**
     * Generate sample users for the iTrust2 system.
     *
     * @throws ParseException
     * @throws NumberFormatException
     */
    public static void refreshDB () throws NumberFormatException, ParseException {
        // using the config to drop/create taken from here:
        // https://stackoverflow.com/questions/20535423/how-to-manually-invoke-create-drop-from-jpa-on-hibernate
        // how to actually generate the schemaexport taken from here:
        // http://www.javarticles.com/2015/06/generating-database-schema-using-hibernate.html

        final StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder();
        ssrb.configure( "/hibernate.cfg.xml" );
        ssrb.applySetting( "hibernate.connection.url", DBUtil.getUrl() );
        ssrb.applySetting( "hibernate.connection.username", DBUtil.getUsername() );
        ssrb.applySetting( "hibernate.connection.password", DBUtil.getPassword() );
        final SchemaExport export = new SchemaExport(
                (MetadataImplementor) new MetadataSources( ssrb.build() ).buildMetadata() );
        export.drop( true, true );
        export.create( true, true );

        generateUsers();
        generateTestFaculties();
        generateTestICD();
    }

    /**
     * Generate sample users for the iTrust2 system.
     */
    public static void generateUsers () {
        final User hcp = new User( "hcp", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_HCP,
                true );
        hcp.save();

        final Personnel p = new Personnel();
        p.setSelf( hcp );
        p.setFirstName( "HCP" );
        p.setLastName( "HCP" );
        p.setEmail( "csc326.201.1@gmail.com" );
        p.setAddress1( "1234 Road St." );
        p.setCity( "town" );
        p.setState( State.AK );
        p.setZip( "12345" );
        p.setPhone( "111-222-3333" );
        p.setSpecialty( Specialty.SPECIALTY_NONE );
        p.save();

        // Create an opthalmology HCP
        final User ophhcp = new User( "ophhcp", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_HCP, true );
        ophhcp.save();

        final Personnel ophp = new Personnel();
        ophp.setSelf( ophhcp );
        ophp.setFirstName( "P" );
        ophp.setLastName( "Sherman" );
        ophp.setEmail( "csc326.201.1@gmail.com" );
        ophp.setAddress1( "42 Wallaby Way" );
        ophp.setCity( "Sydney" );
        ophp.setState( State.NC );
        ophp.setZip( "12345" );
        ophp.setPhone( "111-222-3333" );
        ophp.setSpecialty( Specialty.SPECIALTY_OPHTHALMOLOGY );
        ophp.save();

        // Create an optometry HCP
        final User opthcp = new User( "opthcp", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_HCP, true );
        opthcp.save();

        final Personnel optp = new Personnel();
        optp.setSelf( opthcp );
        optp.setFirstName( "Preston" );
        optp.setLastName( "Garvey" );
        optp.setEmail( "csc326.201.1@gmail.com" );
        optp.setAddress1( "123 TheresASettlementThatNeedsYourHelp Street" );
        optp.setCity( "Sanctuary" );
        optp.setState( State.NC );
        optp.setZip( "12345" );
        optp.setPhone( "111-222-3333" );
        optp.setSpecialty( Specialty.SPECIALTY_OPTOMETRY );
        optp.save();

        final User patient = new User( "patient", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, true );
        patient.save();

        final User admin = new User( "admin", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_ADMIN, true );
        admin.save();

        final User er = new User( "er", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_ER,
                true );
        er.save();

        final User alminister = new User( "alminister", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_ADMIN, true );
        alminister.save();

        final User jbean = new User( "jbean", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, true );
        jbean.save();

        final User nsanderson = new User( "nsanderson", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, true );
        nsanderson.save();

        final User svang = new User( "svang", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_HCP, true );
        svang.save();
        saveAsGeneralHCP( svang );

        // generate users for testing password change & reset
        for ( int i = 1; i <= 5; i++ ) {
            final User pwtestuser = new User( "pwtestuser" + i,
                    "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_HCP, true );
            pwtestuser.save();
            saveAsGeneralHCP( pwtestuser );
        }

        final User lockoutUser = new User( "lockoutUser",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_HCP, true );
        lockoutUser.save();
        saveAsGeneralHCP( lockoutUser );

        final User lockoutUser2 = new User( "lockoutUser2",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_HCP, true );
        lockoutUser2.save();
        saveAsGeneralHCP( lockoutUser2 );

        final User knightSolaire = new User( "knightSolaire",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_ER, true );
        knightSolaire.save();
        final Personnel kniSolai = new Personnel();
        kniSolai.setSelf( knightSolaire );
        kniSolai.setFirstName( "Knight" );
        kniSolai.setLastName( "Solaire" );
        kniSolai.save();

        final User labTech = new User( "labtech", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_LABTECH, true );
        labTech.save();
        final Personnel labTechPerson = new Personnel();
        labTechPerson.setSelf( labTech );
        labTechPerson.setFirstName( "Lab" );
        labTechPerson.setLastName( "Technician" );
        labTechPerson.save();

        final User larryTech = new User( "larrytech", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_LABTECH, true );
        larryTech.save();
        final Personnel larryTechPerson = new Personnel();
        larryTechPerson.setSelf( larryTech );
        larryTechPerson.setFirstName( "Larry" );
        larryTechPerson.setLastName( "Teacher" );
        larryTechPerson.save();

        final Patient billy = new Patient();
        billy.setFirstName( "Billy" );
        final User billyUser = new User( "BillyBob", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, true );
        billyUser.save();
        billy.setUsername( billyUser.getUsername() );
        billy.setLastName( "Bob" );
        final Calendar billyBirth = Calendar.getInstance();
        billyBirth.add( Calendar.YEAR, -40 ); // billy is 40 years old
        billy.setDateOfBirth( billyBirth );
        billy.save();

        final Patient jill = new Patient();
        jill.setFirstName( "Jill" );
        final User jillUser = new User( "JillBob", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_PATIENT, true );
        jillUser.save();
        jill.setUsername( jillUser.getUsername() );
        jill.setLastName( "Bob" );
        final Calendar jillBirth = Calendar.getInstance();
        jillBirth.add( Calendar.YEAR, -40 ); // jill is 40 years old
        jill.setDateOfBirth( jillBirth );
        jill.save();
    }

    /**
     * Generates the patients, hospitals, drugs, etc. needed for testing.
     *
     * @throws ParseException
     * @throws NumberFormatException
     */
    public static void generateTestFaculties () throws NumberFormatException, ParseException {
        final Patient tim = new Patient();
        final User timUser = new User( "TimTheOneYearOld",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, true );
        timUser.save();
        tim.setUsername( timUser.getUsername() );
        tim.setFirstName( "TimTheOneYearOld" );
        tim.setLastName( "Smith" );
        final Calendar timBirth = Calendar.getInstance();
        timBirth.add( Calendar.YEAR, -1 ); // tim is one year old
        tim.setDateOfBirth( timBirth );
        tim.save();

        final Patient bob = new Patient();
        bob.setFirstName( "BobTheFourYearOld" );
        final User bobUser = new User( "BobTheFourYearOld",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, true );
        bobUser.save();
        bob.setUsername( bobUser.getUsername() );
        bob.setLastName( "Smith" );
        final Calendar bobBirth = Calendar.getInstance();
        bobBirth.add( Calendar.YEAR, -4 ); // bob is four years old
        bob.setDateOfBirth( bobBirth );
        bob.save();

        final Patient alice = new Patient();
        alice.setFirstName( "AliceThirteen" );
        final User aliceUser = new User( "AliceThirteen",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, true );
        aliceUser.save();
        alice.setUsername( aliceUser.getUsername() );
        alice.setLastName( "Smith" );
        final Calendar aliceBirth = Calendar.getInstance();
        aliceBirth.add( Calendar.YEAR, -13 ); // alice is thirteen years old
        alice.setDateOfBirth( aliceBirth );
        alice.save();

        final Hospital hosp = new Hospital( "General Hospital", "123 Main St", "12345", "NC" );
        hosp.save();

        final Drug d = new Drug();
        d.setCode( "1000-0001-10" );
        d.setName( "Quetiane Fumarate" );
        d.setDescription( "atypical antipsychotic and antidepressant" );
        d.save();
    }

    /**
     * Generated LOINC codes for Lab Procedure Tests.
     */
    public static void generateTestLOINC () {
        final LOINC l = new LOINC();
        l.setCode( "806-0" );
        l.setCommonName( "manual count of white blood cells in cerebral spinal fluid specimen" );
        l.setComponent( "white blood cells" );
        l.setProperty( "manual count" );
        l.save();
    }

    /**
     * Generated ICDCodes for Ophthalmology Diagnosis
     */
    public static void generateTestICD () {

        // Ophthalmology-related diagnoses should have O as the first
        // letter of their ICD-10 code.

        // Cataracts
        final ICDCodeForm codeForm = new ICDCodeForm();
        codeForm.setCode( "O01.0000" );
        codeForm.setDescription( "Cataracts" );
        final ICDCode cataracts = new ICDCode( codeForm );
        cataracts.save();

        // Age-related macular degeneration
        codeForm.setCode( "O01.0001" );
        codeForm.setDescription( "Age-related macular degeneration" );
        final ICDCode macDegen = new ICDCode( codeForm );
        macDegen.save();

        // Amblyopia
        codeForm.setCode( "O01.0002" );
        codeForm.setDescription( "Amblyopia" );
        final ICDCode amblyopia = new ICDCode( codeForm );
        amblyopia.save();

        // Glaucoma
        codeForm.setCode( "O01.0003" );
        codeForm.setDescription( "Glaucoma" );
        final ICDCode glaucoma = new ICDCode( codeForm );
        glaucoma.save();

        // Pneumonia
        codeForm.setCode( "A01.0001" );
        codeForm.setDescription( "Pneumonia" );
        final ICDCode pneumonia = new ICDCode( codeForm );
        pneumonia.save();

        // Lung cancer
        codeForm.setCode( "C01.0001" );
        codeForm.setDescription( "Lung Cancer" );
        final ICDCode lungcancer = new ICDCode( codeForm );
        lungcancer.save();

    }

    /**
     * Generates the patients, hospitals, drugs, etc. needed for testing EHR.
     *
     * @throws ParseException
     * @throws NumberFormatException
     */
    public static void generateTestEHR () throws NumberFormatException, ParseException {

        // Used for APIEmergencyRecordFormTest
        final Patient siegward = new Patient( "onionman",
                "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", true );
        siegward.setFirstName( "SiegwardOf" );
        siegward.setLastName( "Catarina" );
        siegward.setGender( Gender.Male );
        siegward.setBloodType( BloodType.OPos );
        final Calendar siegBirth = Calendar.getInstance();
        // SiegwardOf Catarina is 30 years old
        siegBirth.add( Calendar.YEAR, -30 );
        siegward.setDateOfBirth( siegBirth );
        siegward.save();

        final Patient king1 = new Patient( "kingone", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                true );
        king1.setFirstName( "King" );
        king1.setLastName( "One" );
        king1.setGender( Gender.Male );
        king1.setBloodType( BloodType.OPos );
        final Calendar king1Birth = Calendar.getInstance();
        king1Birth.add( Calendar.YEAR, -30 ); // King One is 30 years
                                              // old
        king1.setDateOfBirth( king1Birth );
        king1.save();

        final Patient king2 = new Patient( "kingtwo", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                true );
        king2.setFirstName( "King" );
        king2.setLastName( "Two" );
        king2.setGender( Gender.Male );
        king2.setBloodType( BloodType.OPos );
        final Calendar king2Birth = Calendar.getInstance();
        king2Birth.add( Calendar.YEAR, -30 ); // King One is 30 years
                                              // old
        king2.setDateOfBirth( king2Birth );
        king2.save();

        final Patient king3 = new Patient( "kingthree", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                true );
        king3.setFirstName( "King" );
        king3.setLastName( "Three" );
        king3.setGender( Gender.Male );
        king3.setBloodType( BloodType.OPos );
        final Calendar king3Birth = Calendar.getInstance();
        king3Birth.add( Calendar.YEAR, -30 ); // King One is 30 years
                                              // old
        king3.setDateOfBirth( king3Birth );
        king3.save();

        final Patient king4 = new Patient( "kingfour", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                true );
        king4.setFirstName( "King" );
        king4.setLastName( "Four" );
        king4.setGender( Gender.Male );
        king4.setBloodType( BloodType.OPos );
        final Calendar king4Birth = Calendar.getInstance();
        king4Birth.add( Calendar.YEAR, -30 ); // King One is 30 years
                                              // old
        king4.setDateOfBirth( king4Birth );
        king4.save();

        // First Prescription for APIEmergencyRecordFormTest
        final Drug estus = new Drug();
        estus.setCode( "1111-2222-33" );
        estus.setName( "Sunny D" );
        estus.setDescription( "Estus to heal up those wounds!" );
        estus.save();

        final Prescription estusPresc = new Prescription();
        estusPresc.setDosage( 1 );
        estusPresc.setDrug( estus );
        estusPresc.setRenewals( 20 );
        Calendar startDate = Calendar.getInstance();
        startDate.add( Calendar.DAY_OF_MONTH, -30 ); // Prescribed 30 days ago
        estusPresc.setStartDate( startDate );
        Calendar endDate = Calendar.getInstance();
        endDate.add( Calendar.DAY_OF_MONTH, 60 ); // Ends in 60 days
        estusPresc.setEndDate( endDate );
        estusPresc.setPatient( siegward );
        estusPresc.save();

        // Second Prescription for APIEmergencyRecordFormTest
        final Drug purpMoss = new Drug();
        purpMoss.setCode( "3333-2222-11" );
        purpMoss.setName( "Purple Moss" );
        purpMoss.setDescription( "Medicinal purple moss clump.\n" + "Reduces poison build-up. Cures poison." );
        purpMoss.save();

        final Prescription purpMossPresc = new Prescription();
        purpMossPresc.setDosage( 1 );
        purpMossPresc.setDrug( purpMoss );
        purpMossPresc.setRenewals( 99 );
        startDate = Calendar.getInstance();
        startDate.add( Calendar.DAY_OF_MONTH, -60 ); // Prescribed 60 days ago
        purpMossPresc.setStartDate( startDate );
        endDate = Calendar.getInstance();
        endDate.add( Calendar.DAY_OF_MONTH, 30 ); // Ends in 30 days
        purpMossPresc.setEndDate( endDate );
        purpMossPresc.setPatient( siegward );
        purpMossPresc.save();

        // Set First Diagnosis Code for APIEmergencyRecordFormTest
        ICDCodeForm codeForm = new ICDCodeForm();
        codeForm.setCode( "T49" );
        codeForm.setDescription( "Poisoned by topical agents.  Probably in Blighttown" );
        final ICDCode poisoned = new ICDCode( codeForm );
        poisoned.save();

        // Create Second Diagnosis Code for APIEmergencyRecordFormTest
        codeForm = new ICDCodeForm();
        codeForm.setCode( "S34" );
        codeForm.setDescription( "Injury of lumbar and sacral spinal cord.  Probably carrying teammates." );
        final ICDCode backPain = new ICDCode( codeForm );
        backPain.save();

        // Create an office visit with two diagnoses
        final OfficeVisitForm form = new OfficeVisitForm();
        form.setDate( "4/20/2048" );
        form.setTime( "4:20 PM" );
        form.setHcp( "hcp" );
        form.setPatient( "onionman" );
        form.setNotes( "Office Visit For SiegwardOf Catarina" );
        form.setType( AppointmentType.GENERAL_CHECKUP.toString() );
        form.setHospital( "General Hospital" );
        form.setHdl( 1 );
        form.setHeight( 1f );
        form.setWeight( 1f );
        form.setLdl( 1 );
        form.setTri( 100 );
        form.setDiastolic( 1 );
        form.setSystolic( 1 );
        form.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        form.setPatientSmokingStatus( PatientSmokingStatus.NEVER );

        final List<Diagnosis> diagnoses = new ArrayList<Diagnosis>();
        final Diagnosis estusD = new Diagnosis();
        estusD.setCode( backPain );
        estusD.setNote( "This guy needs some Sunny D stat!" );
        diagnoses.add( estusD );
        final Diagnosis purpMossD = new Diagnosis();
        purpMossD.setCode( poisoned );
        purpMossD.setNote( "This guy is poisoned!  Quick, eat some purple moss!" );
        diagnoses.add( purpMossD );
        form.setDiagnoses( diagnoses );
        final OfficeVisit siegOffVisit = new OfficeVisit( form );
        siegOffVisit.save();
    }

    private static void saveAsGeneralHCP ( final User u ) {
        final Personnel p = new Personnel();
        p.setSelf( u );
        p.setSpecialty( Specialty.SPECIALTY_NONE );
        p.setEnabled( u.getEnabled() );
        p.save();
    }
}
