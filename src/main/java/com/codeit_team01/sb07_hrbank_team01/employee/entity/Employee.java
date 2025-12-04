package com.codeit_team01.sb07_hrbank_team01.employee.entity;

import com.codeit_team01.sb07_hrbank_team01.base.BaseEntity;
import com.codeit_team01.sb07_hrbank_team01.department.entity.Department;
import com.codeit_team01.sb07_hrbank_team01.file.entity.MetaFile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Entity
@Table(name = "employees")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee extends BaseEntity {
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "job_position", nullable = false, length = 50)
    private String jobPosition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "hire_date", nullable = false)
    private Instant hireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private EmployeeStatus status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    private MetaFile profile;

    @Column(name = "employee_no", nullable = false, unique = true, length = 50)
    private String employeeNo;

    @Builder
    private Employee(String name, String email,
            String jobPosition, Department department,
            Instant hireDate, String employeeNo, MetaFile profile) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("이메일이 null이거나 empty면 안됩니다.");
        }
        this.name = name;
        this.email = email.toLowerCase();
        this.jobPosition = jobPosition;
        this.department = department;
        this.hireDate = hireDate;
        this.employeeNo = employeeNo;
        this.profile = profile;
        this.status = EmployeeStatus.ACTIVE;
    }

    public void updateInfo(String name, String email,
                           String jobPosition, Department department,
                           Instant hireDate, MetaFile profile) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("이메일이 null이거나 empty면 안됩니다.");
        }
        this.email = email.toLowerCase();
        this.name = name;
        this.jobPosition = jobPosition;
        this.department = department;
        this.hireDate = hireDate;
        this.profile = profile;
    }

    public void changeStatus(EmployeeStatus status) {
        this.status = status;
    }
}
