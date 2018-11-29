package edu.ncsu.csc.itrust2.controllers.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.Specialty;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.enums.Status;
import edu.ncsu.csc.itrust2.models.enums.SurgeryType;
import edu.ncsu.csc.itrust2.models.persistent.Personnel;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * This class provides GET endpoints for all of the Enums, so that they can be
 * used for creating proper DomainObjects
 *
 * @author Kai Presler-Marshall
 */
@RestController
public class APIEnumController extends APIController {

    /**
     * Gets appointment types
     *
     * @return appointment types
     */
    @GetMapping ( BASE_PATH + "/appointmenttype" )
    public List<AppointmentType> getAppointmentTypes () {
        final User current = User.getByName( LoggerUtil.currentUser() );

        // If no one is logged in, they want all appointment types.
        if ( current == null ) {
            return Arrays.asList( AppointmentType.values() );
        }

        final Role role = current.getRole();
        // Patients must be able to access all appointment types on their form
        if ( role.equals( Role.ROLE_PATIENT ) ) {
            return Arrays.asList( AppointmentType.values() );
        }

        // HCPs only need to access the types that they are associated with
        return AppointmentType.getAssociatedAppointmentTypes( Personnel.getByName( current ).getSpecialty() );
    }

    /**
     * Gets appointment statuses
     *
     * @return appointment statuses
     */
    @GetMapping ( BASE_PATH + "/appointmentstatus" )
    public List<Status> getAppointmentStatuses () {
        return Arrays.asList( Status.values() );
    }

    /**
     * Get the blood types
     *
     * @return blood types
     */
    @GetMapping ( BASE_PATH + "/bloodtype" )
    public List<Map<String, Object>> getBloodTypes () {
        return Arrays.asList( BloodType.values() ).stream().map( bt -> bt.getInfo() ).collect( Collectors.toList() );
    }

    /**
     * Get ethnicity
     *
     * @return ethnicity
     */
    @GetMapping ( BASE_PATH + "/ethnicity" )
    public List<Map<String, Object>> getEthnicity () {
        return Arrays.asList( Ethnicity.values() ).stream().map( eth -> eth.getInfo() ).collect( Collectors.toList() );
    }

    /**
     * Get genders
     *
     * @return genders
     */
    @GetMapping ( BASE_PATH + "/gender" )
    public List<Map<String, Object>> getGenders () {
        return Arrays.asList( Gender.values() ).stream().map( gen -> gen.getInfo() ).collect( Collectors.toList() );
    }

    /**
     * Get states
     *
     * @return states
     */
    @GetMapping ( BASE_PATH + "/state" )
    public List<Map<String, Object>> getStates () {
        return Arrays.asList( State.values() ).stream().map( st -> st.getInfo() ).collect( Collectors.toList() );
    }

    /**
     * Get house smoking statuses
     *
     * @return house smoking statuses
     */
    @GetMapping ( BASE_PATH + "/housesmoking" )
    public List<HouseholdSmokingStatus> getHouseSmokingStatuses () {
        final List<HouseholdSmokingStatus> ret = Arrays.asList( HouseholdSmokingStatus.values() ).subList( 1,
                HouseholdSmokingStatus.values().length );
        return ret;
    }

    /**
     * Get specialty types
     *
     * @return HCP specialties
     */
    @GetMapping ( BASE_PATH + "/specialty" )
    public List<Specialty> getSpecialties () {
        return Arrays.asList( Specialty.values() );
    }

    /**
     * Get surgery types
     *
     * @return surgery types
     */
    @GetMapping ( BASE_PATH + "/surgeryTypes" )
    public List<SurgeryType> getSurgeryTypes () {
        return Arrays.asList( SurgeryType.values() );
    }

    /**
     * Get patient smoking statuses
     *
     * @return patient smoking statuses
     */
    @GetMapping ( BASE_PATH + "/patientsmoking" )
    public List<PatientSmokingStatus> getPatientSmokingStatuses () {
        final List<PatientSmokingStatus> ret = Arrays.asList( PatientSmokingStatus.values() ).subList( 1,
                PatientSmokingStatus.values().length );
        return ret;
    }

}
