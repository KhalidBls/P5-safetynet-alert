package com.safetynet.alerts.services;

import com.safetynet.alerts.models.Medicalrecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalrecordService {

    @Autowired
    private DataInitialization repo;

    public List<Medicalrecord> getMedicalrecords(){
        return repo.getMedicalrecords();
    }

    public Medicalrecord findMedicalrecordByName(String firstName, String lastName) {
        for (Medicalrecord medicalrecord : repo.getMedicalrecords()) {
            if(medicalrecord.getFirstName().equals(firstName) && medicalrecord.getLastName().equals(lastName))
                return medicalrecord;
        }
        return null;
    }

    public Medicalrecord save(Medicalrecord medicalrecord) {
        repo.getMedicalrecords().add(medicalrecord);
        return medicalrecord;
    }

    public void deleteByName(String firstName, String lastName) {
        repo.getMedicalrecords().removeIf(person -> (person.getFirstName().equals(firstName))
                && (person.getLastName().equals(lastName)));
    }



}
