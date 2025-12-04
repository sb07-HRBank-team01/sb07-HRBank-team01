package com.codeit_team01.sb07_hrbank_team01.backup.repository;

import com.codeit_team01.sb07_hrbank_team01.backup.entity.Backup;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.BackupStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRepository extends JpaRepository<Backup, Long>, BackupRepositoryCustom {

  // EndTime으로 내림차순 정렬 중 가장 첫번째 값
  Optional<Backup> findFirstByOrderByEndTimeDesc();

  // EndTime으로 내림차순 정렬 중
  // 해당 status를 만족하는 가장 첫번째 값
  Optional<Backup> findFirstByStatusOrderByEndTimeDesc(BackupStatus status);

}
