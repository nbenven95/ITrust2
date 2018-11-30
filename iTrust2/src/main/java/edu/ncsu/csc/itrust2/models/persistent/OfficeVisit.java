package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.criterion.Criterion;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.Role;

/**
 * This is the validated database-persisted office visit representation
 *
 * @author Kai Presler-Marshall
 *
 */
@Entity
@Table ( name = "OfficeVisits" )
@Inheritance ( strategy = InheritanceType.JOINED )
public class OfficeVisit extends DomainObject<OfficeVisit> {

    /**
     * The patient of this office visit
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private User               patient;

    /**
     * The hcp of this office visit
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "hcp_id", columnDefinition = "varchar(100)" )
    private User               hcp;

    /**
     * The basic health metric data associated with this office visit.
     */
    @OneToOne
    @JoinColumn ( name = "basichealthmetrics_id" )
    private BasicHealthMetrics basicHealthMetrics;

    /**
     * The date of this office visit
     */
    @NotNull
    private Calendar           date;

    /**
     * The id of this office visit
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    protected Long             id;

    /**
     * The type of this office visit
     */
    @NotNull
    @Enumerated ( EnumType.STRING )
    private AppointmentType    type;

    /**
     * The hospital of this office visit
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "hospital_id", columnDefinition = "varchar(100)" )
    private Hospital           hospital;

    /**
     * The notes of this office visit
     */
    private String             notes;

    /**
     * The appointment of this office visit
     */
    @OneToOne
    @JoinColumn ( name = "appointment_id" )
    private AppointmentRequest appointment;

    /**
     * Get a specific office visit by the database ID
     *
     * @param id
     *            the database ID
     * @return the specific office visit with the desired ID
     */
    public static OfficeVisit getById ( final Long id ) {
        try {
            return getWhere( eqList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }

    }

    /**
     * Get all office visits for a specific patient
     *
     * @param patientName
     *            the name of the patient
     * @return the office visits of the queried patient
     */
    public static List<OfficeVisit> getForPatient ( final String patientName ) {
        return getWhere( eqList( "patient", User.getByNameAndRole( patientName, Role.ROLE_PATIENT ) ) );
    }

    /**
     * Get all office visits for a specific HCP
     *
     * @param hcpName
     *            the name of the HCP
     * @return the office visits of the queried HCP
     */
    public static List<OfficeVisit> getForHCP ( final String hcpName ) {
        return getWhere( eqList( "hcp", User.getByNameAndRole( hcpName, Role.ROLE_HCP ) ) );
    }

    /**
     * Get all office visits done by a specific HCP for a specific patient
     *
     * @param hcpName
     *            the name of the HCP
     * @param patientName
     *            the name of the patient
     * @return the office visits of the queried HCP, patient combo
     */
    public static List<OfficeVisit> getForHCPAndPatient ( final String hcpName, final String patientName ) {
        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "hcp", User.getByNameAndRole( hcpName, Role.ROLE_HCP ) ) );
        criteria.add( eq( "patient", User.getByNameAndRole( patientName, Role.ROLE_PATIENT ) ) );
        return getWhere( criteria );

    }

    /**
     * Get all office visits in the database
     *
     * @return all office visits in the database
     */
    @SuppressWarnings ( "unchecked" )
    public static List<OfficeVisit> getOfficeVisits () {
        final List<OfficeVisit> visits = (List<OfficeVisit>) getAll( OfficeVisit.class );
        visits.sort( ( x1, x2 ) -> x1.getDate().compareTo( x2.getDate() ) );
        return visits;
    }

    /**
     * Helper method to pass to the DomainObject class that performs a specific
     * query on the database.
     *
     * @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<OfficeVisit> Because get all just
     *                   returns a list of DomainObjects, the cast is okay.
     *
     * @param where
     *            List of Criterion to and together and search for records by
     * @return the result of the query
     */
    @SuppressWarnings ( "unchecked" )
    private static List<OfficeVisit> getWhere ( final List<Criterion> where ) {
        return (List<OfficeVisit>) getWhere( OfficeVisit.class, where );
    }

    /** For Hibernate/Thymeleaf _must_ be an empty constructor */
    public OfficeVisit () {
    }

    /**
     * Creates an OfficeVisit from the OfficeVisitForm provided
     *
     * @param ovf
     *            OfficeVisitForm The OfficeVisitForm to create an OfficeVisit
     *            out of
     * @throws ParseException
     *             If the date & time in the OfficeVisitForm cannot be parsed
     *             properly
     * @throws NumberFormatException
     *             If the ID cannot be parsed to a Long.
     */
    public OfficeVisit ( final OfficeVisitForm ovf ) throws ParseException, NumberFormatException {
        setPatient( User.getByNameAndRole( ovf.getPatient(), Role.ROLE_PATIENT ) );
        setHcp( User.getByNameAndRole( ovf.getHcp(), Role.ROLE_HCP ) );
        setNotes( ovf.getNotes() );

        if ( ovf.getId() != null ) {
            setId( Long.parseLong( ovf.getId() ) );
        }

        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy hh:mm aaa", Locale.ENGLISH );
        final Date parsedDate = sdf.parse( ovf.getDate() + " " + ovf.getTime() );
        final Calendar c = Calendar.getInstance();
        c.setTime( parsedDate );
        setDate( c );

        AppointmentType at = null;
        try {
            at = AppointmentType.valueOf( ovf.getType() );
        }
        catch ( final NullPointerException npe ) {
            at = AppointmentType.GENERAL_CHECKUP; /*
                                                   * If for some reason we don't
                                                   * have a type, default to
                                                   * general checkup
                                                   */
        }
        setType( at );

        if ( null != ovf.getPreScheduled() ) {
            final List<AppointmentRequest> requests = AppointmentRequest
                    .getAppointmentRequestsForHCPAndPatient( ovf.getHcp(), ovf.getPatient() );
            try {
                final AppointmentRequest match = requests.stream().filter( e -> e.getDate().equals( c ) )
                        .collect( Collectors.toList() )
                        .get( 0 ); /*
                                    * We should have one and only one
                                    * appointment for the provided HCP & patient
                                    * and the time specified
                                    */
                setAppointment( match );
            }
            catch ( final Exception e ) {
                throw new IllegalArgumentException( "Marked as preschedule but no match can be found" + e.toString() );
            }

        }
        setHospital( Hospital.getByName( ovf.getHospital() ) );
        setBasicHealthMetrics( new BasicHealthMetrics( ovf ) );

        final Patient p = Patient.getPatient( patient );
        if ( p == null || p.getDateOfBirth() == null ) {
            return; // we're done, patient can't be tested against
        }
        final Calendar dob = p.getDateOfBirth();
        int age = date.get( Calendar.YEAR ) - dob.get( Calendar.YEAR );
        if ( date.get( Calendar.MONTH ) < dob.get( Calendar.MONTH ) ) {
            age -= 1;
        }
        else if ( date.get( Calendar.MONTH ) == dob.get( Calendar.MONTH ) ) {
            if ( date.get( Calendar.DATE ) < dob.get( Calendar.DATE ) ) {
                age -= 1;
            }
        }

        final BasicHealthMetrics empty = new BasicHealthMetrics();
        empty.setHcp( this.hcp );
        empty.setPatient( this.patient );
        if ( this instanceof GeneralCheckup || !this.basicHealthMetrics.equals( empty ) ) {
            if ( age < 3 ) {
                validateUnder3();
            }
            else if ( age < 12 ) {
                validateUnder12();
            }
            else {
                validate12AndOver();
            }
        }
    }

    /**
     * Validates an office visit form for containing correct fields for patients
     * 12 and over. Expects the basic health metrics to already be set by the
     * OfficeVisit constructor.
     */
    private void validate12AndOver () {
        // should already be set in office visit constructor
        final BasicHealthMetrics bhm = getBasicHealthMetrics();
        if ( bhm.getDiastolic() == null || bhm.getHdl() == null || bhm.getHeight() == null
                || bhm.getHouseSmokingStatus() == null || bhm.getLdl() == null || bhm.getPatientSmokingStatus() == null
                || bhm.getSystolic() == null || bhm.getTri() == null || bhm.getWeight() == null ) {
            throw new IllegalArgumentException( "Not all necessary fields for basic health metrics were submitted." );
        }
    }

    /**
     * Validates an office visit form for containing correct fields for patients
     * 12 and under.
     */
    private void validateUnder12 () {
        // should already be set in office visit constructor
        final BasicHealthMetrics bhm = getBasicHealthMetrics();
        if ( bhm.getDiastolic() == null || bhm.getHeight() == null || bhm.getHouseSmokingStatus() == null
                || bhm.getSystolic() == null || bhm.getWeight() == null ) {
            throw new IllegalArgumentException( "Not all necessary fields for basic health metrics were submitted." );
        }
    }

    /**
     * Validates an office visit form for containing correct fields for patients
     * 3 and under.
     */
    private void validateUnder3 () {
        // should already be set in office visit constructor
        final BasicHealthMetrics bhm = getBasicHealthMetrics();
        if ( bhm.getHeight() == null || bhm.getHeadCircumference() == null || bhm.getHouseSmokingStatus() == null
                || bhm.getWeight() == null ) {
            throw new IllegalArgumentException( "Not all necessary fields for basic health metrics were submitted." );
        }
    }

    /**
     * Get the patient of this office visit
     *
     * @return the patient of this office visit
     */
    public User getPatient () {
        return patient;
    }

    /**
     * Set the patient of this office visit
     *
     * @param patient
     *            the patient to set this office visit to
     */
    public void setPatient ( final User patient ) {
        this.patient = patient;
    }

    /**
     * Get the hcp of this office visit
     *
     * @return the hcp of this office visit
     */
    public User getHcp () {
        return hcp;
    }

    /**
     * Set the hcp of this office visit
     *
     * @param hcp
     *            the hcp to set this office visit to
     */
    public void setHcp ( final User hcp ) {
        this.hcp = hcp;
    }

    /**
     * Get the date of this office visit
     *
     * @return the date of this office visit
     */
    public Calendar getDate () {
        return date;
    }

    /**
     * Set the date of this office visit
     *
     * @param date
     *            the date to set this office visit to
     */
    public void setDate ( final Calendar date ) {
        this.date = date;
    }

    /**
     * Get the id of this office visit
     *
     * @return the id of this office visit
     */
    @Override
    public Long getUsername () {
        return id;
    }

    /**
     * Set the id of this office visit
     *
     * @param id
     *            the id to set this office visit to
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Get the type of this office visit
     *
     * @return the type of this office visit
     */
    public AppointmentType getType () {
        return type;
    }

    /**
     * Set the type of this office visit
     *
     * @param type
     *            the type to set this office visit to
     */
    public void setType ( final AppointmentType type ) {
        this.type = type;
    }

    /**
     * Get the hospital of this office visit
     *
     * @return the hospital of this office visit
     */
    public Hospital getHospital () {
        return hospital;
    }

    /**
     * Set the hospital of this office visit
     *
     * @param hospital
     *            the hospital to set this office visit to
     */
    public void setHospital ( final Hospital hospital ) {
        this.hospital = hospital;
    }

    /**
     * Get the notes of this office visit
     *
     * @return the notes of this office visit
     */
    public String getNotes () {
        return notes;
    }

    /**
     * Set the notes of this office visit
     *
     * @param notes
     *            the notes to set this office visit to
     */
    public void setNotes ( final String notes ) {
        this.notes = notes;
    }

    /**
     * Get the appointment of this office visit
     *
     * @return the appointment of this office visit
     */
    public AppointmentRequest getAppointment () {
        return appointment;
    }

    /**
     * Set the appointment of this office visit
     *
     * @param appointment
     *            the appointment to set this office visit to
     */
    public void setAppointment ( final AppointmentRequest appointment ) {
        this.appointment = appointment;
    }

    /**
     * Gets the basic health metrics for this office visit.
     *
     * @return the basicHealthMetrics
     */
    public BasicHealthMetrics getBasicHealthMetrics () {
        return basicHealthMetrics;
    }

    /**
     * Sets the basic health metrics for this office visit.
     *
     * @param basicHealthMetrics
     *            the basicHealthMetrics to set
     */
    public void setBasicHealthMetrics ( final BasicHealthMetrics basicHealthMetrics ) {
        this.basicHealthMetrics = basicHealthMetrics;
    }

    /**
     * Overrides the basic domain object save in order to save basic health
     * metrics and the office visit.
     */
    @Override
    public void save () {
        this.basicHealthMetrics.save();
        super.save();
    }

    /**
     * Deletes all Office visits, and all Diagnoses and Lab Procedure (No
     * diagnoses without an office visit)
     */
    public static void deleteAll () {
        DomainObject.deleteAll( Diagnosis.class );
        DomainObject.deleteAll( LabProcedure.class );
        DomainObject.deleteAll( OfficeVisit.class );
    }

    @Override
    public boolean equals ( final Object o ) {
        if ( o instanceof OfficeVisit ) {
            final OfficeVisit v = (OfficeVisit) o;
            return id.equals( v.getUsername() );
        }
        return false;
    }

}
