package com.codeit_team01.sb07_hrbank_team01.history.entity;

import com.codeit_team01.sb07_hrbank_team01.base.BaseEntity;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

@Entity
@Table(name = "employee_histories")
public class History extends BaseEntity {

    // 수정 이력 Id, 이력 등록 시간은 base에

    // 수정 유형
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private HistoryType type;

    // 선택적으로 화면에서 입력
    @Column(length = 255)
    private String memo;

    // ip_address : 서버 자동 추출
    @Column(name = "ip_address", length = 255)
    private String ipAddress;

    // 직원 사번
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // 변경 상세 목록 (1:N)
    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<HistoryDetail> details = new ArrayList<>();

    public static History createHistory(HistoryType type, Employee employee, String memo, String ipAddress) {
        History history = new History();
        history.type = type;
        history.employee = employee;
        history.memo = memo != null ? memo : defaultMemo(type);
        history.ipAddress = ipAddress;
        return history;
    }

    public void addDetail(String propertyName, String beforeValue, String afterValue) {
        HistoryDetail detail = HistoryDetail.createDetail(propertyName, beforeValue, afterValue, this);
        this.details.add(detail);
    }

    private static String defaultMemo(HistoryType type) {
        // 향상된 switch문
        return switch (type) {
            case EMPLOYEE_CREATE -> "신규 직원 등록";
            case EMPLOYEE_UPDATE -> "직원 정보 수정";
            case EMPLOYEE_DELETE -> "직원 삭제";
        };
    }
}
