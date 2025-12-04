package com.codeit_team01.sb07_hrbank_team01.backup.entity;

import com.codeit_team01.sb07_hrbank_team01.base.BaseEntity;
import com.codeit_team01.sb07_hrbank_team01.file.entity.MetaFile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "backups")
public class Backup extends BaseEntity {
  @Column(name = "worker", nullable = false, length = 50)
  private String worker;

  @Column(name = "start_time", nullable = false)
  private Instant startTime;

  @Column(name = "end_time", nullable = false)
  private Instant endTime;

  @Column(name = "status", nullable = false, length = 30)
  @Enumerated(EnumType.STRING)
  private BackupStatus status;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id")
  private MetaFile metaFile;


}
