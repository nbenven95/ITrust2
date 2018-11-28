package edu.ncsu.csc.itrust2.models.enums;

/**
 * For keeping track of various types of HCP specialties that are known to the
 * system. Different specialties have different functionality.
 *
 * @author jwlong
 *
 */
public enum Specialty {

    /**
     * HCP with no specialty.
     */
    SPECIALTY_NONE,
    /**
     * HCP that specializes in optometry.
     */
    SPECIALTY_OPTOMETRY,
    /**
     * HCP that specializes in ophthalmology.
     */
    SPECIALTY_OPHTHALMOLOGY;

    /**
     * Parse a string into its equivalent specialty enumeration.
     *
     * @param specialty
     *            The string to parse.
     * @return The enumeriation.
     */
    public static Specialty parse ( final String specialty ) {
        try {
            return Specialty.valueOf( Specialty.class, specialty );
        }
        catch ( final Exception e ) {
            return Specialty.SPECIALTY_NONE;
        }
    }

}
