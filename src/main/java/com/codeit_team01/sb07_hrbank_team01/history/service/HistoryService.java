package com.codeit_team01.sb07_hrbank_team01.history.service;

import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import com.codeit_team01.sb07_hrbank_team01.history.entity.History;

import java.util.List;

public interface HistoryService {

    // 직원 생성 이력 등록
    void createHistory(Employee employee, String memo, String ipAddress);

    // 직원 수정 이력 등록 : 퇴사 포함
    void createUpdateHistory(Employee beforeEmployee, Employee afterEmployee, String memo, String ipAddress);

    // 직원 삭제 이력 등록
    void createDeleteHistory(Employee employee, String memo, String ipAddress);

    // 이력 조회
    List<History> getAllHistory();

//    List<History> getHistoryByEmployee(Employee employee);
}
