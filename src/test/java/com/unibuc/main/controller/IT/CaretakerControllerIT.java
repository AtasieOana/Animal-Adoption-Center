package com.unibuc.main.controller.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.main.constants.ProjectConstants;
import com.unibuc.main.constants.TestConstants;
import com.unibuc.main.controller.CaretakerController;
import com.unibuc.main.dto.EmployeeDto;
import com.unibuc.main.service.employees.CaretakerService;
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
@WebMvcTest(controllers = CaretakerController.class)
public class CaretakerControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    CaretakerService caretakerService;
    EmployeeDto caretakerDto;

    @Test
    public void getAllCaretakersTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(caretakerDto);

        //WHEN
        when(caretakerService.getAllEmployees()).thenReturn(employeeDtos);

        //THEN
        mockMvc.perform(get("/caretakers"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(employeeDtos)));
    }

    @Test
    public void getCaretakerByNameTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.getEmployeeByName(TestConstants.FIRSTNAME,TestConstants.LASTNAME)).thenReturn(caretakerDto);

        //THEN
        mockMvc.perform(get("/caretakers/" + TestConstants.FIRSTNAME + "/" + TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(caretakerDto)));
    }

    @Test
    public void addNewCaretakerTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();

        //WHEN
        when(caretakerService.addNewEmployee(caretakerDto)).thenReturn(caretakerDto);

        //THEN
        mockMvc.perform(post("/caretakers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(caretakerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(caretakerDto.getFirstName()))
                .andExpect(jsonPath("$.salary").value(caretakerDto.getSalary()));
    }

    @Test
    public void deleteEmployeeTest() throws Exception {
        //GIVEN
        caretakerDto = null;

        //WHEN
        when( caretakerService.deleteEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME)).thenReturn(true);

        //THEN
        mockMvc.perform(delete("/caretakers/" + TestConstants.FIRSTNAME + "/" + TestConstants.LASTNAME))
                .andExpect(status().isOk())
                .andExpect(content().string(ProjectConstants.OBJ_DELETED));
    }

    @Test
    public void updateCaretakerTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();
        EmployeeDto updatedCaretaker = caretakerDto;
        updatedCaretaker.setSalary(2200);

        //WHEN
        when(caretakerService.updateEmployee(TestConstants.FIRSTNAME, TestConstants.LASTNAME, caretakerDto)).thenReturn(updatedCaretaker);

        //THEN
        mockMvc.perform(put("/caretakers/" + TestConstants.FIRSTNAME + "/" + TestConstants.LASTNAME)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(caretakerDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedCaretaker)))
                .andExpect(jsonPath("$.firstName").value(caretakerDto.getFirstName()))
                .andExpect(jsonPath("$.salary").value(updatedCaretaker.getSalary()));
    }

    @Test
    public void updateAllSalariesWithAPercentTest() throws Exception {
        //GIVEN
        caretakerDto = EmployeeMocks.mockCaretakerDto();
        EmployeeDto updatedCaretaker = caretakerDto;
        updatedCaretaker.setSalary(caretakerDto.getSalary() + caretakerDto.getSalary() * 20/100);
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        employeeDtos.add(updatedCaretaker);

        //WHEN
        when(caretakerService.updateAllSalariesWithAPercent(20)).thenReturn(employeeDtos);

        //THEN
        mockMvc.perform(put("/caretakers/updateAllSalaries/20"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(employeeDtos)));
    }


}
