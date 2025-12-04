package com.codeit_team01.sb07_hrbank_team01.employee.dto.request;

import com.codeit_team01.sb07_hrbank_team01.employee.entity.EmployeeStatus;

import java.time.LocalDate;

public record EmployeeSearchConditionDto(
        String nameOrEmail,
        String departmentName,
        String position,
        String employeeNumber,
        LocalDate hireDateFrom,
        LocalDate hireDateTo,
        EmployeeStatus status
) {
}
