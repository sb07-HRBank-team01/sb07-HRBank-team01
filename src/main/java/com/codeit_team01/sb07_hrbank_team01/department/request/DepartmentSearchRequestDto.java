package com.codeit_team01.sb07_hrbank_team01.department.request;



import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public record DepartmentSearchRequestDto(

        String nameOrDescription,

        @Positive(message = "idAfter는 1 이상이어야 합니다.")
        Long idAfter,

        String cursor,

        @Positive(message = "size는 1 이상이어야 합니다.")
        Integer size,


        @Pattern(regexp = "name|establishedDate", message = "정렬 필드는 name 또는 establishedDate만 가능합니다.")
        String sortField,


        @Pattern(regexp = "asc|desc", message = "정렬 방향은 asc 또는 desc만 가능합니다.")
        String sortDirection
) {
    //기본값
    //디비에서 is null 트루면 전체조회라 공백은 모두 null로 전체조회
    public DepartmentSearchRequestDto {
        if (nameOrDescription != null && nameOrDescription.isBlank()) nameOrDescription = null;
        if (size == null || size <= 0) size = 10;
        if (sortField == null || sortField.isBlank()) sortField = "name";
        if (sortDirection == null || sortDirection.isBlank()) sortDirection = "asc";


        boolean cursorCheck;

        switch (sortField) {
            case "name":
                cursorCheck = true; // 문자열은 그냥 허용
                break;

            case "establishedDate":
                try {
                    LocalDate.parse(cursor);
                    cursorCheck = true;  // 정상적인 yyyy-MM-dd 형식 이면 트루
                } catch (DateTimeParseException e) {
                    cursorCheck = false; // 형식이 틀림
                }
                break;

            default:
                cursorCheck = false; // 위에서 거르긴했는데 혹시모르니
                break;
        }

        if (!cursorCheck) {
            cursor = null; // 위에서 안걸리고 이상하다 null로 전체
        }
    }
}