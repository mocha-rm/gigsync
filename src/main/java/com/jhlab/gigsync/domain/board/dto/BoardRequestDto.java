package com.jhlab.gigsync.domain.board.dto;

import com.jhlab.gigsync.domain.board.type.BoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BoardRequestDto {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 50, message = "제목은 50자까지 작성할 수 있습니다.")
    private final String title;

    private final String text;

    @NotNull(message = "카테고리를 설정해주세요.")
    private final BoardType boardType;

    private final List<Long> deleteFileIds;
}
