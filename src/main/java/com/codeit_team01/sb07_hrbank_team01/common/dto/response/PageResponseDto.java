package com.codeit_team01.sb07_hrbank_team01.common.dto.response;

import java.util.List;

public record PageResponseDto<T>(
    List<T> content,
    Object nextCursor,
    Long nextIdAfter,
    int size,
    Long totalElements,
    boolean hasNext
) {

}