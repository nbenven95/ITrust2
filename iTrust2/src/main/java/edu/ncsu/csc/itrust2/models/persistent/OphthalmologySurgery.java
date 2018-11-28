package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.models.enums.SurgeryType;

/**
 * Maintains information about an ophtalmology surgery.
 *
 * @author jwlong
 *
 */
@Entity
public class OphthalmologySurgery extends OfficeVisit {

    @NotNull
    private SurgeryType     surgeryType;

    @OneToOne
    @JoinColumn ( name = "basiceyemetrics_id" )
    private BasicEyeMetrics basicEyeMetrics;

    /**
     * Creates an OphthalmologySurgery with no initialization.
     */
    public OphthalmologySurgery () {
    }

    /**
     * Creates an OphthalmologySurgery and initializes it with the office visit
     * form.
     *
     * @param ovf
     *            The office visit form.
     * @throws ParseException
     */
    public OphthalmologySurgery ( OfficeVisitForm ovf ) throws ParseException {
        setBasicEyeMetrics( new BasicEyeMetrics( ovf ) );
        setSurgeryType( SurgeryType.parse( ovf.getSurgeryType() ) );
    }

    /**
     * Gets the surgery type.
     * 
     * @return the surgeryType
     */
    public SurgeryType getSurgeryType () {
        return surgeryType;
    }

    /**
     * Sets the surgery type.
     * 
     * @param surgeryType
     *            the surgeryType to set
     */
    public void setSurgeryType ( SurgeryType surgeryType ) {
        this.surgeryType = surgeryType;
    }

    /**
     * Gets basic eye metrics.
     * 
     * @return the basicEyeMetrics
     */
    public BasicEyeMetrics getBasicEyeMetrics () {
        return basicEyeMetrics;
    }

    /**
     * Sets basic eye metrics.
     * 
     * @param basicEyeMetrics
     *            the basicEyeMetrics to set
     */
    public void setBasicEyeMetrics ( BasicEyeMetrics basicEyeMetrics ) {
        this.basicEyeMetrics = basicEyeMetrics;
    }

}
