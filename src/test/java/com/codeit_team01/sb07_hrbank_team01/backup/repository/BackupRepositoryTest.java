package com.codeit_team01.sb07_hrbank_team01.backup.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codeit_team01.sb07_hrbank_team01.backup.dto.request.BackupRequestDto;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.Backup;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.BackupStatus;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

@DataJpaTest // JPA TEST, Transactional 내장
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestQuerydslConfig.class) // Querydsl 설정 임포트
class BackupRepositoryTest {

  @Autowired
  private BackupRepository backupRepository;

  @BeforeEach
  void setup() {
    // 테스트 데이터 셋업 (충분한 데이터가 필요합니다.)
    // status: FAILED, COMPLETED, IN_PROGRESS 순으로 정렬되도록 데이터 삽입 가정
    for (int i = 0; i < 5; i++) {
      backupRepository.save(Backup.builder()
          .worker("worker" + i)
          .startTime(Instant.now().minusSeconds(50 - i * 10))
          .endTime(Instant.now().minusSeconds(50 - i * 10))
          .status(BackupStatus.COMPLETED).build());

      backupRepository.save(Backup.builder()
          .worker("worker" + i)
          .startTime(Instant.now().minusSeconds(100 - i * 10))
          .endTime(Instant.now().minusSeconds(100 - i * 10))
          .status(BackupStatus.FAILED).build());

      backupRepository.save(Backup.builder()
          .worker("worker" + i)
          .startTime(Instant.now().minusSeconds(1500 - i * 10))
          .endTime(Instant.now().minusSeconds(1500 - i * 10))
          .status(BackupStatus.SKIPPED).build());
    }
  }

  @Test
  @DisplayName("모든 데이터 조회 및 출력")
  void given_none_when_findAll_then_Success() {
    // given
    System.out.println("--- Repository 테스트 시작 ---");

    // when: 모든 데이터를 조회하고 출력합니다.
    backupRepository.findAll().forEach(b -> {
      // b 객체의 필드를 출력 (Backup 엔티티에 toString()이 구현되어 있어야 합니다.)
      System.out.println("Backup Data: " + b.toString());
    });

    // then: 현재는 출력 확인이 목표이므로 별도의 검증 코드는 생략합니다.
    System.out.println("--- Repository 테스트 종료 ---");
  }

  @Test
  @DisplayName("가장 최근 데이터 조회 테스트 - SUCCESS")
  void given_latestData_When_findLatestData_then_equalsGivenData() {
    // given
    Backup backup = Backup.builder()
        .worker("system_latest")
        .startTime(Instant.now())
        .endTime(Instant.now())
        .status(BackupStatus.IN_PROGRESS)
        .metaFile(null)
        .build();

    backupRepository.save(backup);

    // when
    Backup firstByOrderByEndTimeDesc = backupRepository.findFirstByOrderByEndTimeDesc()
        .orElse(null);

    // then
    // Instant.now()로 최신 데이터를 갱신했기 때문에 같아야함.
    assertEquals(firstByOrderByEndTimeDesc, backup);
  }

  @Test
  @DisplayName("QueryDsl: 커서 기반 페이지네이션 (status DESC 정렬 검증)")
  void given_dataWithCursors_when_findBackupPage_withDescCursor_then_returnsNextPage() {
    // given
    // 1. 첫 번째 페이지 조회 조건 (status DESC 정렬을 검증)
    long count = backupRepository.count();
    System.out.println("count = " + count);

    BackupRequestDto firstCond = BackupRequestDto.builder() // 테스트 시 Builder필요
        .size(5) // Long 타입으로 수정
        .sortField("status")
        .sortDirection("desc")
        .build();

    // -------------------------------------------------------------------
    // ✅ 1차 쿼리 조건 출력
    System.out.println("\n--- 1. First Page Query (ID=null, Cursor=null) ---");
    System.out.println("✅ Condition (firstCond): " + firstCond.toString());
    // -------------------------------------------------------------------

    // when: 첫 번째 페이지 조회
    Page<Backup> firstPage = backupRepository.findBackupPage(firstCond);

    // -------------------------------------------------------------------
    // ✅ 1차 쿼리 결과 출력
    System.out.println("➡️ Results (" + firstPage.getContent().size() + " rows):");
    firstPage.getContent().forEach(b -> System.out.println("-> " + b.toString()));
    System.out.println("---------------------------------------------------");
    // -------------------------------------------------------------------

    // 2. 커서 설정 (첫 페이지의 마지막 항목)
    if (firstPage.getContent().isEmpty() || !firstPage.hasNext()) {
      System.err.println("다음 페이지가 없거나 데이터가 부족하여 커서 테스트를 계속할 수 없습니다.");
      return;
    }

    Backup lastItem = firstPage.getContent().get(firstPage.getContent().size() - 1);

    // 3. 다음 페이지 조회 조건 설정 (커서 조건 추가)
    BackupRequestDto secondCond = BackupRequestDto.builder()
        .size(5)
        .sortField("status")
        .cursor(lastItem.getStatus().name()) // 커서 필드 값 (Enum의 String 이름)
        .idAfter(lastItem.getId())// 보조 정렬 필드 값
        .sortDirection("desc")
        .build();

    // -------------------------------------------------------------------
    System.out.println("\n--- 2. Second Page Query (Cursor Applied) ---");
    System.out.println("✅ Condition (secondCond): " + secondCond.toString());
    // -------------------------------------------------------------------

    // when: 두 번째 페이지 조회
    Page<Backup> secondPage = backupRepository.findBackupPage(secondCond);

    // then (데이터 확인 및 검증)
    // -------------------------------------------------------------------
    System.out.println("➡️ Results (" + secondPage.getContent().size() + " rows):");
    secondPage.getContent().forEach(b -> System.out.println("-> " + b.toString()));
    System.out.println("---------------------------------------------------");

    // 5. 첫 번째 페이지의 데이터가 두 번째 페이지에 포함되지 않았는지 검증
    assertFalse(secondPage.getContent().contains(lastItem),
        "다음 페이지는 이전 페이지의 마지막 항목을 포함하면 안 됩니다 (커서 누락).");

    if (!secondPage.getContent().isEmpty()) {
      Backup nextItem = secondPage.getContent().get(0);

      // 6. ✅ 동적 커서 조건 검증 (핵심 로직)
      // 두 번째 페이지의 첫 번째 항목이 첫 번째 페이지의 마지막 항목이 만든 커서 조건을 따르는지 검증
      assertTrue(isNextItemFollowsCursor(nextItem, lastItem, secondCond.sortField(),
              secondCond.sortDirection()),
          "'status' DESC 정렬에서 다음 항목은 커서 조건을 만족해야 합니다.");
    }
  }

  /**
   * 다음 항목이 커서 조건을 만족하는지 동적으로 검증하는 헬퍼 함수
   */
  private boolean isNextItemFollowsCursor(
      Backup nextItem,
      Backup lastItem,
      String sortField,
      String sortDirection) {
    // 정렬 방향 (DESC 또는 ASC)
    boolean isDesc = "desc".equalsIgnoreCase(sortDirection);

    // 기본 보조 조건: 주 필드가 같을 때 ID 비교
    Long cursorId = lastItem.getId();
    boolean followsIdCondition = isDesc
        ? nextItem.getId() < cursorId // DESC: ID가 작아야 함
        : nextItem.getId() > cursorId; // ASC: ID가 커야 함

    // ----------------------------------------------------
    // 1. 상태(status) 정렬 검증 (문자열/Enum 타입)
    // ----------------------------------------------------
    if ("status".equalsIgnoreCase(sortField)) {
      String nextStatus = nextItem.getStatus().name();
      String cursorStatus = lastItem.getStatus().name();
      int comparison = nextStatus.compareTo(cursorStatus); // 문자열 사전 순 비교

      // 주 필드 조건: 다음 항목의 상태가 커서 상태보다 사전순으로 작은지(DESC) 또는 큰지(ASC)
      boolean followsMainCondition = isDesc
          ? (comparison < 0) // DESC: 다음 항목이 사전순으로 더 작아야 함 (예: COMPLETED -> FAILED)
          : (comparison > 0); // ASC: 다음 항목이 사전순으로 더 커야 함

      // 복합 조건: (주 필드 조건 만족) OR (주 필드 값이 같고 ID 조건 만족)
      return followsMainCondition || (comparison == 0 && followsIdCondition);
    }

    // ----------------------------------------------------
    // 2. 시간(startedAt/endedAt) 정렬 검증 (Instant 타입)
    // ----------------------------------------------------
    else if ("startedAt".equalsIgnoreCase(sortField)) {
      Instant nextTime = nextItem.getStartTime();
      Instant cursorTime = lastItem.getStartTime();

      // 주 필드 조건: 다음 항목의 시간이 커서 시간보다 이전(작다)인지 또는 이후(크다)인지
      boolean followsMainCondition = isDesc
          ? nextTime.isBefore(cursorTime) // DESC: 시간이 이전이어야 함
          : nextTime.isAfter(cursorTime);  // ASC: 시간이 이후여야 함

      // 복합 조건: (주 필드 조건 만족) OR (주 필드 값이 같고 ID 조건 만족)
      return followsMainCondition || (nextTime.equals(cursorTime) && followsIdCondition);
    }
    // ... (추가 정렬 필드에 대한 조건은 여기에 추가)

    // 지원하지 않는 정렬 필드
    return false;
  }
}