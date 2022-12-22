package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.CageDto;
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

    public CageDto getCageById(Long id) {
        Optional<Cage> cage = cageRepository.findById(id);
        if (cage.isEmpty()) {
            throw new CageNotFoundException(String.format(ProjectConstants.CAGE_NOT_FOUND, id));
        }
        return cageMapper.mapToCageDto(cage.get());
    }

    public List<CageDto> getCagesWithoutACaretaker(){
        return cageRepository.findAllByCaretakerNull()
                .stream().map(c -> cageMapper.mapToCageDto(c))
                .collect(Collectors.toList());
    }
    public CageDto addCage(CageDto cageDto) {
        Cage cage = cageMapper.mapToCage(cageDto);
        if (cageDto.getCaretaker() != null) {
            Optional<Employee> employee = employeeRepository.findCaretakerByName(cageDto.getCaretaker().getFirstName(), cageDto.getCaretaker().getLastName());
            if (employee.isEmpty()) {
                throw new EmployeeNotFoundException(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, cageDto.getCaretaker().getFirstName() + ' ' + cageDto.getCaretaker().getLastName()));
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

    public CageDto updatePlacesInCage(Long id, Integer newPlacesNumber) {
        Optional<Cage> cage = cageRepository.findById(id);
        if (cage.isEmpty()) {
            throw new CageNotFoundException(String.format(ProjectConstants.CAGE_NOT_FOUND, id));
        }
        Cage newCage = cage.get();
        newCage.setNumberPlaces(newPlacesNumber);
        return cageMapper.mapToCageDto(cageRepository.save(newCage));
    }

    public CageDto updateCageCaretaker(Long id, String caretakerFirstName, String caretakerLastName) {
        Optional<Cage> cage = cageRepository.findById(id);
        if (cage.isEmpty()) {
            throw new CageNotFoundException(String.format(ProjectConstants.CAGE_NOT_FOUND, id));
        }
        Optional<Employee> employee = employeeRepository.findCaretakerByName(caretakerFirstName, caretakerLastName);
        if (employee.isEmpty()) {
            throw new EmployeeNotFoundException(String.format(ProjectConstants.EMPLOYEE_NOT_FOUND, caretakerFirstName + ' ' + caretakerLastName));
        }
        Cage newCage = cage.get();
        newCage.setCaretaker(employee.get());
        return cageMapper.mapToCageDto(cageRepository.save(newCage));
    }

}
