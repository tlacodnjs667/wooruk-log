package org.itwill.springboot4.repository;

// 메서드의 이름은 뭔지 파라미터를 어떻게 전달해야 하는 것인지
// JPA 형식과는 별개의 라이브러리임

import org.itwill.springboot4.domain.Post;
import org.springframework.data.querydsl.binding.QuerydslPredicate;

@QuerydslPredicate
public interface PostQuerydsl {

    Post searchById (Long id);


}
