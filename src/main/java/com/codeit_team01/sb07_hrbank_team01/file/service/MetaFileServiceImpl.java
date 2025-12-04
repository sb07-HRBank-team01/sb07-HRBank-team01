package com.codeit_team01.sb07_hrbank_team01.file.service;

import com.codeit_team01.sb07_hrbank_team01.common.exception.CustomException;
import com.codeit_team01.sb07_hrbank_team01.common.exception.ErrorCode;
import com.codeit_team01.sb07_hrbank_team01.file.dto.FileCreateRequestDto;
import com.codeit_team01.sb07_hrbank_team01.file.dto.FileResponseDto;
import com.codeit_team01.sb07_hrbank_team01.file.entity.MetaFile;
import com.codeit_team01.sb07_hrbank_team01.file.mapper.MetaFileMapper;
import com.codeit_team01.sb07_hrbank_team01.file.repository.MetaFileRepository;
import com.codeit_team01.sb07_hrbank_team01.file.storage.FileLocalStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MetaFileServiceImpl implements MetaFileService {

    private final MetaFileRepository metaFileRepository;
    private final FileLocalStorage fileLocalStorage;
    private final MetaFileMapper metaFileMapper;

    @Override
    @Transactional
    public FileResponseDto createFile(FileCreateRequestDto fileCreateRequestDto) {
        MetaFile metaFile = MetaFile.builder()
                .name(fileCreateRequestDto.name())
                .type(fileCreateRequestDto.type())
                .size((long) fileCreateRequestDto.bytes().length)
                .build();
        metaFileRepository.save(metaFile);
        fileLocalStorage.put(metaFile.getId(), fileCreateRequestDto.bytes());
        return metaFileMapper.toDto(metaFile);
    }

    @Override
    @Transactional(readOnly = true)
    public FileResponseDto findById(long id) {
        MetaFile metaFile = metaFileRepository.findById(id)
                .orElseThrow(() ->
                        new CustomException(ErrorCode.FILE_NOT_FOUND,
                        id + "번 파일이 존재하지 않습니다."));
        return metaFileMapper.toDto(metaFile);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        metaFileRepository.deleteById(id);
    }
}
