package com.codeit_team01.sb07_hrbank_team01.employee.repository;

import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeSearchPageRequestDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeSearchConditionDto;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;

import java.util.List;

public interface EmployeeRepositoryCustom {
    List<Employee> search(EmployeeSearchConditionDto employeeSearchConditionDto);
    List<Employee> searchPage(EmployeeSearchPageRequestDto employeeSearchPageRequestDto);
    long countBySearchCondition(EmployeeSearchConditionDto employeeSearchConditionDto);
}
