package org.ohgiraffers.board.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostResponse {//title, content 를 받고 보여줌.

    private Long postId;
    private String title;
    private String content;

}
