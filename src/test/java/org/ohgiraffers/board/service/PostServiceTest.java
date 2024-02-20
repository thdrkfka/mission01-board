package org.ohgiraffers.board.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ohgiraffers.board.domain.dto.*;
import org.ohgiraffers.board.domain.entity.Post;
import org.ohgiraffers.board.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    /* @Mock
     * 가짜 객체, 테스트 실행 시 실제가 아닌 Mock 객체를 반환한다.
     * */
    @Mock
    private PostRepository postRepository;

    /* @InjectMocks
     * Mock 객체가 주입될 클래스를 지정한다.
     * */
    @InjectMocks
    private PostService postService;

    private Post savedPost;
    private Post post;
    private CreatePostRequest createPostRequest;
    private UpdatePostRequest updatePostRequest;

    @BeforeEach
    void setup() {
        //초기화
        post = new Post(1L, "테스트 제목", "테스트 내용");
        savedPost = new Post(2L, "저장되어 있던 테스트 제목", "저장되어 있던 테스트 내용");
        createPostRequest = new CreatePostRequest("테스트 제목", "테스트 내용");
        updatePostRequest = new UpdatePostRequest("변경된 테스트 제목", "변경된 테스트 내용");
    }

    //give : 테스트 시 필요한 파라미터를 준비한다.
    //when : 테스할 메소드를 호출한다.
    //then : 실행 결과를 검증한다.

    @Test
    @DisplayName("게시글 작성 기능 테스트")
    void create_post_service() {

        //given
//        // mockito 기본 형태
//        when(postRepository.save(any())).thenReturn(post); // 작동은 아래 given 과 똑같음.//이름만 바뀐 거임.
        // BDDMockito 형태로
        given(postRepository.save(any())).willReturn(post); //controller 테스트 시 사용

        //when
        CreatePostResponse createPostResponse = postService.createPost(createPostRequest);

        //then
        assertThat(createPostResponse.getPostId()).isEqualTo(1L);
        assertThat(createPostResponse.getTitle()).isEqualTo("테스트 제목");
        assertThat(createPostResponse.getContent()).isEqualTo("테스트 내용");

    }

    @Test
    @DisplayName("postId로 게시글을 조회하는 기능 테스트")
    void read_post_service() {
        //given
        when(postRepository.findById(any())).thenReturn(Optional.of(savedPost));

        //when
        ReadPostResponse readPostResponse = postService.readPostById(savedPost.getPostId());

        //then
        assertThat(readPostResponse.getPostId()).isEqualTo(savedPost.getPostId());
        assertThat(readPostResponse.getTitle()).isEqualTo(savedPost.getTitle());
        assertThat(readPostResponse.getContent()).isEqualTo(savedPost.getContent());

    }
    @Test
    @DisplayName("postId로 게시글을 찾지 못했을 때, 지정한 Exception을 발생시키는지 테스트")
    void read_post_by_id_2() {
        //given//service에서 기능이 실행되었을 떄, 돌아오는 값이 빈 값일 때
        given(postRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThrows(EntityNotFoundException.class, () ->
                postService.readPostById(1L));
    }
    
    @Test
    @DisplayName("전체 게시글 조회 기능 테스트")
    void read_all_post() {
        //given
        Pageable pageable = PageRequest.of(0, 5);
        List<Post> posts = Arrays.asList(post, savedPost); //데이터 를 리스트로 만듬.

        Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());
        
        given(postRepository.findAll(pageable)).willReturn(postPage);
        
        //when
        Page<ReadPostResponse> responses = postService.readAllPost(pageable);
        
        //then
        assertThat(responses.getContent()).hasSize(2);
        assertThat(responses.getContent().get(0).getTitle()).isEqualTo("테스트 제목");
        assertThat(responses.getContent().get(0).getContent()).isEqualTo("테스트 내용");
        assertThat(responses.getContent().get(1).getTitle()).isEqualTo("저장되어 있던 테스트 제목");
        assertThat(responses.getContent().get(1).getContent()).isEqualTo("저장되어 있던 테스트 내용");

    }

    @Test
    @DisplayName("게시글 수정 기능 테스트")
    void update_post_service(){
        //given //service 부분 //postRepository에서 postId로 게시글을 찾을 때, 반환값이 Optional.of(savedPost) 이다. //Optional.of => value 값만!
        given(postRepository.findById(any())).willReturn(Optional.of(savedPost));

        //when //controller 부분 //service에서 postService.updatePost() 기능이 실행되었을 때, updatePostRequest(새로 수정한 입력값)을 updatePostResponse에 변경,저장
        UpdatePostResponse updatePostResponse = postService.updatePost(savedPost.getPostId(), updatePostRequest);

        //then//수정되고 저장된 값 확인 
        assertThat(updatePostResponse.getPostId()).isEqualTo(savedPost.getPostId());//postId 변했는지 확인
        assertThat(updatePostResponse.getTitle()).isEqualTo("변경된 테스트 제목");
        assertThat(updatePostResponse.getContent()).isEqualTo("변경된 테스트 내용");
    }
    
    @Test
    @DisplayName("postId로 게시글을 찾지 못했을 때, 지정한 Exception을 발생시키는지 테스트")
    void update_post_by_id() {
        //given
        given(postRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThrows(EntityNotFoundException.class, () ->
                postService.updatePost(2L, updatePostRequest));

    }
    
    @Test
    @DisplayName("게시글 삭제 기능 테스트")
    void delete_post_service() {
        //given
        given(postRepository.findById(any())).willReturn(Optional.of(savedPost));

        //when
        DeletePostResponse deletePostResponse = postService.deletePost(savedPost.getPostId());

        //then
        assertThat(deletePostResponse.getPostId()).isEqualTo(2L);

    }
    
}
