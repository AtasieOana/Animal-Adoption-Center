package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.DietDto;
import com.unibuc.main.entity.Diet;
import com.unibuc.main.entity.Employee;
import com.unibuc.main.exception.DietAlreadyExistsException;
import com.unibuc.main.exception.DietNotFoundException;
import com.unibuc.main.exception.EmployeeAlreadyExistsException;
import com.unibuc.main.mapper.DietMapper;
import com.unibuc.main.repository.DietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DietService {

    @Autowired
    private DietRepository dietRepository;
    @Autowired
    private DietMapper dietMapper;

    public List<DietDto> getAllDiets() {
        return dietRepository.findAll()
                .stream().map(v -> dietMapper.mapToDietDto(v))
                .collect(Collectors.toList());
    }

    public DietDto getDietByType(String dietType) {
        Optional<Diet> diet = dietRepository.findByDietType(dietType);
        if (diet.isEmpty()) {
            throw new DietNotFoundException(String.format(ProjectConstants.DIET_NOT_FOUND, dietType));
        }
        return dietMapper.mapToDietDto(diet.get());
    }

    public DietDto addDiet(DietDto dietDto) {
        if(dietRepository.findByDietType(dietDto.getDietType()).isPresent()){
            throw new DietAlreadyExistsException(String.format(ProjectConstants.DIET_EXISTS,dietDto.getDietType()));
        }
        return dietMapper.mapToDietDto(dietRepository.save(dietMapper.mapToDiet(dietDto)));
    }

    public DietDto updateDietPartial(String dietType, DietDto dietDto) {
        Optional<Diet> diet = dietRepository.findByDietType(dietType);
        if (diet.isEmpty()) {
            throw new DietNotFoundException(String.format(ProjectConstants.DIET_NOT_FOUND, dietType));
        }
        String newDietType = dietDto.getDietType();
        Optional<Diet> existingDietOpt = dietRepository.findByDietType(newDietType);
        if (existingDietOpt.isPresent() && !Objects.equals(existingDietOpt.get().getId(), diet.get().getId())) {
            throw new DietAlreadyExistsException(String.format(ProjectConstants.DIET_EXISTS,dietDto.getDietType()));
        }
        Diet newDiet = diet.get();
        newDiet.setDietType(dietDto.getDietType() != null ? dietDto.getDietType() : diet.get().getDietType());
        newDiet.setQuantityOnStock(dietDto.getQuantityOnStock() != null ? dietDto.getQuantityOnStock() : diet.get().getQuantityOnStock());
        return dietMapper.mapToDietDto(dietRepository.save(newDiet));
    }

    public String deleteDietOnlyIfStockEmpty(String dietType) {
        Optional<Diet> diet = dietRepository.findByDietType(dietType);
        if (diet.isEmpty()) {
            throw new DietNotFoundException(String.format(ProjectConstants.DIET_NOT_FOUND, dietType));
        }
        if(diet.get().getQuantityOnStock() != 0){
            return ProjectConstants.DIET_NOT_DELETED;
        }
        else{
            dietRepository.delete(diet.get());
            return ProjectConstants.DIET_DELETED;
        }
    }

}
