package com.codeit_team01.sb07_hrbank_team01.file.service;

import com.codeit_team01.sb07_hrbank_team01.file.dto.FileCreateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.file.dto.FileResponseDto;

public interface MetaFileService {
    FileResponseDto createFile(FileCreateRequestDto fileCreateRequestDto);
    FileResponseDto findById(long id);
    void deleteById(long id);
}
