package com.codeit_team01.sb07_hrbank_team01.employee.repository;

import com.codeit_team01.sb07_hrbank_team01.department.entity.Department;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.stream.Stream;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    int countByDepartmentId(Long departmentId);
    boolean existsByEmailIgnoreCase(String email);
     @Query("SELECT e FROM Employee e")
    Stream<Employee> streamAll();

    @Query(value = "SELECT nextval('employee_global_number')", nativeQuery = true)
    long nextEmployeeNumber();

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}