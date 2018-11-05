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
    SPECIALTY_NONE ( 0 ),
    /**
     * HCP that specializes in optometry.
     */
    SPECIALTY_OPTOMETRY ( 1 ),
    /**
     * HCP that specializes in ophthalmology.
     */
    SPECIALTY_OPHTHALMOLOGY ( 2 );

    /**
     * Numeric code of the specialty.
     */
    private int code;

    /**
     * Create a Specialty from a code.
     *
     * @param code
     *            Code of the Specialty.
     */
    private Specialty ( final int code ) {
        this.code = code;
    }

    /**
     * Gets the numeric code of the Specialty
     * 
     * @return Code of the specialty
     */
    public int getCode () {
        return this.code;
    }

}
