package edu.ncsu.csc.itrust2.models.enums;

/**
 * Keeps track of surgery types known to the system.
 *
 * @author jwlong
 *
 */
public enum SurgeryType {

    /**
     * Cataract surgery
     */
    CATARACT,
    /**
     * Laser surgery
     */
    LASER,
    /**
     * Refractive surgery
     */
    REFRACTIVE;

    /**
     * Tries to parse a string into its corresponding enum value.
     *
     * @param surgeryType
     *            The string to parse.
     * @return Its enum value.
     */
    public static SurgeryType parse ( final String surgeryType ) {
        try {
            return SurgeryType.valueOf( SurgeryType.class, surgeryType );
        }
        catch ( final Exception e ) {
            throw new IllegalArgumentException( "Invalid surgery type: " + surgeryType );
        }
    }

}
