package com.codeit_team01.sb07_hrbank_team01.employee.repository;

import com.codeit_team01.sb07_hrbank_team01.department.entity.QDepartment;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeSearchPageRequestDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeSearchConditionDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeSortCondition;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeSortDirection;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.QEmployee;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Employee> search(EmployeeSearchConditionDto employeeSearchConditionDto) {
        QEmployee employee = QEmployee.employee;
        QDepartment department = QDepartment.department;

        BooleanBuilder builder = buildSearch(employeeSearchConditionDto, employee);

        return jpaQueryFactory
                .selectFrom(employee)
                .join(employee.department, department)
                .fetchJoin()
                .where(builder)
                .fetch();
    }

    @Override
    public List<Employee> searchPage(EmployeeSearchPageRequestDto employeeSearchPageRequestDto) {
        QEmployee employee = QEmployee.employee;
        QDepartment department = QDepartment.department;

        BooleanBuilder builder = buildSearch(
                employeeSearchPageRequestDto.employeeSearchConditionDto(),
                employee);

        JPAQuery<Employee> query = jpaQueryFactory
                .selectFrom(employee)
                .join(employee.department, department)
                .fetchJoin()
                .where(builder);

        // 커서 조건
        if(employeeSearchPageRequestDto.idAfter() != null) {
            Employee cursor = jpaQueryFactory
                    .selectFrom(employee)
                    .where(employee.id.eq(employeeSearchPageRequestDto.idAfter()))
                    .fetchOne();

            if(cursor != null) {
                BooleanExpression cursorSelect = buildCursorSelect(
                        employee,
                        employeeSearchPageRequestDto.sortField(),
                        employeeSearchPageRequestDto.sortDirection(),
                        cursor);

                query.where(cursorSelect);
            }
        }

        // 정렬
        boolean isAsc = employeeSearchPageRequestDto.sortDirection() == EmployeeSortDirection.ASC;
        switch (employeeSearchPageRequestDto.sortField()) {
            case NAME -> query.orderBy(
                    isAsc ? employee.name.asc() : employee.name.desc(),
                    isAsc ? employee.id.asc() : employee.id.desc());
            case HIRE_DATE -> query.orderBy(
                    isAsc ? employee.hireDate.asc() : employee.hireDate.desc(),
                    isAsc ? employee.id.asc() : employee.id.desc());
            case EMPLOYEE_NUMBER -> query.orderBy(
                    isAsc ? employee.employeeNo.asc() : employee.employeeNo.desc(),
                    isAsc ? employee.id.asc() :  employee.id.desc());
            default -> throw new IllegalArgumentException("지원하지 않는 정렬입니다.");
        }

        query.limit(employeeSearchPageRequestDto.size());

        return query.fetch();
    }

    @Override
    public long countBySearchCondition(EmployeeSearchConditionDto employeeSearchConditionDto) {
        QEmployee employee = QEmployee.employee;
        BooleanBuilder builder = buildSearch(employeeSearchConditionDto, employee);

        Long countQuery = jpaQueryFactory
                .select(employee.count())
                .from(employee)
                .where(builder)
                .fetchOne();

        return countQuery != null ? countQuery : 0L;
    }

    private BooleanBuilder buildSearch(EmployeeSearchConditionDto employeeSearchConditionDto,
                                       QEmployee employee) {
        BooleanBuilder builder = new BooleanBuilder();

        // 이름이나 이메일 체크
        if(StringUtils.hasText(employeeSearchConditionDto.nameOrEmail())) {
            String keyword = employeeSearchConditionDto.nameOrEmail();
            builder.and(employee.name.containsIgnoreCase(keyword)
                    .or(employee.email.containsIgnoreCase(keyword))
            );
        }

        // 부서 체크 ( 부분 일치 )
        if(StringUtils.hasText(employeeSearchConditionDto.departmentName())) {
            String departmentName = employeeSearchConditionDto.departmentName();
            builder.and(employee.department.name.containsIgnoreCase(departmentName));
        }

        // 직함 ( 부분 일치 )
        if(StringUtils.hasText(employeeSearchConditionDto.position())) {
            String position = employeeSearchConditionDto.position();
            builder.and(employee.jobPosition.containsIgnoreCase(position));
        }

        // 사번 ( 부분 일치 )
        if(StringUtils.hasText(employeeSearchConditionDto.employeeNumber())) {
            String employeeNumber = employeeSearchConditionDto.employeeNumber();
            builder.and(employee.employeeNo.containsIgnoreCase(employeeNumber));
        }

        // 입사일 범위 시작
        if(employeeSearchConditionDto.hireDateFrom() != null) {
            Instant from = employeeSearchConditionDto.hireDateFrom()
                    .atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(employee.hireDate.goe(from));
        }

        // 입사일 범위 끝
        if(employeeSearchConditionDto.hireDateTo() != null) {
            Instant to = employeeSearchConditionDto.hireDateTo()
                    .plusDays(1)
                    .atStartOfDay(ZoneId.systemDefault()).toInstant();
            builder.and(employee.hireDate.lt(to));
        }

        // 상태 ( 완전 일치 )
        if(employeeSearchConditionDto.status() != null) {
            builder.and(employee.status.eq(employeeSearchConditionDto.status()));
        }

        return builder;
    }

    private BooleanExpression buildCursorSelect(QEmployee employee,
                                                EmployeeSortCondition employeeSortCondition,
                                                EmployeeSortDirection employeeSortDirection,
                                                Employee cursor) {
        boolean isAsc = employeeSortDirection == EmployeeSortDirection.ASC;

        return switch(employeeSortCondition) {
            case NAME ->
                    isAsc ? employee.name.gt(cursor.getName())
                    .or(employee.name.eq(cursor.getName())
                            .and(employee.id.gt(cursor.getId())))
                    : employee.name.lt(cursor.getName())
                            .or(employee.name.eq(cursor.getName())
                                    .and(employee.id.lt(cursor.getId())));

            case HIRE_DATE ->
                    isAsc ? employee.hireDate.gt(cursor.getHireDate())
                    .or(employee.hireDate.eq(cursor.getHireDate())
                            .and(employee.id.gt(cursor.getId())))
                    : employee.hireDate.lt(cursor.getHireDate())
                            .or(employee.hireDate.eq(cursor.getHireDate())
                                    .and(employee.id.lt(cursor.getId())));

            case EMPLOYEE_NUMBER ->
                    isAsc ? employee.employeeNo.gt(cursor.getEmployeeNo())
                    .or(employee.employeeNo.eq(cursor.getEmployeeNo())
                            .and(employee.id.gt(cursor.getId())))
                    : employee.employeeNo.lt(cursor.getEmployeeNo())
                            .or(employee.employeeNo.eq(cursor.getEmployeeNo())
                                    .and(employee.id.lt(cursor.getId())));
        };
    }
}
