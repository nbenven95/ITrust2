package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

@Entity
public class GeneralOphthalmologyVisit extends OfficeVisit {
    /**
     * The set of diagnoses associated with this visits Marked transient so not
     * serialized or saved in DB If removed, serializer gets into an infinite
     * loop
     */
    @OneToMany ( mappedBy = "visit" )
    public transient List<Diagnosis> diagnoses;

    @OneToOne
    @JoinColumn ( name = "basiceyemetrics_id" )
    private BasicEyeMetrics          basicEyeMetrics;

    /**
     * Creates an GeneralCheckup from the OfficeVisitForm provided
     *
     * @param ovf
     *            OfficeVisitForm The OfficeVisitForm to create an
     *            GeneralCheckup out of
     * @throws ParseException
     *             If the date & time in the OfficeVisitForm cannot be parsed
     *             properly
     * @throws NumberFormatException
     *             If the ID cannot be parsed to a Long.
     */
    public GeneralOphthalmologyVisit ( final OfficeVisitForm ovf ) throws ParseException, NumberFormatException {
        super( ovf );
        // associate all diagnoses with this visit
        if ( ovf.getDiagnoses() != null ) {
            setDiagnoses( ovf.getDiagnoses() );
            for ( final Diagnosis d : diagnoses ) {
                d.setVisit( this );
            }
        }

        validateDiagnoses();

        setBasicEyeMetrics( new BasicEyeMetrics( ovf ) );
    }

    /** For Hibernate/Thymeleaf _must_ be an empty constructor */
    public GeneralOphthalmologyVisit () {
    }

    private void validateDiagnoses () {
        if ( diagnoses == null ) {
            return;
        }
        for ( final Diagnosis d : diagnoses ) {
            if ( d.getNote().length() > 500 ) {
                throw new IllegalArgumentException( "Dagnosis note too long (500 character max) : " + d.getNote() );
            }
            if ( d.getCode() == null ) {
                throw new IllegalArgumentException( "Diagnosis Code missing!" );
            }
        }
    }

    /**
     * Sets the list of Diagnoses associated with this visit
     *
     * @param list
     *            The List of Diagnoses
     */
    public void setDiagnoses ( final List<Diagnosis> list ) {
        diagnoses = list;
    }

    /**
     * Returns the list of diagnoses for this visit
     *
     * @return The list of diagnoses
     */
    public List<Diagnosis> getDiagnoses () {
        return diagnoses;
    }

    /**
     * Sets the basic eye metrics associated with this visit
     *
     * @return the basicEyeMetrics
     */
    public BasicEyeMetrics getBasicEyeMetrics () {
        return basicEyeMetrics;
    }

    /**
     * Returns the basic eye metrics for this visit
     *
     * @param basicEyeMetrics
     *            the basicEyeMetrics to set
     */
    public void setBasicEyeMetrics ( final BasicEyeMetrics basicEyeMetrics ) {
        this.basicEyeMetrics = basicEyeMetrics;
    }

    @Override
    public void save () {
        this.basicEyeMetrics.save();
        super.save();

        // get list of ids associated with this visit if this visit already
        // exists
        final Set<Long> previous = Diagnosis.getByVisit( id ).stream().map( Diagnosis::getId )
                .collect( Collectors.toSet() );
        if ( getDiagnoses() != null ) {
            for ( final Diagnosis d : getDiagnoses() ) {
                if ( d == null ) {
                    continue;
                }

                final boolean had = previous.remove( d.getId() );
                try {
                    if ( !had ) {
                        // new Diagnosis
                        LoggerUtil.log( TransactionType.DIAGNOSIS_CREATE, getHcp().getUsername(),
                                getPatient().getUsername(), getHcp() + " created a diagnosis for " + getPatient() );
                    }
                    else {
                        // already had - check if edited
                        final Diagnosis old = Diagnosis.getById( d.getId() );
                        if ( !old.getCode().getCode().equals( d.getCode().getCode() )
                                || !old.getNote().equals( d.getNote() ) ) {
                            // was edited:
                            LoggerUtil.log( TransactionType.DIAGNOSIS_EDIT, getHcp().getUsername(),
                                    getPatient().getUsername(), getHcp() + " edit a diagnosis for " + getPatient() );

                        }
                    }
                }
                catch ( final Exception e ) {
                    e.printStackTrace();
                }
                d.save();

            }
        }
        // delete any previous associations - they were deleted by user.
        for ( final Long oldId : previous ) {
            final Diagnosis dDie = Diagnosis.getById( oldId );
            if ( dDie != null ) {
                dDie.delete();
                try {
                    LoggerUtil.log( TransactionType.DIAGNOSIS_DELETE, getHcp().getUsername(),
                            getPatient().getUsername(),
                            getHcp().getUsername() + " deleted a diagnosis for " + getPatient().getUsername() );
                }
                catch ( final Exception e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Deletes any diagnoses associated with this office visit, then deletes the
     * visit entry
     */
    @Override
    public void delete () {
        if ( diagnoses != null ) {
            for ( final Diagnosis d : diagnoses ) {
                d.delete();
                try {
                    LoggerUtil.log( TransactionType.DIAGNOSIS_DELETE, getHcp().getUsername(),
                            getPatient().getUsername(), getHcp() + " deleted a diagnosis for " + getPatient() );
                }
                catch ( final Exception e ) {
                    e.printStackTrace();
                }
            }
        }
        super.delete();
    }

}
