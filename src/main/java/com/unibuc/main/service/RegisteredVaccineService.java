package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.entity.MedicalRecord;
import com.unibuc.main.entity.RegisteredVaccine;
import com.unibuc.main.entity.Vaccine;
import com.unibuc.main.exception.MedicalRecordNotFoundException;
import com.unibuc.main.exception.RegisteredVaccineNotFoundException;
import com.unibuc.main.exception.VaccineNotFoundException;
import com.unibuc.main.mapper.RegisteredVaccineMapper;
import com.unibuc.main.repository.MedicalRecordRepository;
import com.unibuc.main.repository.RegisteredVaccineRepository;
import com.unibuc.main.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegisteredVaccineService {

    @Autowired
    private RegisteredVaccineRepository registeredVaccineRepository;

    @Autowired
    private RegisteredVaccineMapper registeredVaccineMapper;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private VaccineRepository vaccineRepository;

    public List<RegisteredVaccineDto> getAllRegisteredVaccines() {
        return registeredVaccineRepository.findAll()
                .stream().map(v -> registeredVaccineMapper.mapToRegisteredVaccineDto(v))
                .collect(Collectors.toList());
    }
    public List<RegisteredVaccineDto> associateVaccinesWithMedicalRecord(List<VaccineDto> vaccineDtos, Long medicalRecordId, Date regDate) {
        java.sql.Date sqlDate = new java.sql.Date(regDate.getTime());

        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new MedicalRecordNotFoundException(String.format(ProjectConstants.RECORD_NOT_FOUND, medicalRecordId)));

        List<RegisteredVaccine> newRegisteredVaccines = vaccineDtos.stream()
                .map(vaccineDto -> {
                    Vaccine vaccine = vaccineRepository.findByVaccineName(vaccineDto.getVaccineName())
                            .orElseThrow(() -> new VaccineNotFoundException(String.format(ProjectConstants.VACCINE_NOT_FOUND, vaccineDto.getVaccineName())));
                    return RegisteredVaccine.builder()
                            .vaccine(vaccine)
                            .medicalRecord(medicalRecord)
                            .registrationDate(sqlDate)
                            .build();
                })
                .collect(Collectors.toList());
        List<RegisteredVaccine> savedRegisteredVaccines = registeredVaccineRepository.saveAll(newRegisteredVaccines);
        return savedRegisteredVaccines.stream().map(registeredVaccine -> registeredVaccineMapper.mapToRegisteredVaccineDto(registeredVaccine)).collect(Collectors.toList());
    }

    public RegisteredVaccineDto updateRegistrationDate(Long registeredVaccineId, Date newDate) {
        java.sql.Date sqlDate = new java.sql.Date(newDate.getTime());

        Optional<RegisteredVaccine> registeredVaccine = registeredVaccineRepository.findById(registeredVaccineId);
        if (registeredVaccine.isEmpty()) {
            throw new RegisteredVaccineNotFoundException(String.format(ProjectConstants.REG_VACCINE_NOT_FOUND, registeredVaccineId));
        }
        RegisteredVaccine newRegVaccine = registeredVaccine.get();
        newRegVaccine.setRegistrationDate(sqlDate);
        return registeredVaccineMapper.mapToRegisteredVaccineDto(registeredVaccineRepository.save(newRegVaccine));
    }

    public boolean deleteVaccineFromMedicalControl(Long registeredVaccineId) {
        Optional<RegisteredVaccine> registeredVaccine = registeredVaccineRepository.findById(registeredVaccineId);
        if (registeredVaccine.isEmpty()) {
            throw new RegisteredVaccineNotFoundException(String.format(ProjectConstants.REG_VACCINE_NOT_FOUND, registeredVaccineId));
        }
        registeredVaccineRepository.delete(registeredVaccine.get());
        return true;
    }

}
