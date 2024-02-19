package org.ohgiraffers.board.controller;

/* 레이어드 아키텍쳐
 * 소프트웨어를 여러개의 계층으로 분리해서 설계하는 방법
 * 각 계층이 독립적으로 구성되서, 한 계층이 변경이 일어나도, 다른 계층에 영향을 주지 않음.
 * 따라서 코드의 재사용성과 유지보수성을 높일 수 있음. */

import lombok.RequiredArgsConstructor;
import org.ohgiraffers.board.domain.dto.CreatePostRequest;
import org.ohgiraffers.board.domain.dto.CreatePostResponse;
import org.ohgiraffers.board.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* Controller RestController
 * Controller : 화면 반환
 *            : 주로 화면 View 를 반환하기 위해( return 뒤에 화면 경로 정해줄 때) 사용한다.
 *            : 하지만 종종 Controller를 쓰면서도 데이터를 반환해야 할 때가 있는데, 이럴 때 사용하는 것이 @ResponseBody(api로 요청할 때 받아야할 값, 데이터 반환할 때 사용)
 * RestController : @Controller + @ResponseBody : 화면 + 데이터 반환
 *
 * REST 란?
 * Representational Astate Transfer 의 약자
 * 자원을 이름으로 구분하여 자원의 상태를 주고 받는 것을 의미한다.
 * REST 특징 : 기본적으로 웹의 기존 기술과 HTTP 프로토콜을 그대로 사용하기 때문에,
 * 웹의 장점을 최대한 활용 할 수 있는 아키텍쳐 스타일이다.*/

@RestController
// @RequestMapping : 특정 URL을 매핑하게 도와준다.
@RequestMapping("/api/v1/posts") //기본 경로
// @RequiredArgsConstructor : final 혹은 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성해준다.
@RequiredArgsConstructor
public class PostController { //controller 는 service 로 연결됨.

    private final PostService postService;

    @PostMapping                                        //return 받을 애
    public ResponseEntity<CreatePostResponse> postCreate(@RequestBody CreatePostRequest request) {

        CreatePostResponse response = postService.createPost(request);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
