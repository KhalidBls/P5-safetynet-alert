package com.safetynet.alerts.services;

import com.safetynet.alerts.models.Medicalrecord;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class MedicalrecordServiceTest {

    @Mock
    EntitiesRepository repo;

    @InjectMocks
    MedicalrecordService medicalrecordService;

    @Test
    public void getMedicalrecordShouldReturnAllMedicalrecords(){
        //GIVEN
        List<Medicalrecord> ourListOfMedicalrecord = new ArrayList<>();

        List<String> medications1 = new ArrayList<>();
        medications1.add("dodoli");
        medications1.add("dodol");
        List<String> medications2 = new ArrayList<>();
        medications2.add("dodoli500");
        medications2.add("dodol1000");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("lait");
        List<String> allergies2= new ArrayList<>();
        allergies2.add("chat");
        allergies2.add("blabla");


        Medicalrecord medicalrecord1 = new Medicalrecord("Louis","Funes","01/10/1997",medications1,allergies1);
        Medicalrecord medicalrecord2 = new Medicalrecord("Franck","Francky","14/02/1966",medications2,allergies2);

        ourListOfMedicalrecord.add(medicalrecord1);
        ourListOfMedicalrecord.add(medicalrecord2);

        when(repo.getMedicalrecords()).thenReturn(ourListOfMedicalrecord);

        //WHEN
        List<Medicalrecord> result = medicalrecordService.getMedicalrecords();

        //THEN
        assertTrue(result.size()==2);
        assertTrue(result.get(0).getFirstName().equals("Louis"));
        assertTrue(result.get(0).getLastName().equals("Funes"));
        assertTrue(result.get(1).getFirstName().equals("Franck"));
        assertTrue(result.get(1).getLastName().equals("Francky"));
    }

    @Test
    public void testFindMedicalrecordByName(){
        //GIVEN
        List<Medicalrecord> ourListOfMedicalrecord = new ArrayList<>();

        List<String> medications1 = new ArrayList<>();
        medications1.add("dodoli");
        medications1.add("dodol");
        List<String> medications2 = new ArrayList<>();
        medications2.add("dodoli500");
        medications2.add("dodol1000");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("lait");
        List<String> allergies2= new ArrayList<>();
        allergies2.add("chat");
        allergies2.add("blabla");


        Medicalrecord medicalrecord1 = new Medicalrecord("Louis","Funes","01/10/1997",medications1,allergies1);
        Medicalrecord medicalrecord2 = new Medicalrecord("Franck","Francky","14/02/1966",medications2,allergies2);

        ourListOfMedicalrecord.add(medicalrecord1);
        ourListOfMedicalrecord.add(medicalrecord2);

        when(repo.findMedicalrecordByName("Louis","Funes")).thenReturn(medicalrecord1);

        //WHEN
        Medicalrecord result = medicalrecordService.findMedicalrecordByName("Louis","Funes");

        //THEN
        assertTrue(result.getMedications().get(0).equals("dodoli"));
        assertTrue(result.getMedications().get(1).equals("dodol"));
        assertTrue(result.getAllergies().get(0).equals("lait"));
    }

    @Test
    public void testSaveMedicalRecordShouldReturnMedicalRecord(){
        //GIVEN
        List<Medicalrecord> ourListOfMedicalrecord = new ArrayList<>();

        List<String> medications1 = new ArrayList<>();
        medications1.add("dodoli");
        medications1.add("dodol");
        List<String> medications2 = new ArrayList<>();
        medications2.add("dodoli500");
        medications2.add("dodol1000");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("lait");
        List<String> allergies2= new ArrayList<>();
        allergies2.add("chat");
        allergies2.add("blabla");


        Medicalrecord medicalrecord1 = new Medicalrecord("Louis","Funes","01/10/1997",medications1,allergies1);
        Medicalrecord medicalrecord2 = new Medicalrecord("Franck","Francky","14/02/1966",medications2,allergies2);

        ourListOfMedicalrecord.add(medicalrecord1);
        ourListOfMedicalrecord.add(medicalrecord2);

        when(repo.getMedicalrecords()).thenReturn(ourListOfMedicalrecord);

        Medicalrecord medicalrecordToSave = new Medicalrecord("Bill","Billy","14/02/1993",medications2,allergies1);

        //WHEN
        Medicalrecord result = medicalrecordService.save(medicalrecordToSave);

        //THEN
        assertTrue(result.getMedications().get(0).equals("dodoli500"));
        assertTrue(result.getMedications().get(1).equals("dodol1000"));
        assertTrue(result.getAllergies().get(0).equals("lait"));
    }

    @Test
    public void testDeleteMedicalRecord(){
        //GIVEN
        List<Medicalrecord> ourListOfMedicalrecord = new ArrayList<>();

        List<String> medications1 = new ArrayList<>();
        medications1.add("dodoli");
        medications1.add("dodol");
        List<String> medications2 = new ArrayList<>();
        medications2.add("dodoli500");
        medications2.add("dodol1000");

        List<String> allergies1 = new ArrayList<>();
        allergies1.add("lait");
        List<String> allergies2= new ArrayList<>();
        allergies2.add("chat");
        allergies2.add("blabla");


        Medicalrecord medicalrecord1 = new Medicalrecord("Louis","Funes","01/10/1997",medications1,allergies1);
        Medicalrecord medicalrecord2 = new Medicalrecord("Franck","Francky","14/02/1966",medications2,allergies2);

        ourListOfMedicalrecord.add(medicalrecord1);
        ourListOfMedicalrecord.add(medicalrecord2);

        when(repo.getMedicalrecords()).thenReturn(ourListOfMedicalrecord);
        assertTrue(ourListOfMedicalrecord.size() == 2);

        //WHEN
        medicalrecordService.deleteByName("Louis","Funes");

        //THEN
        assertTrue(ourListOfMedicalrecord.size() == 1);
    }

}
