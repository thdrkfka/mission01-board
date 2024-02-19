package org.ohgiraffers.board.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest { //업데이트 해서 바뀌는 애들 입력 받음.

    private String title;
    private String content;

}
