package com.codeit_team01.sb07_hrbank_team01.employee.dto.request;

import com.codeit_team01.sb07_hrbank_team01.employee.entity.EmployeeStatus;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record EmployeeUpdateRequestDto(
        String name,
        @NotBlank String email,
        Long departmentId,
        String position,
        LocalDate hireDate,
        EmployeeStatus status,
        String memo
) {
}