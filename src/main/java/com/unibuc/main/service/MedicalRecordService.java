package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.AddMedicalRecordDto;
import com.unibuc.main.dto.MedicalRecordDto;
import com.unibuc.main.dto.PartialMedicalRecordDto;
import com.unibuc.main.entity.Animal;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.entity.MedicalRecord;
import com.unibuc.main.exception.AnimalNotFoundException;
import com.unibuc.main.exception.EmployeeNotFoundException;
import com.unibuc.main.exception.MedicalRecordNotFoundException;
import com.unibuc.main.mapper.MedicalRecordMapper;
import com.unibuc.main.repository.AnimalRepository;
import com.unibuc.main.repository.EmployeeRepository;
import com.unibuc.main.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public MedicalRecordDto addMedicalRecord(AddMedicalRecordDto addedMedicalRecord) {
        MedicalRecord mr = medicalRecordMapper.mapToMedicalRecord(addedMedicalRecord);
        Optional<Animal> animal = animalRepository.findById(addedMedicalRecord.getAnimalId());
        if (animal.isEmpty()) {
            throw new AnimalNotFoundException(String.format(ProjectConstants.ANIMAL_NOT_FOUND, addedMedicalRecord.getAnimalId()));
        }
        Optional<Employee> vet = employeeRepository.findById(addedMedicalRecord.getVetId());
        if (vet.isEmpty()) {
            throw new EmployeeNotFoundException(String.format(ProjectConstants.EMPLOYEE_NOT_ID, addedMedicalRecord.getVetId()));
        }
        mr.setAnimal(animal.get());
        mr.setVet(vet.get());
        return medicalRecordMapper.mapToMedicalRecordDto(medicalRecordRepository.save(mr));
    }

    public List<MedicalRecordDto> getAllMedicalRecords() {
        return medicalRecordRepository.findAll()
                .stream().map(a -> medicalRecordMapper.mapToMedicalRecordDto(a))
                .collect(Collectors.toList());
    }

    public MedicalRecordDto getMedicalRecordById(Long id) {
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(id);
        if (medicalRecord.isEmpty()) {
            throw new MedicalRecordNotFoundException(String.format(ProjectConstants.RECORD_NOT_FOUND, id));
        }
        return medicalRecordMapper.mapToMedicalRecordDto(medicalRecord.get());
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

    public Boolean deleteMedicalRecord(Long medicalRecordId)  {
        Optional<MedicalRecord> medicalRecord = medicalRecordRepository.findById(medicalRecordId);
        if (medicalRecord.isEmpty()) {
            throw new MedicalRecordNotFoundException(String.format(ProjectConstants.RECORD_NOT_FOUND, medicalRecordId));
        }
        medicalRecordRepository.delete(medicalRecord.get());
        return true;
    }

    public Page<MedicalRecordDto> findPaginatedMedicalRecords(Pageable pageable) {
        Page<MedicalRecordDto> medicalRecordPage = medicalRecordRepository.findAll(pageable).map(medicalRecord -> medicalRecordMapper.mapToMedicalRecordDto(medicalRecord));
        return medicalRecordPage;
    }
    
}
