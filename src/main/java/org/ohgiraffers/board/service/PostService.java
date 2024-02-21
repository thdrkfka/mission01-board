package org.ohgiraffers.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.ohgiraffers.board.domain.dto.*;
import org.ohgiraffers.board.domain.entity.Post;
import org.ohgiraffers.board.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/* Service 를 인터페이스와 구현체로 나누는 이유(기능이 추가와 변경이 많을 것 같은 프로젝트면 인터페이스와 클래스로 나누는 게 좋고, 그렇지 않으면 굳이 나누지 않고 사용)
 * 1. 다형성과 OCP원칙을 지키기 위해
 *    인터페이스와 구현체가 나누어지면, 구현체는 외부로부터 독립되어, 구현체의 수정이나 확장이 자유로워진다.
 * 2. 관습적인 추상화 방식
 *    과거, Spring 에서 AOP를 구혈 할 때, JDK Dynamic Proxy 를 사용했는데, 이때 인터페이스가 필수였다.
 *    지금은, CGLB를 기본적으로 포함하여 클래스 기반을 프록시 객체를 생성할 수 이게 되었다. */

/* @Transactional
 * 선언적으로 트랜젝션 관리는 가능하게 해준다.
 * 선언된 메소드가 실행되는 동안 모든 데이터베이스 연산을 하나의 트랜잭션으로 묶어 처리한다.
 * 이를통해, 메소드 내에서 데이터베이스 상태를 변경하는 작업들이 모두 성공적으로 완료되면 그 변경사항을 commit하고
 * 하나라도 실패하면 모든 변경사항을 rollback 시켜 관리한다.
 *
 * Transaction
 * 데이터베이스의 상태를 변화시키기 위해 수행하는 작업의 단위*/

@Service //서비스 계층 이란 걸 알려주는 어노테이션
//조회시에는 딱히 필요x // crud 작업시에는 필요(ex. 개인 - 돈 받는 로직-> 은행 // 개인이나 은행에서 둘 중 하나의 로직이라도 이상 있으면 그냥 rollback 해서 없던 일로 만듬)
@Transactional(readOnly = true)
@RequiredArgsConstructor//final 사용할 때, required 생성자 필요
public class PostService {//서비스는 repository랑 연결

    private final PostRepository postRepository;

    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request) {

        //데이터 저장
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        //입력한 데이터 저장
        Post savedPost = postRepository.save(post);

        return new CreatePostResponse(savedPost.getPostId(), savedPost.getTitle(), savedPost.getContent());
    }

    public ReadPostResponse readPostById(Long postId) { //조회만 할 것이니까 @Transactional 필요 x

        //예외 처리
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 postId로 조회된 게시글이 없습니다."));

        return new ReadPostResponse(foundPost.getPostId(), foundPost.getTitle(), foundPost.getContent());

    }

    @Transactional //데이터베이스의 상태 변경하니까 붙여줌.
    public UpdatePostResponse updatePost(Long postId, UpdatePostRequest request) {

        //id가 있는지 확인
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 postId로 조회된 게시글이 없습니다."));

        // Dirty Checking : DB에서 변경된 사항이 감지되면 자동으로 변경해줌.
        // update => PostRepository에서 DB의 변경된 사항이 감지되면 자동으로 변경
        foundPost.update(request.getTitle(), request.getContent());

        return new UpdatePostResponse(foundPost.getPostId(), foundPost.getTitle(), foundPost.getContent());

    }

    @Transactional
    public DeletePostResponse deletePost(Long postId) {

        //id가 있는지 확인
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 postId로 조회된 게시글이 없습니다."));

        postRepository.delete(foundPost);

        return new DeletePostResponse(foundPost.getPostId());

    }

    //list 조회
    public Page<ReadPostResponse> readAllPost(Pageable pageable) {

        //Page<T> : 페이지 정보를 담게 되는 인터페이스
        //Pageable : 페이지 처리에 필요한 정보를 담게 되는 인터페이스

        //Post 클래스를 타입으로 페이징처리,,
        Page<Post> postsPage = postRepository.findAll(pageable);

        //findAll(pageable) => 필드를 다 찾겠다.// 다 찾은 값들=postsPage를 post필드로 5개씩 묶을 거다.
        return postsPage.map(post -> new ReadPostResponse(post.getPostId(), post.getTitle(), post.getContent()));

    }

}
