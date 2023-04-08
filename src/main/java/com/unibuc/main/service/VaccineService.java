package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.PartialVaccineDto;
import com.unibuc.main.dto.VaccineDto;
import com.unibuc.main.entity.Vaccine;
import com.unibuc.main.exception.VaccineAlreadyExistsException;
import com.unibuc.main.exception.VaccineNotFoundException;
import com.unibuc.main.mapper.VaccineMapper;
import com.unibuc.main.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VaccineService {

    @Autowired
    private VaccineRepository vaccineRepository;
    @Autowired
    private VaccineMapper vaccineMapper;

    public List<VaccineDto> getAllVaccinesOrderByExpiredDate() {
        return vaccineRepository.findAllOrderByExpirationDate()
                .stream().map(v -> vaccineMapper.mapToVaccineDto(v))
                .collect(Collectors.toList());
    }

    public VaccineDto addVaccine(VaccineDto vaccineDto) {
        if(vaccineRepository.findByVaccineName(vaccineDto.getVaccineName()).isPresent()){
            throw new VaccineAlreadyExistsException(String.format(ProjectConstants.VACCINE_EXISTS,vaccineDto.getVaccineName()));
        }
        return vaccineMapper.mapToVaccineDto(vaccineRepository.save(vaccineMapper.mapToVaccine(vaccineDto)));
    }

    public String deleteExpiredVaccines() {
        List<Vaccine> vaccineListExpired = vaccineRepository.findAllByExpirationDateBefore();
        if(vaccineListExpired.isEmpty()){
            return ProjectConstants.NO_EXP_VACCINES;
        }
        vaccineRepository.deleteAll(vaccineListExpired);
        return ProjectConstants.DELETED_EXP_VACCINES;
    }

    public VaccineDto getVaccineByName(String vaccineName){
        Optional<Vaccine> vaccine = vaccineRepository.findByVaccineName(vaccineName);
        if (vaccine.isEmpty()) {
            throw new VaccineNotFoundException(String.format(ProjectConstants.VACCINE_NOT_FOUND, vaccineName));
        }
        return vaccineMapper.mapToVaccineDto(vaccine.get());
    }

    public VaccineDto updateVaccine(String vaccineName, PartialVaccineDto partialVaccineDto) {
        Optional<Vaccine> vaccine = vaccineRepository.findByVaccineName(vaccineName);
        if (vaccine.isEmpty()) {
            throw new VaccineNotFoundException(String.format(ProjectConstants.VACCINE_NOT_FOUND, vaccineName));
        }
        Vaccine newVaccine = vaccine.get();
        newVaccine.setQuantityOnStock(partialVaccineDto.getQuantityOnStock()  != null ? partialVaccineDto.getQuantityOnStock()  : vaccine.get().getQuantityOnStock());
        newVaccine.setExpirationDate(partialVaccineDto.getExpirationDate()  != null ? partialVaccineDto.getExpirationDate()  : vaccine.get().getExpirationDate());
        return vaccineMapper.mapToVaccineDto(vaccineRepository.save(newVaccine));
    }

    public Page<VaccineDto> findPaginatedVaccines(Pageable pageable) {
        Page<VaccineDto> vaccinePage = vaccineRepository.findAll(pageable).map(vaccine -> vaccineMapper.mapToVaccineDto(vaccine));
        return vaccinePage;
    }
}
