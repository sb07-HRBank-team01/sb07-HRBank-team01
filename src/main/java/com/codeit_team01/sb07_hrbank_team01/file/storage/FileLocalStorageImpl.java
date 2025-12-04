package com.codeit_team01.sb07_hrbank_team01.file.storage;

import com.codeit_team01.sb07_hrbank_team01.common.exception.CustomException;
import com.codeit_team01.sb07_hrbank_team01.common.exception.ErrorCode;
import com.codeit_team01.sb07_hrbank_team01.file.dto.FileResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "HRBank.storage.type", havingValue = "local")
public class FileLocalStorageImpl implements FileLocalStorage {

    @Value("${HRBank.storage.local.root-path}")
    private String filePath;

    private Path rootPath;

    @PostConstruct
    void init() {
        this.rootPath = Paths.get(filePath).toAbsolutePath();

        if (!Files.exists(this.rootPath)) {
            try {
                Files.createDirectories(this.rootPath);
            } catch (IOException e) {
                throw new CustomException(ErrorCode.DIRECTORY_CREATION_FAILED, e.getMessage());
            }
        }
    }

    private Path resolvePath(Long id) {
        return this.rootPath.resolve(id.toString());
    }

    @Override
    public void put(Long id, byte[] bytes) {
        Path path = resolvePath(id);

        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_OPERATION_FAILED, "파일 저장에 실패했습니다.");
        }
    }

    public InputStream get(Long id) {
        Path path = resolvePath(id);

        try {
            if (!Files.exists(path)) {
                throw new CustomException(ErrorCode.FILE_NOT_FOUND, id + "번 파일이 존재하지 않습니다.");
            }
            return new FileInputStream(path.toFile());
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_OPERATION_FAILED, "파일 읽기에 실패했습니다.");
        }
    }

    public ResponseEntity<Resource> download(FileResponseDto fileResponseDto) {
        InputStream fileInputStream = get(fileResponseDto.id());
        InputStreamResource resource = new InputStreamResource(fileInputStream);
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(fileResponseDto.name(), StandardCharsets.UTF_8)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        headers.setContentLength(fileResponseDto.size());
        headers.setContentType(MediaType.parseMediaType(fileResponseDto.type()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
