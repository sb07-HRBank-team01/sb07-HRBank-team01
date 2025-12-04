package com.codeit_team01.sb07_hrbank_team01.file.mapper;

import com.codeit_team01.sb07_hrbank_team01.file.dto.FileResponseDto;
import com.codeit_team01.sb07_hrbank_team01.file.entity.MetaFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetaFileMapper {

    FileResponseDto toDto(MetaFile metaFile);
}
