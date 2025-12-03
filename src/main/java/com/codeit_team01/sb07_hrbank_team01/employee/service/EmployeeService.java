package com.codeit_team01.sb07_hrbank_team01.employee.service;

import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeCreateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeUpdateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.response.EmployeeResponseDto;
import com.codeit_team01.sb07_hrbank_team01.file.dto.FileCreateRequestDto;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(EmployeeCreateRequestDto employeeCreateRequestDto,
                                       FileCreateRequestDto fileCreateRequestDto);
    EmployeeResponseDto updateEmployee(EmployeeUpdateRequestDto employeeUpdateRequestDto,
                                       FileCreateRequestDto fileCreateRequestDto,
                                       Long id);
    void deleteEmployee(Long id);
    EmployeeResponseDto getEmployee(Long id);
}
