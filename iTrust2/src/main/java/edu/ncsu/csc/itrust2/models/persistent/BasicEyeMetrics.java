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

/**
 * Object persisted in the database that represents the BasicEyeMetrics of a
 * patient's office visit.
 *
 * @author Neil Dey
 */
@Entity
@Table ( name = "BasicEyeMetrics" )
public class BasicEyeMetrics extends DomainObject<BasicEyeMetrics> {

    /**
     * ID of the AppointmentRequest
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long    id;

    /**
     * The left eye's sphere measurement
     */
    @NotNull
    private Double  leftSphere;

    /**
     * The right eye's sphere measurement
     */
    @NotNull
    private Double  rightSphere;

    /**
     * The left eye's cylinder measurement
     */
    private Double  leftCylinder;

    /**
     * The right eye's cylinder measurement
     */
    private Double  rightCylinder;

    /**
     * The left eye's axis measurement
     */
    private Integer leftAxis;

    /**
     * The right eye's axis measurement
     */
    private Integer rightAxis;

    /**
     * The left eye's visual acuity
     */
    @NotNull
    private String  leftVisualAcuity;

    /**
     * The right eye's visual acuity
     */
    @NotNull
    private String  rightVisualAcuity;

    /**
     * The Patient who is associated with this AppointmentRequest
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private User    patient;

    /**
     * The HCP who is associated with this AppointmentRequest
     */
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "hcp_id", columnDefinition = "varchar(100)" )
    private User    hcp;

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

    /**
     * Retrieves the ID of the AppointmentRequest
     *
     * @return the id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the ID of the AppointmentRequest
     *
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets the left sphere measurement
     *
     * @return the left sphere measurement
     */
    public Double getLeftSphere () {
        return leftSphere;
    }

    /**
     * Sets the left sphere measurement
     *
     * @param leftSphere
     *            the left sphere measurement to set
     */
    public void setLeftSphere ( final Double leftSphere ) {
        this.leftSphere = leftSphere;
    }

    /**
     * Sets the right sphere measurement
     *
     * @return the right sphere measurement
     */
    public Double getRightSphere () {
        return rightSphere;
    }

    /**
     * Sets the right sphere measurement
     *
     * @param rightSphere
     *            the right sphere measurement to set
     */
    public void setRightSphere ( final Double rightSphere ) {
        this.rightSphere = rightSphere;
    }

    /**
     * Gets the left cylinder measurement
     *
     * @return the left cylinder measurement
     */
    public Double getLeftCylinder () {
        return leftCylinder;
    }

    /**
     * Sets the left cylinder measurement
     *
     * @param leftCylinder
     *            the left cylinder measurement to set
     */
    public void setLeftCylinder ( final Double leftCylinder ) {
        this.leftCylinder = leftCylinder;
    }

    /**
     * Gets the right cylinder measurement
     *
     * @return the left cylinder measurement
     */
    public Double getRightCylinder () {
        return rightCylinder;
    }

    /**
     * Sets the right cylinder measurement
     *
     * @param rightCylinder
     *            the right cylinder measurement to set
     */
    public void setRightCylinder ( final Double rightCylinder ) {
        this.rightCylinder = rightCylinder;
    }

    /**
     * Gets the left axis measurement
     *
     * @return the left axis measurement
     */
    public Integer getLeftAxis () {
        return leftAxis;
    }

    /**
     * Sets the left axis measurement
     *
     * @param leftAxis
     *            the left axis measurement to set
     */
    public void setLeftAxis ( final Integer leftAxis ) {
        this.leftAxis = leftAxis;
    }

    /**
     * Get the right axis measurement
     *
     * @return the right axis measurement
     */
    public Integer getRightAxis () {
        return rightAxis;
    }

    /**
     * Sets the right axis measurement
     *
     * @param rightAxis
     *            the rightAxis to set
     */
    public void setRightAxis ( final Integer rightAxis ) {
        this.rightAxis = rightAxis;
    }

    /**
     * Gets the left visual acuity
     *
     * @return the left visual acuity
     */
    public String getLeftVisualAcuity () {
        return leftVisualAcuity;
    }

    /**
     * Sets the left visual acuity
     *
     * @param leftVisualAcuity
     *            the leftVisualAcuity to set
     */
    public void setLeftVisualAcuity ( final String leftVisualAcuity ) {
        if ( !leftVisualAcuity.matches( "\\d\\d?/\\d\\d?" ) ) {
            throw new IllegalArgumentException( "Does not match proper visual acuity format" );
        }
        this.leftVisualAcuity = leftVisualAcuity;
    }

    /**
     * Gets the right visual acuity
     *
     * @return the right visual acuity
     */
    public String getRightVisualAcuity () {
        return rightVisualAcuity;
    }

    /**
     * Sets the right visual acuity
     *
     * @param rightVisualAcuity
     *            the right visual acuity to set
     */
    public void setRightVisualAcuity ( final String rightVisualAcuity ) {
        if ( !rightVisualAcuity.matches( "\\d\\d?/\\d\\d?" ) ) {
            throw new IllegalArgumentException( "Does not match proper visual acuity format" );
        }
        this.rightVisualAcuity = rightVisualAcuity;
    }

    /**
     * Gets the patient associated with the appointment
     *
     * @return the patient associated with the appointment
     */
    public User getPatient () {
        return patient;
    }

    /**
     * Sets the patient associated with the appointment
     *
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final User patient ) {
        this.patient = patient;
    }

    /**
     * Gets the HCP associated with the appointment
     *
     * @return the hcp associated with the appointment
     */
    public User getHcp () {
        return hcp;
    }

    /**
     * Sets the HCP associated with the appointment
     *
     * @param hcp
     *            the hcp to set
     */
    public void setHcp ( final User hcp ) {
        this.hcp = hcp;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( hcp == null ) ? 0 : hcp.hashCode() );
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( leftAxis == null ) ? 0 : leftAxis.hashCode() );
        result = prime * result + ( ( leftCylinder == null ) ? 0 : leftCylinder.hashCode() );
        result = prime * result + ( ( leftSphere == null ) ? 0 : leftSphere.hashCode() );
        result = prime * result + ( ( leftVisualAcuity == null ) ? 0 : leftVisualAcuity.hashCode() );
        result = prime * result + ( ( patient == null ) ? 0 : patient.hashCode() );
        result = prime * result + ( ( rightAxis == null ) ? 0 : rightAxis.hashCode() );
        result = prime * result + ( ( rightCylinder == null ) ? 0 : rightCylinder.hashCode() );
        result = prime * result + ( ( rightSphere == null ) ? 0 : rightSphere.hashCode() );
        result = prime * result + ( ( rightVisualAcuity == null ) ? 0 : rightVisualAcuity.hashCode() );
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final BasicEyeMetrics other = (BasicEyeMetrics) obj;
        if ( hcp == null ) {
            if ( other.hcp != null ) {
                return false;
            }
        }
        else if ( !hcp.equals( other.hcp ) ) {
            return false;
        }
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        }
        else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( leftAxis == null ) {
            if ( other.leftAxis != null ) {
                return false;
            }
        }
        else if ( !leftAxis.equals( other.leftAxis ) ) {
            return false;
        }
        if ( leftCylinder == null ) {
            if ( other.leftCylinder != null ) {
                return false;
            }
        }
        else if ( !leftCylinder.equals( other.leftCylinder ) ) {
            return false;
        }
        if ( leftSphere == null ) {
            if ( other.leftSphere != null ) {
                return false;
            }
        }
        else if ( !leftSphere.equals( other.leftSphere ) ) {
            return false;
        }
        if ( leftVisualAcuity == null ) {
            if ( other.leftVisualAcuity != null ) {
                return false;
            }
        }
        else if ( !leftVisualAcuity.equals( other.leftVisualAcuity ) ) {
            return false;
        }
        if ( patient == null ) {
            if ( other.patient != null ) {
                return false;
            }
        }
        else if ( !patient.equals( other.patient ) ) {
            return false;
        }
        if ( rightAxis == null ) {
            if ( other.rightAxis != null ) {
                return false;
            }
        }
        else if ( !rightAxis.equals( other.rightAxis ) ) {
            return false;
        }
        if ( rightCylinder == null ) {
            if ( other.rightCylinder != null ) {
                return false;
            }
        }
        else if ( !rightCylinder.equals( other.rightCylinder ) ) {
            return false;
        }
        if ( rightSphere == null ) {
            if ( other.rightSphere != null ) {
                return false;
            }
        }
        else if ( !rightSphere.equals( other.rightSphere ) ) {
            return false;
        }
        if ( rightVisualAcuity == null ) {
            if ( other.rightVisualAcuity != null ) {
                return false;
            }
        }
        else if ( !rightVisualAcuity.equals( other.rightVisualAcuity ) ) {
            return false;
        }
        return true;
    }

    @Override
    public void save () {
        if ( ( leftAxis == null || rightAxis == null ) && ( leftCylinder != null || rightCylinder != null ) ) {
            throw new IllegalArgumentException( "Axis is required when cylinder is provided." );
        }
        if ( leftAxis == null ^ rightAxis == null ) {
            throw new IllegalArgumentException( "One axis value is not present." );
        }
        if ( leftCylinder == null ^ rightCylinder == null ) {
            throw new IllegalArgumentException( "One cylinder value is not present." );
        }
        super.save();
    }
}
