package com.codeit_team01.sb07_hrbank_team01.department.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record DepartmentCreateRequestDto(

        @NotBlank(message = "부서 이름은 필수입니다.")
        @Size(max = 50, message = "부서 이름은 50자를 넘을 수 없습니다.")
        String name,

        @NotBlank(message = "부서 설명은 필수입니다.")
        @Size(max = 200, message = "부서 설명은 200자를 넘을 수 없습니다.")
        String description,

        @PastOrPresent(message = "설립일은 오늘 또는 과거여야 합니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "설립일은 필수입니다.")
        LocalDate establishedDate
) {



}
