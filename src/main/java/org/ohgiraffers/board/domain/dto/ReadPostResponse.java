package org.ohgiraffers.board.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadPostResponse { //모든 필드의 값 넘겨줌.

    private Long postId;
    private String title;
    private String content;

}
