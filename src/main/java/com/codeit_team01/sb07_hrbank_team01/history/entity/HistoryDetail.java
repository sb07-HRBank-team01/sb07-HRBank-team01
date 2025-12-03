package com.codeit_team01.sb07_hrbank_team01.history.entity;

import com.codeit_team01.sb07_hrbank_team01.base.BaseEntity;
import com.codeit_team01.sb07_hrbank_team01.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

@Entity
@Table(name = "history_details")
public class HistoryDetail extends BaseEntity {

    // id, 생성 시간은 base에

    // 변경 상세 내용
    @Column(name = "property_name", length = 100, nullable = false)
    private String propertyName;

    // 변경 전
    @Column(name = "before_value", length = 255)
   private String beforeValue;

    // 변경 후
    @Column(name = "after_value", length = 255)
    private String afterValue;

    // history_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id", nullable = false)
    private History history;

    // 정적 팩토리 : entity에 setter쓰기 싫다니까 이거 추천해줌
    public static HistoryDetail createDetail(String propertyName, String beforeValue, String afterValue, History history) {
        HistoryDetail detail = new HistoryDetail();
        detail.propertyName = propertyName;
        detail.beforeValue = beforeValue != null ? beforeValue : "";
        detail.afterValue = afterValue != null ? afterValue : "";
        detail.history = history; // 연관관계 설정
        return detail;
    }
}
