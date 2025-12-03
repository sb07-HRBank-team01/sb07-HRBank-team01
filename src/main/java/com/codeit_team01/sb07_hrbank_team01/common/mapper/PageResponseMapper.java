package com.codeit_team01.sb07_hrbank_team01.common.mapper;

import com.codeit_team01.sb07_hrbank_team01.common.dto.response.PageResponseDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

  // 구현 거의 맞습니다.
  // nextCursor는 도메인마다 목록조회에서 사용하는 커서가 달라서, Object로 구현했습니다.
  default <T> PageResponseDto<T> toPageResponseDto(Page<T> page, Object nextCursor, Long nextIdAfter) {
    return new PageResponseDto<>(
        page.getContent(),
        nextCursor,
        nextIdAfter,
        page.getSize(),
        page.getTotalElements(),
        page.hasNext()

    );
  }


}