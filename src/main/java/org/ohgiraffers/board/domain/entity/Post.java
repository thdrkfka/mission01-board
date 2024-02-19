package org.ohgiraffers.board.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

/* Entity
 * 실제 세계의 객체나 개념을 소프트웨어 내에서 모델링 한 것으로, 데이터 베이스의 테이블에 해당하는 클래스
 * Entity 에서는 setter 사용 지양하는 이유
 * : 데이터의 일관성을 위해 setter 사용 지양
 * : setter(객체 안에 있는 애를 바꾸는 애) 를 사용하면, entity를 어디서든 수정할 수 있으니까
 *   데이터베이스나 이런데에 저장한 정보들이 달라질 수 있음. 일관성 유지가 어렵다. */

/* @Data
 * -> getter, setter, toString, equalsandhashcode .. 이것들이 묶여있어 쓰이는 애들 */

/* @Entity
 * : JPA Entity라는 것을 의미한다. Id와 @GeneratedValue를 이용해 고유 식별자를 정의한다.*/
@Entity
/* @Builder : 빌더 디자인 패턴을 자동으로 생성해준다.*/
@Builder
/* @Getter : 클래스의 모든 필드에 대한 getter 메소드를 자동으로 생성해준다.*/
@Getter
/* @AllArgsConstructor : 클래스의 모든 필드를 매개변수로 받는 전체 생성자를 자동 생성한다.*/
@AllArgsConstructor
/* @NoArgsConstructor : 매개 변수가 없는 기본 생성자를 생성한다.
 * accessLevel을 통해 접근 수준을 설정할 수 있다.*/
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue
    private Long postId;

    private String title;
    private String content;

}

