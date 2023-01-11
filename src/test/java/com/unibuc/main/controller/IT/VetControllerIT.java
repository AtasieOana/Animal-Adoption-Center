package com.unibuc.main.controller.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.VetController;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.employees.VetService;
import com.unibuc.main.utils.EmployeeMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = VetController.class)
public class VetControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    VetService vetService;
    EmployeeDto vetDto;

    @Test
    public void getAllVetsTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(vetDto);

        //WHEN
        when(vetService.getAllEmployees()).thenReturn(employeeDtos);

        //THEN
        mockMvc.perform(get("/vets"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(employeeDtos)));
    }

    @Test
    public void getVetByNameTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.getEmployeeByName(TestConstants.FIRSTNAME,TestConstants.LASTNAME)).thenReturn(vetDto);

        //THEN
        mockMvc.perform(get("/vets/" + TestConstants.FIRSTNAME + "/" + TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(vetDto)));
    }

    @Test
    public void addNewVetTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();

        //WHEN
        when(vetService.addNewEmployee(vetDto)).thenReturn(vetDto);

        //THEN
        mockMvc.perform(post("/vets")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(vetDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(vetDto.getFirstName()))
                .andExpect(jsonPath("$.salary").value(vetDto.getSalary()));
    }

    @Test
    public void deleteEmployeeTest() throws Exception {
        //GIVEN
        vetDto = null;

        //WHEN
        when( vetService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(true);

        //THEN
        mockMvc.perform(delete("/vets/" + TestConstants.FIRSTNAME + "/" + TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(content().string(ProjectConstants.OBJ_DELETED));
    }

    @Test
    public void updateVetTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();
        EmployeeDto updatedVet = EmployeeMocks.mockVetDto();
        updatedVet.setSalary(2200);

        //WHEN
        when(vetService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME, vetDto)).thenReturn(updatedVet);

        //THEN
        mockMvc.perform(put("/vets/" + TestConstants.FIRSTNAME + "/" + TestConstants.LASTNAME)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(vetDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedVet)))
                .andExpect(jsonPath("$.firstName").value(vetDto.getFirstName()))
                .andExpect(jsonPath("$.salary").value(updatedVet.getSalary()));
    }

    @Test
    public void updateAllSalariesWithAPercentTest() throws Exception {
        //GIVEN
        vetDto = EmployeeMocks.mockVetDto();
        EmployeeDto updatedVet = EmployeeMocks.mockVetDto();
        updatedVet.setSalary(vetDto.getSalary() + vetDto.getSalary() * 20/100);
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(updatedVet);

        //WHEN
        when(vetService.updateAllSalariesWithAPercent(20)).thenReturn(employeeDtos);

        //THEN
        mockMvc.perform(put("/vets/updateAllSalaries/20"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(employeeDtos)));
    }


}
