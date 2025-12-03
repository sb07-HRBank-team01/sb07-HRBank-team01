package com.codeit_team01.sb07_hrbank_team01.employee.mapper;

import com.codeit_team01.sb07_hrbank_team01.employee.dto.response.EmployeeResponseDto;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class EmployeeMapper {
    public EmployeeResponseDto toDto(Employee employee) {
        Long profileImageId = employee.getProfile() != null
                ? employee.getProfile().getId() : null;

        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getEmployeeNo(),
                employee.getDepartment().getId(),
                employee.getDepartment().getName(),
                employee.getJobPosition(),
                employee.getHireDate().atZone(ZoneId.systemDefault()).toLocalDate(),
                employee.getStatus(),
                profileImageId
        );
    }
}
