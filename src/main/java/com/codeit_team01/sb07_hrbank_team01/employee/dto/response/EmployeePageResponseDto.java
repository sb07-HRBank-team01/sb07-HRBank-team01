package com.codeit_team01.sb07_hrbank_team01.employee.dto.response;

import java.util.List;

public record EmployeePageResponseDto(
        List<EmployeeResponseDto> content,
        String nextCursor,
        Long nextIdAfter,
        int size,
        long totalElements,
        boolean hasNext
) {
}
