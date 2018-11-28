package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.persistent.BasicEyeMetrics;
import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;
import edu.ncsu.csc.itrust2.models.persistent.GeneralOphthalmologyVisit;
import edu.ncsu.csc.itrust2.models.persistent.Hospital;
import edu.ncsu.csc.itrust2.models.persistent.ICDCode;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.User;

public class GeneralOphthalmologyVisitTest {

    @Test
    public void testOfficeVisit () throws NumberFormatException, ParseException {

        OfficeVisit.deleteAll();

        final Hospital hosp = new Hospital( "Dr. Jenkins' Insane Asylum", "123 Main St", "12345", "NC" );
        hosp.save();

        final GeneralOphthalmologyVisit visit = new GeneralOphthalmologyVisit();

        final BasicHealthMetrics bhm = new BasicHealthMetrics();

        bhm.setDiastolic( 150 );
        bhm.setHcp( User.getByName( "hcp" ) );
        bhm.setPatient( User.getByName( "AliceThirteen" ) );
        bhm.setHdl( 75 );
        bhm.setLdl( 75 );
        bhm.setHeight( 75f );
        bhm.setWeight( 130f );
        bhm.setTri( 300 );
        bhm.setSystolic( 150 );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.NONSMOKING );
        bhm.setPatientSmokingStatus( PatientSmokingStatus.NEVER );

        bhm.save();

        final BasicEyeMetrics bem = new BasicEyeMetrics();

        bem.setHcp( User.getByName( "ophhcp" ) );
        bem.setPatient( User.getByName( "AliceThirteen" ) );
        bem.setLeftAxis( 90 );
        bem.setRightAxis( 89 );
        bem.setLeftCylinder( -0.5 );
        bem.setRightCylinder( 0.5 );
        bem.setLeftSphere( -5.0 );
        bem.setRightSphere( -4.0 );

        bem.save();

        visit.setBasicHealthMetrics( bhm );
        visit.setBasicEyeMetrics( bem );
        visit.setType( AppointmentType.OPHTHALMOLOGY_VISIT );
        visit.setHospital( hosp );
        visit.setPatient( User.getByName( "AliceThirteen" ) );
        visit.setHcp( User.getByName( "ophhcp" ) );
        visit.setDate( Calendar.getInstance() );
        visit.save();

        final List<Diagnosis> diagnoses = new Vector<Diagnosis>();

        final ICDCode code = new ICDCode();
        code.setCode( "E42" );
        code.setDescription( "Actually Horrible" );

        code.save();

        final Diagnosis diagnosis = new Diagnosis();

        diagnosis.setCode( code );
        diagnosis.setNote( "This is bad" );
        diagnosis.setVisit( visit );

        diagnoses.add( diagnosis );

        visit.setDiagnoses( diagnoses );

        visit.save();

        // Test the visit's persistence
        final GeneralOphthalmologyVisit copy = (GeneralOphthalmologyVisit) OfficeVisit.getById( visit.getId() );
        assertEquals( visit.getId(), copy.getId() );
        assertEquals( visit.getAppointment(), copy.getAppointment() );
        assertEquals( visit.getBasicHealthMetrics(), copy.getBasicHealthMetrics() );
        assertEquals( visit.getHcp(), copy.getHcp() );
        assertEquals( visit.getHospital().getName(), copy.getHospital().getName() );
        assertEquals( visit.getPatient(), copy.getPatient() );
        assertEquals( visit.getBasicEyeMetrics(), copy.getBasicEyeMetrics() );

        // Test the form object
        final OfficeVisitForm form = new OfficeVisitForm( visit );
        form.setPreScheduled( null );
        assertEquals( visit.getId().toString(), form.getId() );
        assertEquals( visit.getHcp().getUsername(), form.getHcp() );
        assertEquals( visit.getHospital().getName(), form.getHospital() );
        assertEquals( visit.getPatient().getUsername(), form.getPatient() );
        assertEquals( visit.getDiagnoses(), form.getDiagnoses() );

        final OfficeVisit clone = new OfficeVisit( form );
        assertEquals( visit.getId(), clone.getId() );
        assertEquals( visit.getAppointment(), clone.getAppointment() );
        assertEquals( visit.getBasicHealthMetrics().getDiastolic(), clone.getBasicHealthMetrics().getDiastolic() );
        assertEquals( visit.getHcp(), clone.getHcp() );
        assertEquals( visit.getHospital().getName(), clone.getHospital().getName() );
        assertEquals( visit.getPatient(), clone.getPatient() );

        visit.save();

        visit.delete();

    }

}
