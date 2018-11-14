package edu.ncsu.csc.itrust2.config;

import org.springframework.stereotype.Component;

import edu.ncsu.csc.itrust2.models.persistent.Personnel;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Custom security service for iTrust2. Allows us to add additional methods that
 * can be used by Spring Security Annotations (such as PreAuthorize).
 *
 * @author jwlong
 *
 */
@Component ( "iTrust2SecurityService" )
public class CustomSecurityService {

    /**
     * Determines if the current logged in user has the specified specialty.
     *
     * @param specialty
     *            The specialty to check against.
     * @return True if the user has the specialty, false otherwise.
     */
    public boolean hasSpecialty ( final String specialty ) {
        final Personnel p = Personnel.getByName( LoggerUtil.currentUser() );

        if ( p.getSpecialty() != null ) {
            return p.getSpecialty().toString().equals( specialty );
        }

        return false;
    }

}
