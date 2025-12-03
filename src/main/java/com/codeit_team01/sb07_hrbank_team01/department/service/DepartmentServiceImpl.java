package com.codeit_team01.sb07_hrbank_team01.department.service;

import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import com.codeit_team01.sb07_hrbank_team01.common.mapper.PageResponseMapper;
import com.codeit_team01.sb07_hrbank_team01.department.entity.Department;
import com.codeit_team01.sb07_hrbank_team01.department.repository.DepartmentRepository;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentCreateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentSearchRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.request.DepartmentUpdateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.department.response.DepartmentResponseDto;
import com.codeit_team01.sb07_hrbank_team01.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import java.util.NoSuchElementException;


@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PageResponseMapper pageResponseMapper;

    @Override
    @Transactional
    public DepartmentResponseDto createDepartment(DepartmentCreateRequestDto request) {
        boolean exist = departmentRepository.existsByName(request.name());
        if(exist){
            throw new IllegalArgumentException("이미 존재하는 부서 이름입니다: " + request.name());
        }
        Department department = Department.of(request.name(), request.description(), request.establishedDate());

        Department save = departmentRepository.save(department);


        return DepartmentResponseDto.from(save, 0);
    }

    @Override
    @Transactional
    public DepartmentResponseDto updateDepartment(Long departmentId, DepartmentUpdateRequestDto request) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NoSuchElementException("부서를 찾을 수 없습니다: " + departmentId));

        if (!department.getName().equals(request.name())
                && departmentRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("이미 존재하는 부서 이름입니다: " + request.name());
        }

        department.update(request.name(), request.description(), request.establishedDate());

        int employeeCount = (int) employeeRepository.countByDepartmentId(department.getId());

        return DepartmentResponseDto.from(department, employeeCount);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new NoSuchElementException("부서 아이디로 찾을 수 없습니다");
        }

        departmentRepository.deleteById(departmentId);

    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDto getDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NoSuchElementException("부서 아이디로 찾을수 없습니다"));

        int employeeCount = (int) employeeRepository.countByDepartmentId(department.getId());

        return DepartmentResponseDto.from(department, employeeCount);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<Department> searchDepartment(DepartmentSearchRequestDto req) {
        //처음부터 커서기준으로 정렬을 해야할때가 있다



        //없거나 0보다 작으면 디폴트 10에 고정
        int size = (req.size() == null || req.size() <= 0) ? 10 : req.size();

        // 정렬 없음(쿼리에 고정) 커서때문에 커서가 널이면 다 가지고와서 0은그냥 디폴트로
        Pageable pageable = PageRequest.of(0, size);

        //혹시모르니 일단 소문자로 바꾸자
        String field = req.sortField() == null ? "name" : req.sortField().toLowerCase();
        String dir   = req.sortDirection() == null ? "asc"  : req.sortDirection().toLowerCase();

        Page<Department> page;

        //커서가name이냐,establishedDate냐  x  오름차냐,내림차냐
        //null이면 쿼리 들어갈때 전부 조회라 null인것같으면 null로
        if ("name".equals(field)) {
            String cursorName = (req.cursor() == null || req.cursor().isBlank()) ? null : req.cursor();
            page = "asc".equals(dir)
                    ? departmentRepository.findByNameAsc(req.nameOrDescription(), cursorName, req.idAfter(), pageable)
                    : departmentRepository.findByNameDesc(req.nameOrDescription(), cursorName, req.idAfter(), pageable);

        } else if ("establishedDate".equals(field)) {
            LocalDate cursorDate = parseDateOrNull(req.cursor());
            page = "asc".equals(dir)
                    ? departmentRepository.findByDateAsc(req.nameOrDescription(), cursorDate, req.idAfter(), pageable)
                    : departmentRepository.findByDateDesc(req.nameOrDescription(), cursorDate, req.idAfter(), pageable);

        } else {
            //한번 더 확인
            throw new IllegalArgumentException("sortField 값은 'name' or 'establishedDate' 이어야 한다");
        }

        // DTO 매핑 (employeeCount  이건 많이 조회해서 엠플로이에 한번에 집계하는걸 만들어봐야한다
        List<DepartmentResponseDto> contents = page.getContent().stream()
                .map(d -> DepartmentResponseDto.from(d, (int) employeeRepository.countByDepartmentId(d.getId())))
                .toList();

        //이것도 name이냐 establishedDate 냐에 커서값 할당
        //전체는0부터하니 -1하고 마지막 컨탠츠의 아이디값
        String nextCursor = null;
        Long nextIdAfter = null;
        if (!page.getContent().isEmpty()) {
            Department last = page.getContent().get(page.getContent().size() - 1);
            nextCursor = "name".equals(field) ? last.getName() : last.getEstablishedDate().toString();
            nextIdAfter = last.getId();
        }

        //진짜 요구에맞게 10개 -10개면 size인데 실제 조회면 getNumberOfElements()

        return  pageResponseMapper.toPageResponseDto(page, nextCursor, nextIdAfter);
    }

    // 혹시모를 데이트파싱 수정가능
    private LocalDate parseDateOrNull(String date) {
        if (date == null || date.isBlank()) return null;
        return LocalDate.parse(date);
    }

}
