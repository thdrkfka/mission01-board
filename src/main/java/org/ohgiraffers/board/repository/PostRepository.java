package org.ohgiraffers.board.repository;

import org.ohgiraffers.board.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
                                //extends JpaRepository<entity 클래스명, primary key값 타입>



}
