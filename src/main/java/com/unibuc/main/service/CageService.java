package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.CageDto;
import com.unibuc.main.dto.PartialCageDto;
import com.unibuc.main.entity.Cage;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.exception.CageNotFoundException;
import com.unibuc.main.exception.EmployeeNotFoundException;
import com.unibuc.main.mapper.CageMapper;
import com.unibuc.main.repository.CageRepository;
import com.unibuc.main.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CageService {

    @Autowired
    private CageRepository cageRepository;
    @Autowired
    private CageMapper cageMapper;
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<CageDto> getAllCages() {
        return cageRepository.findAll()
                .stream().map(v -> cageMapper.mapToCageDto(v))
                .collect(Collectors.toList());
    }
    public CageDto getCageById(Long id) {
        Optional<Cage> cage = cageRepository.findById(id);
        if (cage.isEmpty()) {
            throw new CageNotFoundException(String.format(ProjectConstants.CAGE_NOT_FOUND, id));
        }
        return cageMapper.mapToCageDto(cage.get());
    }

    public CageDto addCage(PartialCageDto partialCageDto) {
        Cage cage = cageMapper.mapPartialToCage(partialCageDto);
        if (partialCageDto.getCaretakerId() != null) {
            Optional<Employee> employee = employeeRepository.findById(partialCageDto.getCaretakerId());
            if (employee.isEmpty()) {
                throw new EmployeeNotFoundException(String.format(ProjectConstants.EMP_ID_NOT_FOUND, partialCageDto.getCaretakerId()));
            }
            cage.setCaretaker(employee.get());
        }
        return cageMapper.mapToCageDto(cageRepository.save(cage));
    }

    public boolean deleteCage(Long id) {
        Optional<Cage> cage = cageRepository.findById(id);
        if (cage.isEmpty()) {
            throw new CageNotFoundException(String.format(ProjectConstants.CAGE_NOT_FOUND, id));
        }
        cageRepository.delete(cage.get());
        return true;
    }

    public CageDto updateCage(Long id, PartialCageDto partialCageDto) {
        Cage cage = cageRepository.findById(id)
                .orElseThrow(() -> new CageNotFoundException(String.format(ProjectConstants.CAGE_NOT_FOUND, id)));

        if (partialCageDto.getCaretakerId() != null) {
            Employee employee = employeeRepository.findById(partialCageDto.getCaretakerId())
                    .orElseThrow(() -> new EmployeeNotFoundException(String.format(ProjectConstants.EMP_ID_NOT_FOUND, partialCageDto.getCaretakerId())));
            cage.setCaretaker(employee);
        }
        cage.setNumberPlaces(partialCageDto.getNumberPlaces());
        Cage updatedCage = cageRepository.save(cage);
        return cageMapper.mapToCageDto(updatedCage);
    }

    public PartialCageDto getPartialCageById(Long id) {
        Optional<Cage> cage = cageRepository.findById(id);
        if (cage.isEmpty()) {
            throw new CageNotFoundException(String.format(ProjectConstants.CAGE_NOT_FOUND, id));
        }
        return cageMapper.mapCageToPartial(cage.get());
    }
}
