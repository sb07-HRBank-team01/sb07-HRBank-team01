package com.codeit_team01.sb07_hrbank_team01.backup.repository;

import com.codeit_team01.sb07_hrbank_team01.backup.dto.request.BackupRequestDto;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.Backup;
import org.springframework.data.domain.Page;

public interface BackupRepositoryCustom {
  Page<Backup> findBackupPage(BackupRequestDto cond);
}
