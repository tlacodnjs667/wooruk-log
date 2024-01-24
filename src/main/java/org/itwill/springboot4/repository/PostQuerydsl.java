package org.itwill.springboot4.repository;

// 메서드의 이름은 뭔지 파라미터를 어떻게 전달해야 하는 것인지
// JPA 형식과는 별개의 라이브러리임

import java.time.LocalDateTime;
import java.util.List;
import org.itwill.springboot4.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;

/**
 * Querydsl 라이브러리 :
 * 1. build.gradle에 의존성을 추가, 빌드 옵션 설정
 *      -> gradle tasks -> build 실행 -> refresh gradle project
 *      ->build/generated/querydsl/패키지 아래에 QDomain 클래스들이 생성
 * 2. 인터페이스 작성 - PostQuerydsl
 *      - Querydsl 라이브러리를 사용할 메서드 선언
 * 3. 인터페이스를 구현하는 클래스 작성 - PostQuerydslImpl
 *      - QuerydslRepositorySupport 클래스를 상속하고 인터페이스도 구현
 *      - QuerydslRepositorySupport 클래스는 기본 생성자가 없기 때문에, 반드시 아규먼트를 갖는 생성자를 명시적으로 호출해야 함
 *      - 인터페이스에서 선언된 메서드들을 구현
 * 4. JpaRepository를 상속하는 리포지토리 인트페이스에서 PostQuerydsl 인터페이스를 상속, 의존성 주입을 받아 Querydsl을 사용할 수 있게 됨
 *
 *
 */

@QuerydslPredicate
public interface PostQuerydsl {

    Post searchById (Long id);

    List<Post> searchByTitleOrContent (String keyword);

    List<Post> searchByModifiedTime (LocalDateTime from, LocalDateTime to);
    List<Post> searchByKeywordAndAuthor (String keyword, String author);
    List<Post> searchByKeyword (String... keyword);

    Page<Post> searchByKeywordWithPagination(Pageable pageable, String... keyword);
}
