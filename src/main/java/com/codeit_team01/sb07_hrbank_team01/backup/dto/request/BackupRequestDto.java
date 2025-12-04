package com.codeit_team01.sb07_hrbank_team01.backup.dto.request;

import com.codeit_team01.sb07_hrbank_team01.backup.entity.BackupStatus;
import java.time.Instant;

/**
 *
 * @param worker        작업자
 * @param status        상태 (IN_PROGRESS, COMPLETED, FAILED)
 * @param startedAtFrom 시작 시간(부터)
 * @param startedAtTo   시작 시간(까지)
 * @param idAfter       이전 페이지 마지막 요소 ID
 * @param cursor        커서 (이전 페이지의 마지막 ID)
 * @param size          페이지 크기 Default value : 10
 * @param sortField     정렬 필드 (startedAt, endedAt, status) Default value : startedAt
 * @param sortDirection 정렬 방향 (ASC, DESC) Default value : DESC
 */
//@Builder
// 테스트 코드 사용 시 Builder 필요
public record BackupRequestDto(
    String worker,
    BackupStatus status,
    Instant startedAtFrom,
    Instant startedAtTo,
    Long idAfter,
    String cursor,
    Integer size,
    String sortField,
    String sortDirection

) {

}