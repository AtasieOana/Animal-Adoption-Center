package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.*;
import com.unibuc.main.entity.*;
import com.unibuc.main.exception.*;
import com.unibuc.main.mapper.MedicalRecordMapper;
import com.unibuc.main.mapper.VaccineMapper;
import com.unibuc.main.repository.AnimalRepository;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.repository.MedicalRecordRepository;
import com.unibuc.main.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    @Autowired
    private MedicalRecordMapper medicalRecordMapper;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public MedicalRecordDto addMedicalRecord(MedicalRecordDto addedMedicalRecord) {
        MedicalRecord mr = medicalRecordMapper.mapToMedicalRecord(addedMedicalRecord);
        Optional<Animal> animal = animalRepository.findById(addedMedicalRecord.getAnimalId());
        if (animal.isEmpty()) {
            throw new AnimalNotFoundException(String.format(ProjectConstants.ANIMAL_NOT_FOUND, addedMedicalRecord.getAnimalId()));
        }
        Optional<Employee> vet = employeeRepository.findVetByName(addedMedicalRecord.getVetFirstName(), addedMedicalRecord.getVetLastName());
        if (vet.isEmpty()) {
            throw new EmployeeNotFoundException(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, addedMedicalRecord.getVetFirstName() + ' ' + addedMedicalRecord.getVetLastName()));
        }
        mr.setAnimal(animal.get());
        mr.setVet(vet.get());
        return medicalRecordMapper.mapToMedicalRecordDto(medicalRecordRepository.save(mr));
    }

    public List<MedicalRecordDto> getAllMedicalRecordsForAnimal(Long animalId) {
        return medicalRecordRepository.findAllByAnimal_Id(animalId)
                .stream().map(a -> medicalRecordMapper.mapToMedicalRecordDto(a))
                .collect(Collectors.toList());
    }

    public MedicalRecordDto updateMedicalRecord(Long id, PartialMedicalRecordDto updatedRecord) {
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
        if (medicalRecord.isEmpty()) {
            throw new MedicalRecordNotFoundException(String.format(ProjectConstants.RECORD_NOT_FOUND, id));
        }
        MedicalRecord updatedMedicalRecord = medicalRecord.get();
        updatedMedicalRecord.setGenerationDate(updatedRecord.getGenerationDate() != null ? updatedRecord.getGenerationDate() : medicalRecord.get().getGenerationDate());
        updatedMedicalRecord.setGeneralHealthState(updatedRecord.getGeneralHealthState() != null ? updatedRecord.getGeneralHealthState() : medicalRecord.get().getGeneralHealthState());
        return medicalRecordMapper.mapToMedicalRecordDto(medicalRecordRepository.save(updatedMedicalRecord));
    }

    public String deleteMedicalRecordBeforeADate(String dateString)  {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.findAllByGenerationDateBefore(date);
        if(medicalRecordList.isEmpty()){
            return ProjectConstants.NO_RECORD_BEFORE;
        }
        medicalRecordRepository.deleteAll(medicalRecordList);
        return ProjectConstants.DELETED_RECORDS;
    }

}
