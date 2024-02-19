package org.ohgiraffers.board.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest { //사용자한테 입력 받음. //CreatePostRequest 는 입력하는 란이라고 생각하면 됨. 그래서 거기는 postId 가 없음.

    private String title;
    private String content;

}
