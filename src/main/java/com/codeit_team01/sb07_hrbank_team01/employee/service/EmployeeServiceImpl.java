package com.codeit_team01.sb07_hrbank_team01.employee.service;

import com.codeit_team01.sb07_hrbank_team01.department.entity.Department;
import com.codeit_team01.sb07_hrbank_team01.department.repository.DepartmentRepository;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeCreateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeSearchConditionDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeSearchPageRequestDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.request.EmployeeUpdateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.response.EmployeePageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.employee.dto.response.EmployeeResponseDto;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import com.codeit_team01.sb07_hrbank_team01.employee.mapper.EmployeeMapper;
import com.codeit_team01.sb07_hrbank_team01.employee.repository.EmployeeRepository;
import com.codeit_team01.sb07_hrbank_team01.file.dto.FileCreateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.file.dto.FileResponseDto;
import com.codeit_team01.sb07_hrbank_team01.file.entity.MetaFile;
import com.codeit_team01.sb07_hrbank_team01.file.repository.MetaFileRepository;
import com.codeit_team01.sb07_hrbank_team01.file.service.MetaFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;
    private final MetaFileRepository fileRepository;
    private final MetaFileService fileService;

    @Override
    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeCreateRequestDto employeeCreateRequestDto,
                                              FileCreateRequestDto fileCreateRequestDto) {
        Objects.requireNonNull(employeeCreateRequestDto, "요청이 null일 수 없습니다.");

        if (employeeRepository.existsByEmailIgnoreCase(employeeCreateRequestDto.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Department department = departmentRepository.findById(employeeCreateRequestDto.departmentId())
                .orElseThrow(() -> new NoSuchElementException("부서를 찾을 수 없습니다."));

        // WILL BE CHECK : profile 파일 관리 요구사항 확인
        MetaFile profile = null;
        if (fileCreateRequestDto != null) {
            FileResponseDto file = metaFileService.createFile(fileCreateRequestDto);
            profile = metaFileRepository.getReferenceById(file.id());
        }

        long nextEmployeeNo = employeeRepository.nextEmployeeNumber();
        int year = employeeCreateRequestDto.hireDate().getYear();

        String employeeNo = String.format(
                "%s-%d_%06d",
                department.getName(),
                year,
                nextEmployeeNo
        );

        Instant hireDate = employeeCreateRequestDto.hireDate()
                .atStartOfDay(ZoneId.systemDefault()).toInstant();

        Employee newEmployee = Employee.builder()
                .name(employeeCreateRequestDto.name())
                .email(employeeCreateRequestDto.email())
                .jobPosition(employeeCreateRequestDto.position())
                .department(department)
                .employeeNo(employeeNo)
                .profile(profile)
                .hireDate(hireDate)
                .build();

        Employee save = employeeRepository.save(newEmployee);
        return employeeMapper.toDto(save);
    }

    @Override
    @Transactional
    public EmployeeResponseDto updateEmployee(EmployeeUpdateRequestDto employeeUpdateRequestDto,
                                              FileCreateRequestDto fileCreateRequestDto,
                                              Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("일치하는 사원이 없습니다."));

        if (!employee.getEmail().equalsIgnoreCase(employeeUpdateRequestDto.email()) &&
                employeeRepository.existsByEmailIgnoreCaseAndIdNot(
                        employeeUpdateRequestDto.email(), employee.getId())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Department department = departmentRepository.findById(employeeUpdateRequestDto.departmentId())
                .orElseThrow(() -> new NoSuchElementException("일치하는 부서가 없습니다."));

        MetaFile newProfile = employee.getProfile();
        if (fileCreateRequestDto != null) {
            FileResponseDto fileDto = metaFileService.createFile(fileCreateRequestDto);
            newProfile = metaFileRepository.getReferenceById(fileDto.id());
        }

        Instant hireDate = employeeUpdateRequestDto.hireDate()
                .atStartOfDay(ZoneId.systemDefault()).toInstant();

        employee.updateInfo(
                employeeUpdateRequestDto.name(),
                employeeUpdateRequestDto.email(),
                employeeUpdateRequestDto.position(),
                department,
                hireDate,
                newProfile
        );

        if (employeeUpdateRequestDto.status() != null) {
            employee.changeStatus(employeeUpdateRequestDto.status());
        }

        return employeeMapper.toDto(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("직원을 찾을 수 없습니다."));
        employeeRepository.delete(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployee(Long id) {
        Employee employee = employeeRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NoSuchElementException("직원을 찾을 수 없습니다."));
        return employeeMapper.toDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getEmployeesBySearch(EmployeeSearchConditionDto employeeSearchConditionDto) {
        List<Employee> employees = employeeRepository.search(employeeSearchConditionDto);
        return employees.stream()
                .map(employee -> employeeMapper.toDto(employee))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeePageResponseDto getEmployeesByPageSearch(EmployeeSearchPageRequestDto employeeSearchPageRequestDto) {
        int pageSize = employeeSearchPageRequestDto.size();
        EmployeeSearchPageRequestDto pageRequestDto = new EmployeeSearchPageRequestDto(
                employeeSearchPageRequestDto.employeeSearchConditionDto(),
                employeeSearchPageRequestDto.sortField(),
                employeeSearchPageRequestDto.idAfter(),
                pageSize + 1,
                employeeSearchPageRequestDto.sortDirection(),
                employeeSearchPageRequestDto.cursor()
        );
        List<Employee> employees = employeeRepository.searchPage(pageRequestDto);

        boolean hasNext = employees.size() > pageSize;
        if(hasNext) {
            employees = employees.subList(0, pageSize);
        }

        List<EmployeeResponseDto> content = employees.stream()
                .map(employee -> employeeMapper.toDto(employee))
                .toList();

        Long nextIdAfter = employees.isEmpty()
                ? null : employees.get(employees.size() - 1).getId();

        String nextCursor = nextIdAfter != null ? String.valueOf(nextIdAfter) : null;

        long totalElements = employeeRepository
                .countBySearchCondition(employeeSearchPageRequestDto.employeeSearchConditionDto());


        return new EmployeePageResponseDto(
                content,
                nextCursor,
                nextIdAfter,
                pageSize,
                totalElements,
                hasNext
        );
    }
}
