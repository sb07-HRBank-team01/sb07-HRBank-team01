package com.codeit_team01.sb07_hrbank_team01.history.controller;

import com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto.HistoryChangeLogDto;
import com.codeit_team01.sb07_hrbank_team01.history.dto.responseDto.HistoryDiffDto;
import com.codeit_team01.sb07_hrbank_team01.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/histories") ///api/change-logs
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    // GET 직원 정보 수정 이력 목록 조회
    @GetMapping
    public ResponseEntity<List<HistoryChangeLogDto>> getAllHistories() {
        //service에서 이력 조회 후 DTO로 변환
        List<HistoryChangeLogDto> histories = historyService.getAllHistories();
        return ResponseEntity.ok(histories);
    }

    // GET, /{id}/diffs -> /{historyId}/details 직원 정보 수정 이력 상세 조회
    @GetMapping("/{historyId}/details")
    public ResponseEntity<List<HistoryDiffDto>> getHistoryDetail(@PathVariable("historyId") Long historyId) {
        //service에서 이력 ID로 상세 조회 후 DiffDto 리스트 반환
        List<HistoryDiffDto> histories = historyService.getHistoryDetail(historyId);
        return ResponseEntity.ok(histories);
    }

    // GET, /count -> 수정 이력 건수 조회

}
