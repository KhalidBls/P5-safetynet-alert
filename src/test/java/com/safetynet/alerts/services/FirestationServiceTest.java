package com.safetynet.alerts.services;

import com.safetynet.alerts.models.Firestation;
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
public class FirestationServiceTest {

    @Mock
    DataInitialization repo;

    @InjectMocks
    FirestationService firestationService;

    @Test
    public void testGetFirestationsShouldReturnAllFirestations(){
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","5");
        List<Firestation> ourListOfFirestations = new ArrayList<>();
        ourListOfFirestations.add(firestation1);
        ourListOfFirestations.add(firestation2);

        when(repo.getFirestations()).thenReturn(ourListOfFirestations);

        //WHEN
        List<Firestation> result = firestationService.getFirestations();

        //THEN
        assertTrue(result.size() == 2);
        assertTrue(result.get(0).getAddress().equals("avenue de Paris"));
        assertTrue(result.get(1).getAddress().equals("avenue de Marseille"));
    }

    @Test
    public void testFindAllByAddressWithGoodAddress(){
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","7");
        List<Firestation> ourList = new ArrayList<>();
        ourList.add(firestation1);
        ourList.add(firestation2);

        when(repo.getFirestations()).thenReturn(ourList);

        //WHEN
        Firestation result = firestationService.findAll("avenue de Marseille");

        //THEN
        assertTrue(result.getAddress().equals("avenue de Marseille"));
        assertTrue(result.getStation().equals("7"));
    }

    @Test
    public void testFindFirestationByNumberWithGoodNumberShouldReturnGoodFirestation(){
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","7");
        List<Firestation> ourList = new ArrayList<>();
        ourList.add(firestation1);
        ourList.add(firestation2);

        when(repo.getFirestations()).thenReturn(ourList);

        //WHEN
        Firestation result = firestationService.findByNumber("2");

        //THEN
        assertTrue(result.getAddress().equals("avenue de Paris"));
        assertTrue(result.getStation().equals("2"));
    }

    @Test
    public void testSaveFirestationShouldReturnFirestation(){
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","5");
        Firestation firestation3 = new Firestation("avenue de Brest","8");
        List<Firestation> ourListOfFirestations = new ArrayList<>();
        ourListOfFirestations.add(firestation1);
        ourListOfFirestations.add(firestation2);

        when(repo.getFirestations()).thenReturn(ourListOfFirestations);
        assertTrue(ourListOfFirestations.size()==2);

        //WHEN
        Firestation result = firestationService.save(firestation3);

        //THEN
        assertTrue(ourListOfFirestations.size()==3);
        assertTrue(result.getStation().equals("8"));
        assertTrue(result.getAddress().equals("avenue de Brest"));
    }

    @Test
    public void testDeleteFirestation(){
        //GIVEN
        Firestation firestation1 = new Firestation("avenue de Paris","2");
        Firestation firestation2 = new Firestation("avenue de Marseille","5");
        Firestation firestation3 = new Firestation("avenue de Brest","8");
        List<Firestation> ourListOfFirestations = new ArrayList<>();
        ourListOfFirestations.add(firestation1);
        ourListOfFirestations.add(firestation2);
        ourListOfFirestations.add(firestation3);

        when(repo.getFirestations()).thenReturn(ourListOfFirestations);
        assertTrue(ourListOfFirestations.size()==3);

        //WHEN
        firestationService.deleteStation("avenue de Brest","8");

        //THEN
        assertTrue(ourListOfFirestations.size()==2);
    }

}
