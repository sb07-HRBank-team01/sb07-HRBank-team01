package com.codeit_team01.sb07_hrbank_team01.employee.dto.request;

public record EmployeeSearchPageRequestDto(
        EmployeeSearchConditionDto employeeSearchConditionDto,
        EmployeeSortCondition sortField,
        Long idAfter,
        int size,
        EmployeeSortDirection sortDirection,
        String cursor
) {
}
