package com.codeit_team01.sb07_hrbank_team01.history.service;

import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import com.codeit_team01.sb07_hrbank_team01.history.entity.History;
import com.codeit_team01.sb07_hrbank_team01.history.entity.HistoryType;
import com.codeit_team01.sb07_hrbank_team01.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService{
    private final HistoryRepository historyRepository;

    // 직원 생성 이력 등록
    @Override
    public void createHistory(Employee employee, String memo, String ipAddress) {
        History history = History.createHistory(HistoryType.EMPLOYEE_CREATE, employee, memo, ipAddress);

        history.addDetail("입사일", "", employee.getHireDate().toString());
        history.addDetail("이름", "", employee.getName());
        history.addDetail("직함", "", employee.getJobPosition());
        history.addDetail("부서명", "", employee.getDepartment().toString());
        history.addDetail("이메일", "", employee.getEmail());
        history.addDetail("사번", "", employee.getEmployeeNo());
        history.addDetail("상태", "", employee.getStatus().toString());

        historyRepository.save(history);
    }

    // 직원 수정 이력 등록
    @Override
    public void createUpdateHistory(Employee beforeEmployee, Employee afterEmployee, String memo, String ipAddress) {
        History history = History.createHistory(HistoryType.EMPLOYEE_UPDATE, afterEmployee, memo, ipAddress);


    }

    // 직원 삭제 이력 등록
    @Override
    public void createDeleteHistory(Employee employee, String memo, String ipAddress) {

    }

    // 전체 조회
    @Override
    public List<History> getAllHistory() {
        return List.of();
    }

}
