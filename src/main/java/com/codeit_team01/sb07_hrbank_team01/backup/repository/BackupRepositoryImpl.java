package com.codeit_team01.sb07_hrbank_team01.backup.repository;

import com.codeit_team01.sb07_hrbank_team01.backup.dto.request.BackupRequestDto;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.Backup;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.BackupStatus;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.QBackup;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BackupRepositoryImpl implements BackupRepositoryCustom {

  private final JPAQueryFactory query;
  private final QBackup backup = QBackup.backup; // QBackup 인스턴스를 필드로 정의 (Optional)

  @Override
  public Page<Backup> findBackupPage(BackupRequestDto cond) {

    // 1. 주 정렬 조건 (status, startedAt, endedAt)
    OrderSpecifier<?> mainSort = createMainOrderSpecifier(cond.sortField(), cond.sortDirection());

    // 2. ID 보조 정렬 조건 (주 정렬 방향과 일치해야 함)
    boolean isDesc = "desc".equalsIgnoreCase(cond.sortDirection());
    OrderSpecifier<?> idSort = isDesc ? backup.id.desc() : backup.id.asc();

    // 3. 커서 조건
    BooleanExpression cursorCondition = cursorCondition(
        cond.cursor(),
        cond.idAfter(),
        cond.sortDirection(),
        cond.sortField()
    );

    // 쿼리
    List<Backup> contents = query.selectFrom(backup)
        .where(
            workerContains(cond.worker()),
            statusEq(cond.status()),
            createdAtBetween(cond.startedAtFrom(), cond.startedAtTo()),
            cursorCondition
        )
        .orderBy(mainSort, idSort)
        .limit(cond.size())
        .fetch(); // fetch: limit처럼 원하는 행의 개수 설정하는 옵션

    // 전체 건수
    Long total = query.select(backup.count())
        .from(backup)
        .where(
            workerContains(cond.worker()),
            statusEq(cond.status()),
            createdAtBetween(cond.startedAtFrom(), cond.startedAtTo())
        )
        .fetchOne();

    // PageImpl 생성자를 위해 Pageable 객체 생성
    Pageable pageable = PageRequest.of(0, cond.size().intValue());

    // 6. PageImpl 반환
    return new PageImpl<>(contents, pageable, total == null ? 0L : total);
  }


  private BooleanExpression workerContains(String worker) {
    return worker != null ? backup.worker.contains(worker) : null;
  }

  private BooleanExpression statusEq(BackupStatus status) {
    return status != null ? backup.status.eq(status) : null;
  }

  private BooleanExpression createdAtBetween(Instant start, Instant end) {
    if (start != null && end != null) {
      return backup.startTime.between(start, end);
    }
    if (start != null) {
      return backup.startTime.goe(start);
    }
    if (end != null) {
      return backup.startTime.loe(end);
    }
    return null;
  }

  private BooleanExpression cursorCondition(
      String cursor,
      Long idAfter,
      String sortDirection,
      String sortField
  ) {

    if (cursor == null || idAfter == null) {
      return null;
    }

    boolean isDesc = "desc".equalsIgnoreCase(sortDirection);

    // 1. 상태(status) 필드로 정렬하는 경우 (String/Enum 타입)
    if ("status".equalsIgnoreCase(sortField)) {
      String cursorString = cursor;

      if (isDesc) {
        // DESC: status < cursor OR (status = cursor AND id < idAfter)
        return backup.status.stringValue().lt(cursorString)
            .or(backup.status.stringValue().eq(cursorString).and(backup.id.lt(idAfter)));
      } else {
        // ASC: status > cursor OR (status = cursor AND id > idAfter)
        return backup.status.stringValue().gt(cursorString)
            .or(backup.status.stringValue().eq(cursorString).and(backup.id.gt(idAfter)));
      }

    }
    // 2. 시간(Instant) 필드로 정렬하는 경우
    else if ("startedAt".equalsIgnoreCase(sortField) || "endedAt".equalsIgnoreCase(sortField)) {
      Instant cursorInstant;
      try {
        cursorInstant = Instant.parse(cursor);
      } catch (Exception e) {
        log.error("Instant 파싱이 잘못되었음.");
        return null;
      }

      // 동적으로 필드 선택
      ComparableExpression<Instant> timeField = "startedAt".equalsIgnoreCase(sortField)
          ? backup.startTime : backup.endTime;

      if (isDesc) {
        // DESC: timeField < cursor OR (timeField = cursor AND id < idAfter)
        return timeField.lt(cursorInstant)
            .or(timeField.eq(cursorInstant).and(backup.id.lt(idAfter)));
      } else {
        // ASC: timeField > cursor OR (timeField = cursor AND id > idAfter)
        return timeField.gt(cursorInstant)
            .or(timeField.eq(cursorInstant).and(backup.id.gt(idAfter)));
      }
    }

    return null;
  }

  private OrderSpecifier<?> createMainOrderSpecifier(String sortField, String sortDirection) {
    if (sortField == null) return backup.id.desc(); // 기본 정렬 (ID DESC)
    boolean isDesc = "desc".equalsIgnoreCase(sortDirection);

    return switch (sortField) {
      case "status" -> isDesc ? backup.status.desc() : backup.status.asc();
      case "startedAt" -> isDesc ? backup.startTime.desc() : backup.startTime.asc();
      case "endedAt" -> isDesc ? backup.endTime.desc() : backup.endTime.asc();
      default -> backup.id.desc();
    };
  }
}