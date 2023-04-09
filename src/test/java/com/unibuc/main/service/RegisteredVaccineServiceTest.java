package com.unibuc.main.service;

import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.dto.PartialRegisteredVaccineDto;
import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.entity.RegisteredVaccine;
import com.unibuc.main.exception.MedicalRecordNotFoundException;
import com.unibuc.main.exception.RegisteredVaccineNotFoundException;
import com.unibuc.main.exception.VaccineNotFoundException;
import com.unibuc.main.mapper.MedicalRecordMapper;
import com.unibuc.main.mapper.RegisteredVaccineMapper;
import com.unibuc.main.mapper.VaccineMapper;
import com.unibuc.main.repository.MedicalRecordRepository;
import com.unibuc.main.repository.RegisteredVaccineRepository;
import com.unibuc.main.repository.VaccineRepository;
import com.unibuc.main.utils.RegisteredVaccineMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisteredVaccineServiceTest {

    @InjectMocks
    RegisteredVaccineService registeredVaccineService;

    @Mock
    private RegisteredVaccineRepository registeredVaccineRepository;

    @Mock
    private RegisteredVaccineMapper registeredVaccineMapper;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private VaccineMapper vaccineMapper;

    @Mock
    MedicalRecordMapper medicalRecordMapper;

    RegisteredVaccine registeredVaccine;
    RegisteredVaccineDto registeredVaccineDto;
    PartialRegisteredVaccineDto partialRegisteredVaccineDto;

    @Test
    public void testGetAllRegVaccines() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        List<RegisteredVaccine> registeredVaccines = new ArrayList<>();
        registeredVaccines.add(registeredVaccine);
        List<RegisteredVaccineDto> registeredVaccineDtos = new ArrayList<>();
        registeredVaccineDtos.add(registeredVaccineDto);

        //WHEN
        when(registeredVaccineRepository.findAll()).thenReturn(registeredVaccines);
        when(medicalRecordRepository.findAll()).thenReturn(Collections.singletonList(registeredVaccine.getMedicalRecord()));
        when(vaccineMapper.mapToVaccineDto(registeredVaccine.getVaccine())).thenReturn(registeredVaccineDto.getVaccinesDto().get(0));
        when(medicalRecordMapper.mapToMedicalRecordDto(registeredVaccine.getMedicalRecord())).thenReturn(registeredVaccineDto.getMedicalRecordDto());
        when(registeredVaccineRepository.findByMedicalRecordId(registeredVaccine.getMedicalRecord().getId())).thenReturn(registeredVaccines);

        //THEN
        List<RegisteredVaccineDto> result = registeredVaccineService.getAllRegisteredVaccines();
        assertEquals(result, registeredVaccineDtos);
    }

    @Test
    public void testGetRegisteredVaccineByMedicalRecordId() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        List<RegisteredVaccine> registeredVaccines = new ArrayList<>();
        registeredVaccines.add(registeredVaccine);

        //WHEN
        when(registeredVaccineRepository.findByMedicalRecordId(registeredVaccine.getMedicalRecord().getId())).thenReturn(registeredVaccines);
        when(vaccineMapper.mapToVaccineDto(registeredVaccine.getVaccine())).thenReturn(registeredVaccineDto.getVaccinesDto().get(0));
        when(medicalRecordMapper.mapToMedicalRecordDto(registeredVaccine.getMedicalRecord())).thenReturn(registeredVaccineDto.getMedicalRecordDto());

        //THEN
        RegisteredVaccineDto result = registeredVaccineService.getRegisteredVaccineByMedicalRecordId(registeredVaccine.getMedicalRecord().getId());
        assertEquals(result, registeredVaccineDto);
        assertThat(result).isNotNull();
    }

    @Test
    public void testGetRegisteredVaccineByMedicalRecordIdException() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        //WHEN
        when(registeredVaccineRepository.findByMedicalRecordId(registeredVaccine.getMedicalRecord().getId())).thenReturn(new ArrayList<>());

        //THEN
        RegisteredVaccineNotFoundException registeredVaccineNotFoundException = assertThrows(RegisteredVaccineNotFoundException.class, () -> registeredVaccineService.getRegisteredVaccineByMedicalRecordId(registeredVaccine.getMedicalRecord().getId()));
        assertEquals(String.format(ProjectConstants.REG_VACCINE_NOT_FOUND, registeredVaccine.getId()), registeredVaccineNotFoundException.getMessage());
    }

    @Test
    public void testDeleteVaccinesFromMedicalRecord() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();

        List<RegisteredVaccine> registeredVaccines = new ArrayList<>();
        registeredVaccines.add(registeredVaccine);

        //WHEN
        when(registeredVaccineRepository.findByMedicalRecordId(registeredVaccine.getMedicalRecord().getId())).thenReturn(registeredVaccines);

        //THEN
        Boolean result = registeredVaccineService.deleteVaccinesFromMedicalRecord(registeredVaccine.getMedicalRecord().getId());
        assertEquals(result, true);
    }

    @Test
    public void testDeleteRegVaccineException() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();

        //WHEN
        when(registeredVaccineRepository.findByMedicalRecordId(2L)).thenReturn(new ArrayList<>());

        //THEN
        RegisteredVaccineNotFoundException registeredVaccineNotFoundException = assertThrows(RegisteredVaccineNotFoundException.class, () -> registeredVaccineService.deleteVaccinesFromMedicalRecord(2L));
        assertEquals(String.format(ProjectConstants.REG_VACCINE_NOT_FOUND, 2L), registeredVaccineNotFoundException.getMessage());
    }

    @Test
    public void testUpdateVaccinesWithMedicalRecord() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccine.setRegistrationDate(new Date(2023, Calendar.NOVEMBER,10));
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();
        registeredVaccineDto.setRegistrationDate(new Date(2023, Calendar.NOVEMBER,10));
        partialRegisteredVaccineDto.setRegistrationDate(new Date(2023, Calendar.NOVEMBER,10));

        List<RegisteredVaccine> registeredVaccines = new ArrayList<>();
        registeredVaccines.add(registeredVaccine);
        RegisteredVaccine registeredVaccineWithoutId = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineWithoutId.setId(null);
        registeredVaccineWithoutId.setRegistrationDate(new Date(2023, Calendar.NOVEMBER,10));

        //WHEN
        when(medicalRecordRepository.findById(partialRegisteredVaccineDto.getMedicalRecordId())).thenReturn(Optional.ofNullable(registeredVaccine.getMedicalRecord()));
        when(registeredVaccineRepository.findByMedicalRecordId(partialRegisteredVaccineDto.getMedicalRecordId())).thenReturn(registeredVaccines);
        when(registeredVaccineRepository.findByMedicalRecordIdAndVaccineId(partialRegisteredVaccineDto.getMedicalRecordId(), registeredVaccine.getVaccine().getId())).thenReturn(Optional.empty());
        when(vaccineRepository.findById(registeredVaccine.getVaccine().getId())).thenReturn(Optional.ofNullable(registeredVaccine.getVaccine()));
        when(registeredVaccineRepository.save(registeredVaccineWithoutId)).thenReturn(registeredVaccine);
        when(medicalRecordMapper.mapToMedicalRecordDto(registeredVaccine.getMedicalRecord())).thenReturn(registeredVaccineDto.getMedicalRecordDto());
        when(vaccineMapper.mapToVaccineDto(registeredVaccine.getVaccine())).thenReturn(registeredVaccineDto.getVaccinesDto().get(0));

        //THEN
        RegisteredVaccineDto result = registeredVaccineService.updateVaccinesWithMedicalRecord(partialRegisteredVaccineDto.getMedicalRecordId(), partialRegisteredVaccineDto);
        assertEquals(result, registeredVaccineDto);
        assertEquals(result.getRegistrationDate(), new Date(2023, Calendar.NOVEMBER,10));
        assertNotNull(result);
    }

    @Test
    public void testUpdateVaccinesWithMedicalRecordMedicalRecordException() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();
        partialRegisteredVaccineDto.setRegistrationDate(new Date(2023, Calendar.NOVEMBER,10));

        //WHEN
        when(medicalRecordRepository.findById(partialRegisteredVaccineDto.getMedicalRecordId())).thenReturn(Optional.empty());

        //THEN
        MedicalRecordNotFoundException medicalRecordNotFoundException = assertThrows(MedicalRecordNotFoundException.class, () -> registeredVaccineService.updateVaccinesWithMedicalRecord(partialRegisteredVaccineDto.getMedicalRecordId(),  partialRegisteredVaccineDto));
        assertEquals(String.format(ProjectConstants.RECORD_NOT_FOUND, partialRegisteredVaccineDto.getMedicalRecordId()), medicalRecordNotFoundException.getMessage());
    }

    @Test
    public void testUpdateVaccinesWithMedicalRecordVaccineException() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();
        partialRegisteredVaccineDto.setRegistrationDate(new Date(2023, Calendar.NOVEMBER,10));

        List<RegisteredVaccine> registeredVaccines = new ArrayList<>();
        registeredVaccines.add(registeredVaccine);

        //WHEN
        when(medicalRecordRepository.findById(partialRegisteredVaccineDto.getMedicalRecordId())).thenReturn(Optional.ofNullable(registeredVaccine.getMedicalRecord()));
        when(registeredVaccineRepository.findByMedicalRecordId(partialRegisteredVaccineDto.getMedicalRecordId())).thenReturn(registeredVaccines);
        when(registeredVaccineRepository.findByMedicalRecordIdAndVaccineId(partialRegisteredVaccineDto.getMedicalRecordId(), registeredVaccine.getVaccine().getId())).thenReturn(Optional.empty());
        when(vaccineRepository.findById(registeredVaccine.getVaccine().getId())).thenReturn(Optional.empty());

        //THEN
        VaccineNotFoundException vaccineNotFoundException = assertThrows(VaccineNotFoundException.class, () -> registeredVaccineService.updateVaccinesWithMedicalRecord(partialRegisteredVaccineDto.getMedicalRecordId(), partialRegisteredVaccineDto));
        assertEquals(String.format(ProjectConstants.VACCINE_NOT_ID, registeredVaccine.getVaccine().getId()), vaccineNotFoundException.getMessage());
    }

    @Test
    public void testAssociateVaccinesWithMedicalRecord() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();

        RegisteredVaccine registeredVaccineWithoutId = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineWithoutId.setId(null);

        //WHEN
        when(medicalRecordRepository.findById(partialRegisteredVaccineDto.getMedicalRecordId())).thenReturn(Optional.ofNullable(registeredVaccine.getMedicalRecord()));
        when(vaccineRepository.findById(partialRegisteredVaccineDto.getVaccinesId().get(0))).thenReturn(Optional.ofNullable(registeredVaccine.getVaccine()));
        when(registeredVaccineRepository.save(registeredVaccineWithoutId)).thenReturn(registeredVaccine);
        when(vaccineMapper.mapToVaccineDto(registeredVaccine.getVaccine())).thenReturn(registeredVaccineDto.getVaccinesDto().get(0));
        when(medicalRecordMapper.mapToMedicalRecordDto(registeredVaccine.getMedicalRecord())).thenReturn(registeredVaccineDto.getMedicalRecordDto());

        //THEN
        RegisteredVaccineDto result = registeredVaccineService.associateVaccinesWithMedicalRecord(partialRegisteredVaccineDto);
        assertEquals(result, registeredVaccineDto);
        assertNotNull(result);
    }

    @Test
    public void testAssociateVaccinesWithMedicalRecordMedicalRecordException() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();

        //WHEN
        when(medicalRecordRepository.findById(registeredVaccine.getMedicalRecord().getId())).thenReturn(Optional.empty());

        //THEN
        MedicalRecordNotFoundException medicalRecordNotFoundException = assertThrows(MedicalRecordNotFoundException.class, () -> registeredVaccineService.associateVaccinesWithMedicalRecord(partialRegisteredVaccineDto));
        assertEquals(String.format(ProjectConstants.RECORD_NOT_FOUND, registeredVaccine.getMedicalRecord().getId()), medicalRecordNotFoundException.getMessage());
    }

    @Test
    public void testAssociateVaccinesWithMedicalRecordVaccineException() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();

        //WHEN
        when(medicalRecordRepository.findById(partialRegisteredVaccineDto.getMedicalRecordId())).thenReturn(Optional.ofNullable(registeredVaccine.getMedicalRecord()));
        when(vaccineRepository.findById(registeredVaccine.getVaccine().getId())).thenReturn(Optional.empty());

        //THEN
        VaccineNotFoundException vaccineNotFoundException = assertThrows(VaccineNotFoundException.class, () -> registeredVaccineService.associateVaccinesWithMedicalRecord(partialRegisteredVaccineDto));
        assertEquals(String.format(ProjectConstants.VACCINE_NOT_ID, registeredVaccine.getVaccine().getId()), vaccineNotFoundException.getMessage());
    }

    @Test
    public void testFindPaginatedCaretakers() {
        //GIVEN
        registeredVaccine = RegisteredVaccineMocks.mockRegisteredVaccine();
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        Pageable pageable = PageRequest.of(0,20);

        List<RegisteredVaccine> registeredVaccines = new ArrayList<>();
        registeredVaccines.add(registeredVaccine);
        List<RegisteredVaccineDto> registeredVaccineDtos = new ArrayList<>();
        registeredVaccineDtos.add(registeredVaccineDto);

        //WHEN
        when(registeredVaccineRepository.findAll()).thenReturn(registeredVaccines);
        when(medicalRecordRepository.findAll()).thenReturn(Collections.singletonList(registeredVaccine.getMedicalRecord()));
        when(vaccineMapper.mapToVaccineDto(registeredVaccine.getVaccine())).thenReturn(registeredVaccineDto.getVaccinesDto().get(0));
        when(medicalRecordMapper.mapToMedicalRecordDto(registeredVaccine.getMedicalRecord())).thenReturn(registeredVaccineDto.getMedicalRecordDto());
        when(registeredVaccineRepository.findByMedicalRecordId(registeredVaccine.getMedicalRecord().getId())).thenReturn(registeredVaccines);

        //THEN
        Page<RegisteredVaccineDto> result = registeredVaccineService.findPaginatedRegisteredVaccines(pageable);
        assertEquals(result, new PageImpl<>(registeredVaccineDtos, pageable, registeredVaccineDtos.size()));
    }
}
