package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.criterion.Criterion;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.models.enums.Role;

@Entity
@Table ( name = "BasicEyeMetrics" )
public class BasicEyeMetrics extends DomainObject<BasicEyeMetrics> {

    /**
     * ID of the AppointmentRequest
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long  id;

    private Float leftSphere;

    private Float rightSphere;

    private Float leftCylinder;

    private Float rightCylinder;

    private Float leftAxis;

    private Float rightAxis;

    /**
     * The Patient who is associated with this AppointmentRequest
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private User  patient;

    /**
     * The HCP who is associated with this AppointmentRequest
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "hcp_id", columnDefinition = "varchar(100)" )
    private User  hcp;

    /**
     * Retrieve an BasicHealthMetrics by its numerical ID.
     *
     * @param id
     *            The ID (as assigned by the DB) of the BasicHealthMetrics
     * @return The BasicHealthMetrics, if found, or null if not found.
     */
    public static BasicEyeMetrics getById ( final Long id ) {
        try {
            return getWhere( eqList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Retrieve a List of all BasicHealthMetrics from the database. Can be
     * filtered further once retrieved. Will return the BasicHealthMetrics
     * sorted by date.
     *
     * @return A List of all BasicHealthMetrics saved in the database
     */
    @SuppressWarnings ( "unchecked" )
    public static List<BasicEyeMetrics> getBasicEyeMetrics () {
        final List<BasicEyeMetrics> requests = (List<BasicEyeMetrics>) getAll( BasicEyeMetrics.class );
        requests.sort( ( x1, x2 ) -> x1.getId().compareTo( x2.getId() ) );
        return requests;
    }

    /**
     * Used so that Hibernate can construct and load objects
     */
    public BasicEyeMetrics () {
    }

    /**
     * Retrieve a List of BasicHealthMetrics that meets the given where clause.
     * Clause is expected to be valid SQL.
     *
     * @param where
     *            List of Criterion to and together and search for records by
     * @return The matching list
     */
    @SuppressWarnings ( "unchecked" )
    private static List<BasicEyeMetrics> getWhere ( final List<Criterion> where ) {
        return (List<BasicEyeMetrics>) getWhere( BasicEyeMetrics.class, where );
    }

    /**
     * Retrieves all BasicHealthMetrics for the Patient provided.
     *
     * @param patientName
     *            Name of the patient
     * @return All of their BasicHealthMetrics
     */
    public static List<BasicEyeMetrics> getBasicEyeMetricsForPatient ( final String patientName ) {
        return getWhere( eqList( "patient", User.getByNameAndRole( patientName, Role.ROLE_PATIENT ) ) );
    }

    /**
     * Retrieves all BasicHealthMetrics for the HCP provided
     *
     * @param hcpName
     *            Name of the HCP
     * @return All BasicHealthMetrics involving this HCP
     */
    public static List<BasicEyeMetrics> getBasicEyeMetricsForHCP ( final String hcpName ) {
        return getWhere( eqList( "hcp", User.getByNameAndRole( hcpName, Role.ROLE_HCP ) ) );
    }

    /**
     * Retrieves all BasicHealthMetrics for the HCP _and_ Patient provided. This
     * is the intersection of the requests -- namely, only the ones where both
     * the HCP _and_ Patient are on the request.
     *
     * @param hcpName
     *            Name of the HCP
     * @param patientName
     *            Name of the Patient
     * @return The list of matching BasicHealthMetrics
     */
    public static List<BasicEyeMetrics> getBasicEyeMetricsForHCPAndPatient ( final String hcpName,
            final String patientName ) {

        final Vector<Criterion> criteria = new Vector<Criterion>();
        criteria.add( eq( "hcp", User.getByNameAndRole( hcpName, Role.ROLE_HCP ) ) );
        criteria.add( eq( "patient", User.getByNameAndRole( patientName, Role.ROLE_PATIENT ) ) );
        return getWhere( criteria );
    }

    /**
     * Handles conversion between an OfficeVisitForm (the form with
     * user-provided data) and a BasicHealthMetrics object that contains
     * validated information These two classes are closely intertwined to handle
     * validated persistent information and text-based information that is then
     * displayed back to the user.
     *
     * @param ovf
     *            OfficeVisitForm to convert from
     * @throws ParseException
     *             Error in parsing form.
     */
    public BasicEyeMetrics ( final OfficeVisitForm ovf ) throws ParseException {
        setPatient( User.getByNameAndRole( ovf.getPatient(), Role.ROLE_PATIENT ) );
        setHcp( User.getByNameAndRole( ovf.getHcp(), Role.ROLE_HCP ) );

        setLeftSphere( ovf.getLeftSphere() );
        setRightSphere( ovf.getRightSphere() );
        setLeftCylinder( ovf.getLeftCylinder() );
        setRightCylinder( ovf.getRightCylinder() );
        setLeftAxis( ovf.getLeftAxis() );
        setRightAxis( ovf.getRightAxis() );
    }

    @Override
    public Long getId () {
        return id;
    }

    @SuppressWarnings ( "unused" )
    public void setId ( final Long id ) {
        this.id = id;
    }

    public Float getLeftSphere () {
        return leftSphere;
    }

    public void setLeftSphere ( final Float leftSphere ) {
        this.leftSphere = leftSphere;
    }

    public Float getRightSphere () {
        return rightSphere;
    }

    public void setRightSphere ( final Float rightSphere ) {
        this.rightSphere = rightSphere;
    }

    public Float getLeftCylinder () {
        return leftCylinder;
    }

    public void setLeftCylinder ( final Float leftCylinder ) {
        this.leftCylinder = leftCylinder;
    }

    public Float getRightCylinder () {
        return rightCylinder;
    }

    public void setRightCylinder ( final Float rightCylinder ) {
        this.rightCylinder = rightCylinder;
    }

    public Float getLeftAxis () {
        return leftAxis;
    }

    public void setLeftAxis ( final Float leftAxis ) {
        this.leftAxis = leftAxis;
    }

    public Float getRightAxis () {
        return rightAxis;
    }

    public void setRightAxis ( final Float rightAxis ) {
        this.rightAxis = rightAxis;
    }

    public User getPatient () {
        return patient;
    }

    public void setPatient ( final User patient ) {
        this.patient = patient;
    }

    public User getHcp () {
        return hcp;
    }

    public void setHcp ( final User hcp ) {
        this.hcp = hcp;
    }

}
