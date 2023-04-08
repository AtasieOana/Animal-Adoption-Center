package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.PartialRegisteredVaccineDto;
import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.entity.MedicalRecord;
import com.unibuc.main.entity.RegisteredVaccine;
import com.unibuc.main.entity.Vaccine;
import com.unibuc.main.exception.MedicalRecordNotFoundException;
import com.unibuc.main.exception.VaccineNotFoundException;
import com.unibuc.main.mapper.MedicalRecordMapper;
import com.unibuc.main.mapper.RegisteredVaccineMapper;
import com.unibuc.main.mapper.VaccineMapper;
import com.unibuc.main.repository.MedicalRecordRepository;
import com.unibuc.main.repository.RegisteredVaccineRepository;
import com.unibuc.main.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
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

    @Autowired
    private VaccineMapper vaccineMapper;

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    public List<RegisteredVaccineDto> getAllRegisteredVaccines() {
        List<RegisteredVaccine> registeredVaccines = registeredVaccineRepository.findAll();
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();

        Map<Long, List<VaccineDto>> vaccinesForMedicalRecord = new HashMap<>();
        for (RegisteredVaccine registeredVaccine : registeredVaccines) {
            vaccinesForMedicalRecord
                    .computeIfAbsent(registeredVaccine.getMedicalRecord().getId(), k -> new ArrayList<>())
                    .add(vaccineMapper.mapToVaccineDto(registeredVaccine.getVaccine()));
        }

        List<RegisteredVaccineDto> registeredVaccineDtoList = new ArrayList<>();
        for (MedicalRecord medicalRecord : medicalRecords) {
            if(vaccinesForMedicalRecord.get(medicalRecord.getId()) != null) {
                RegisteredVaccineDto registeredVaccineDto = RegisteredVaccineDto.builder()
                        .vaccinesDto(vaccinesForMedicalRecord.get(medicalRecord.getId()))
                        .medicalRecordDto(medicalRecordMapper.mapToMedicalRecordDto(medicalRecord))
                        .registrationDate(registeredVaccineRepository.findByMedicalRecordId(medicalRecord.getId()).get(0).getRegistrationDate())
                        .build();
                registeredVaccineDtoList.add(registeredVaccineDto);
            }
        }

        return registeredVaccineDtoList;
    }

    public RegisteredVaccineDto getRegisteredVaccineByMedicalRecordId(Long medicalRecordId) {
        List<RegisteredVaccine> registeredVaccines = registeredVaccineRepository.findByMedicalRecordId(medicalRecordId);
        if (registeredVaccines.isEmpty()) {
            throw new IllegalArgumentException("No registered vaccines found for medical record with ID " + medicalRecordId);
        }
        List<VaccineDto> vaccinesForMedicalRecord = new ArrayList<>();
        for (RegisteredVaccine registeredVaccine : registeredVaccines) {
            vaccinesForMedicalRecord.add(vaccineMapper.mapToVaccineDto(registeredVaccine.getVaccine()));
        }

        return RegisteredVaccineDto.builder()
                .vaccinesDto(vaccinesForMedicalRecord)
                .medicalRecordDto(medicalRecordMapper.mapToMedicalRecordDto(registeredVaccines.get(0).getMedicalRecord()))
                .registrationDate(registeredVaccines.get(0).getRegistrationDate())
                .build();
    }

    public RegisteredVaccineDto associateVaccinesWithMedicalRecord(PartialRegisteredVaccineDto partialRegisteredVaccineDto) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(partialRegisteredVaccineDto.getMedicalRecordId())
                .orElseThrow(() -> new MedicalRecordNotFoundException(String.format(ProjectConstants.RECORD_NOT_FOUND, partialRegisteredVaccineDto.getMedicalRecordId())));

        List<VaccineDto> vaccineDtos = partialRegisteredVaccineDto.getVaccinesId().stream()
                .map(vaccineId -> {
                    Vaccine vaccine = vaccineRepository.findById(vaccineId)
                            .orElseThrow(() -> new VaccineNotFoundException(String.format(ProjectConstants.VACCINE_NOT_ID, vaccineId)));
                    RegisteredVaccine registeredVaccine = RegisteredVaccine.builder()
                            .registrationDate(partialRegisteredVaccineDto.getRegistrationDate())
                            .vaccine(vaccine)
                            .medicalRecord(medicalRecord)
                            .build();
                    registeredVaccineRepository.save(registeredVaccine);
                    return vaccineMapper.mapToVaccineDto(vaccine);
                })
                .collect(Collectors.toList());

        return RegisteredVaccineDto.builder()
                .vaccinesDto(vaccineDtos)
                .medicalRecordDto(medicalRecordMapper.mapToMedicalRecordDto(medicalRecord))
                .registrationDate(partialRegisteredVaccineDto.getRegistrationDate())
                .build();
    }

    public RegisteredVaccineDto updateVaccinesWithMedicalRecord(Long medicalRecordId, PartialRegisteredVaccineDto partialRegisteredVaccineDto) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new MedicalRecordNotFoundException(String.format(ProjectConstants.RECORD_NOT_FOUND, partialRegisteredVaccineDto.getMedicalRecordId())));

        Set<Long> updatedVaccineIds = new HashSet<>(partialRegisteredVaccineDto.getVaccinesId());

        List<RegisteredVaccine> registeredVaccines = registeredVaccineRepository.findByMedicalRecordId(partialRegisteredVaccineDto.getMedicalRecordId());
        registeredVaccines.stream()
                .filter(rv -> !updatedVaccineIds.contains(rv.getVaccine().getId()))
                .forEach(registeredVaccineRepository::delete);

        updatedVaccineIds.stream()
                .filter(vaccineId -> registeredVaccineRepository.findByMedicalRecordIdAndVaccineId(partialRegisteredVaccineDto.getMedicalRecordId(), vaccineId).isEmpty())
                .map(vaccineId -> vaccineRepository.findById(vaccineId)
                        .orElseThrow(() -> new VaccineNotFoundException(String.format(ProjectConstants.VACCINE_NOT_ID, vaccineId))))
                .map(vaccine -> RegisteredVaccine.builder()
                        .registrationDate(partialRegisteredVaccineDto.getRegistrationDate())
                        .vaccine(vaccine)
                        .medicalRecord(medicalRecord)
                        .build())
                .forEach(registeredVaccineRepository::save);

        return this.getRegisteredVaccineByMedicalRecordId(medicalRecord.getId());
    }


    public boolean deleteVaccinesFromMedicalRecord(Long medicalRecordId) {
        List<RegisteredVaccine> registeredVaccines = registeredVaccineRepository.findByMedicalRecordId(medicalRecordId);
        if (registeredVaccines.isEmpty()) {
            throw new IllegalArgumentException("No registered vaccines found for medical record with ID " + medicalRecordId);
        }
        registeredVaccineRepository.deleteAll(registeredVaccines);
        return true;
    }

    public Page<RegisteredVaccineDto> findPaginatedRegisteredVaccines(Pageable pageable) {
        List<RegisteredVaccineDto> registeredVaccineDtoList = getAllRegisteredVaccines();
        Page<RegisteredVaccineDto> registeredVaccinePage = new PageImpl<>(registeredVaccineDtoList, pageable, registeredVaccineDtoList.size());
        return registeredVaccinePage;
    }
}
