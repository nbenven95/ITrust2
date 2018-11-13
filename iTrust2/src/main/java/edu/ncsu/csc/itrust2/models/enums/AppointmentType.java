package edu.ncsu.csc.itrust2.models.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enum of all of the types of appointments that are recognized by the system.
 *
 * @author Kai Presler-Marshall
 *
 */
public enum AppointmentType {

    /**
     * General Checkup
     */
    GENERAL_CHECKUP ( 1, Specialty.SPECIALTY_NONE, Specialty.SPECIALTY_OPTOMETRY, Specialty.SPECIALTY_OPHTHALMOLOGY ),

    /**
     * Optometry visit
     */
    OPTOMETRY_VISIT ( 2, Specialty.SPECIALTY_OPTOMETRY, Specialty.SPECIALTY_OPHTHALMOLOGY ),

    /**
     * Ophthalmology visit
     */
    OPHTHALMOLOGY_SURGERY ( 3, Specialty.SPECIALTY_OPHTHALMOLOGY )

    ;

    /**
     * Numerical code of the AppointmentType
     */
    private int             code;

    /**
     * Roles associated with this type of appointment
     */
    private List<Specialty> associatedSpecialties;

    /**
     * Creates the AppointmentType from its code. Only HCPs of the given
     * associated specialties are allowed to access this type of appointment.
     *
     * @param code
     *            Code of the AppointmentType
     */
    private AppointmentType ( final int code, final Specialty... associatedSpecialties ) {
        this.code = code;
        this.associatedSpecialties = Arrays.asList( associatedSpecialties );
    }

    /**
     * Creates the AppointmentType from its code. Any HCP of any specialty
     * (including no specialty) has access to this type of appointment.
     *
     * @param code
     *            Code of the AppointmentType
     */
    private AppointmentType ( final int code ) {
        this( code, Specialty.values() );
    }

    /**
     * Gets the numerical code of the AppointmentType
     *
     * @return Code of the AppointmentType
     */
    public int getCode () {
        return code;
    }

    /**
     * Gets the appointment's associated specialties
     * 
     * @return The appointment's associated specialties
     */
    public List<Specialty> getAssociatedSpecialties () {
        return associatedSpecialties;
    }

    /**
     * Yields a list of appointment types associated with a given specialty. For
     * example, a general HCP is associated only with general checkups, whereas
     * an ophthamologist is associated with general checkups, optometry visits,
     * and ophtalmology surgeries
     *
     * @param specialty
     *            The specialty of the HCP
     * @return The appointment types the HCP can participate in.
     */
    public static List<AppointmentType> getAssociatedAppointmentTypes ( final Specialty specialty ) {
        return Arrays.asList( AppointmentType.values() ).stream()
                .filter( s -> s.associatedSpecialties.contains( specialty ) ).collect( Collectors.toList() );
    }

}
