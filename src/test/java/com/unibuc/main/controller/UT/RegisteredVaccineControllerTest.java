package com.unibuc.main.controller.UT;

import com.unibuc.main.controller.RegisteredVaccineController;
import com.unibuc.main.dto.RegisteredVaccineDto;
import com.unibuc.main.dto.PartialRegisteredVaccineDto;
import com.unibuc.main.entity.MedicalRecord;
import com.unibuc.main.entity.Vaccine;
import com.unibuc.main.mapper.RegisteredVaccineMapper;
import com.unibuc.main.repository.VaccineRepository;
import com.unibuc.main.repository.MedicalRecordRepository;
import com.unibuc.main.repository.VaccineRepository;
import com.unibuc.main.service.RegisteredVaccineService;
import com.unibuc.main.utils.MedicalRecordMocks;
import com.unibuc.main.utils.RegisteredVaccineMocks;
import com.unibuc.main.utils.VaccineMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisteredVaccineControllerTest {

    @InjectMocks
    RegisteredVaccineController registeredVaccineController;
    @Mock
    Model model;
    @Mock
    BindingResult bindingResult;
    @Mock
    RegisteredVaccineService registeredVaccineService;
    @Mock
    private VaccineRepository vaccineRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @Mock
    private RegisteredVaccineMapper registeredVaccineMapper;
    RegisteredVaccineDto registeredVaccineDto;
    PartialRegisteredVaccineDto partialRegisteredVaccineDto;
    
    @Test
    public void getRegisteredVaccinesPageTest() {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        List<RegisteredVaccineDto> registeredVaccineDtos = new ArrayList<>();
        registeredVaccineDtos.add(registeredVaccineDto);
        int currentPage = 1;
        int pageSize = 5;

        //WHEN
        when(registeredVaccineService.findPaginatedRegisteredVaccines(PageRequest.of(currentPage - 1, pageSize))).thenReturn(new PageImpl<>(registeredVaccineDtos));

        //THEN
        String viewName = registeredVaccineController.getRegisteredVaccinesPage(model, Optional.of(currentPage), Optional.of(pageSize));

        assertEquals("/registeredVaccineTemplates/registeredVaccinePaginated", viewName);
        verify(registeredVaccineService, times(1)).findPaginatedRegisteredVaccines(PageRequest.of(currentPage - 1, pageSize));

        ArgumentCaptor<PageImpl> argumentCaptor = ArgumentCaptor.forClass(PageImpl.class);
        verify(model, times(1))
                .addAttribute(eq("registeredVaccinePage"), argumentCaptor.capture() );

        PageImpl registeredVaccineDtoArg = argumentCaptor.getValue();
        assertEquals(registeredVaccineDtoArg.getTotalElements(), 1);
    }

    @Test
    public void getRegisteredVaccineByMedicalRecordIdTest() {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        //WHEN
        when(registeredVaccineService.getRegisteredVaccineByMedicalRecordId(1L)).thenReturn(registeredVaccineDto);

        //THEN
        ModelAndView modelAndViewRegisteredVaccine = registeredVaccineController.getRegisteredVaccineByMedicalRecordId(1L);
        assertEquals("/registeredVaccineTemplates/registeredVaccineDetails", modelAndViewRegisteredVaccine.getViewName());
        verify(registeredVaccineService, times(1)).getRegisteredVaccineByMedicalRecordId(1L);
    }

    @Test
    public void addRegisteredVaccineFormTest() {
        //GIVEN

        //WHEN
        when(vaccineRepository.findAll()).thenReturn(Collections.singletonList(VaccineMocks.mockVaccine()));
        when(medicalRecordRepository.findAll()).thenReturn(Collections.singletonList(MedicalRecordMocks.mockMedicalRecord()));

        //THEN
        String viewName = registeredVaccineController.addRegisteredVaccineForm(model);
        assertEquals("/registeredVaccineTemplates/addRegisteredVaccineForm", viewName);
        verify(vaccineRepository, times(1)).findAll();
        verify(medicalRecordRepository, times(1)).findAll();

        ArgumentCaptor<PartialRegisteredVaccineDto> argumentCaptor = ArgumentCaptor.forClass(PartialRegisteredVaccineDto.class);
        verify(model, times(1))
                .addAttribute(eq("registeredVaccine"), argumentCaptor.capture() );
        ArgumentCaptor<List<Vaccine>> argumentCaptorVac = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("vaccinesAll"), argumentCaptorVac.capture() );
        ArgumentCaptor<List<MedicalRecord>> argumentCaptorMr = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("medicalRecordsAll"), argumentCaptorMr.capture() );
        PartialRegisteredVaccineDto registeredVaccineDtoArg = argumentCaptor.getValue();
        assertEquals(registeredVaccineDtoArg, new PartialRegisteredVaccineDto());
        List<Vaccine> vaccinesAllArg = argumentCaptorVac.getValue();
        assertEquals(vaccinesAllArg, Collections.singletonList(VaccineMocks.mockVaccine()));
        List<MedicalRecord> medicalRecordsAllArg = argumentCaptorMr.getValue();
        assertEquals(medicalRecordsAllArg, Collections.singletonList(MedicalRecordMocks.mockMedicalRecord()));
    }

    @Test
    public void saveRegisteredVaccineTest() {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();

        //WHEN
        when(registeredVaccineService.associateVaccinesWithMedicalRecord(partialRegisteredVaccineDto)).thenReturn(registeredVaccineDto);

        //THEN
        String viewName = registeredVaccineController.saveRegisteredVaccine(partialRegisteredVaccineDto, bindingResult, model);
        assertEquals("redirect:/registeredVaccines", viewName);
        verify(registeredVaccineService, times(1)).associateVaccinesWithMedicalRecord(partialRegisteredVaccineDto);
    }

    @Test
    public void editRegisteredVaccineFormTest() {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();

        //WHEN
        when(registeredVaccineMapper.mapDtoToPartial(registeredVaccineDto)).thenReturn(partialRegisteredVaccineDto);
        when(vaccineRepository.findAll()).thenReturn(Collections.singletonList(VaccineMocks.mockVaccine()));
        when(medicalRecordRepository.findAll()).thenReturn(Collections.singletonList(MedicalRecordMocks.mockMedicalRecord()));
        when(registeredVaccineService.getRegisteredVaccineByMedicalRecordId(1L)).thenReturn(registeredVaccineDto);

        //THEN
        String viewName = registeredVaccineController.editRegisteredVaccineForm(1L, model);
        assertEquals("/registeredVaccineTemplates/editRegisteredVaccineForm", viewName);
        verify(registeredVaccineMapper, times(1)).mapDtoToPartial(registeredVaccineDto);
        verify(vaccineRepository, times(1)).findAll();
        verify(medicalRecordRepository, times(1)).findAll();

        ArgumentCaptor<PartialRegisteredVaccineDto> argumentCaptor = ArgumentCaptor.forClass(PartialRegisteredVaccineDto.class);
        verify(model, times(1))
                .addAttribute(eq("registeredVaccine"), argumentCaptor.capture());
        ArgumentCaptor<List<Vaccine>> argumentCaptorVac = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("vaccinesAll"), argumentCaptorVac.capture());
        ArgumentCaptor<List<MedicalRecord>> argumentCaptorMr = ArgumentCaptor.forClass(List.class);
        verify(model, times(1))
                .addAttribute(eq("medicalRecordsAll"), argumentCaptorMr.capture());
        PartialRegisteredVaccineDto registeredVaccineDtoArg = argumentCaptor.getValue();
        assertEquals(registeredVaccineDtoArg, partialRegisteredVaccineDto);
        List<Vaccine> vaccinesAllArg = argumentCaptorVac.getValue();
        assertEquals(vaccinesAllArg, Collections.singletonList(VaccineMocks.mockVaccine()));
        List<MedicalRecord> medicalRecordsAllArg = argumentCaptorMr.getValue();
        assertEquals(medicalRecordsAllArg, Collections.singletonList(MedicalRecordMocks.mockMedicalRecord()));
    }

    @Test
    public void editRegisteredVaccineTest() {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();
        partialRegisteredVaccineDto = RegisteredVaccineMocks.mockPartialRegisteredVaccineDto();

        //WHEN
        when(registeredVaccineService.updateVaccinesWithMedicalRecord(1L, partialRegisteredVaccineDto)).thenReturn(registeredVaccineDto);

        //THEN
        String viewName = registeredVaccineController.editRegisteredVaccine(1L, partialRegisteredVaccineDto, bindingResult, model);
        assertEquals("redirect:/registeredVaccines", viewName);
        verify(registeredVaccineService, times(1)).updateVaccinesWithMedicalRecord(1L, partialRegisteredVaccineDto);
    }

    @Test
    public void deleteRegisteredVaccineTest() {
        //GIVEN
        registeredVaccineDto = RegisteredVaccineMocks.mockRegisteredVaccineDto();

        //WHEN
        when(registeredVaccineService.deleteVaccinesFromMedicalRecord(1L)).thenReturn(true);

        //THEN
        String viewName = registeredVaccineController.deleteRegisteredVaccine(1L);
        assertEquals("redirect:/registeredVaccines", viewName);
        verify(registeredVaccineService, times(1)).deleteVaccinesFromMedicalRecord(1L);
    }
}

