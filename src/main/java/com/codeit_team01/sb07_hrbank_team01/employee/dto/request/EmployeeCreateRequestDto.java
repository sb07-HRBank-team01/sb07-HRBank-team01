package com.codeit_team01.sb07_hrbank_team01.employee.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmployeeCreateRequestDto(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull Long departmentId,
        @NotBlank String position,
        @NotNull LocalDate hireDate,
        String memo
) {
}