package com.codeit_team01.sb07_hrbank_team01.backup.mapper;

import com.codeit_team01.sb07_hrbank_team01.backup.dto.response.BackupResponseDto;
import com.codeit_team01.sb07_hrbank_team01.backup.entity.Backup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BackupMapper {
  @Mapping(source = "startTime", target = "startedAt")
  @Mapping(source = "endTime", target = "endedAt")
  @Mapping(source = "file.id", target = "fileId")
  BackupResponseDto toDto(Backup backup);
}
