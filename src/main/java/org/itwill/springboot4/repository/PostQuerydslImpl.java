package org.itwill.springboot4.repository;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.domain.Post;
import org.itwill.springboot4.domain.QPost;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Slf4j
public class PostQuerydslImpl extends QuerydslRepositorySupport implements PostQuerydsl {

    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     */
    public PostQuerydslImpl() {
        super(Post.class); // 엔터티 클래스를 알아야 우리가 만들고자 하는 Post 관련 메서드들을 만들 수 있음.
    }
    // 상위 클래스에는 기본 생성자가 없기 때문에 아규먼트를 갖는 생성자를 명시적으로 호출해야만 함
//    QuerydslRepositorySupport 생성자의 아규먼트는 도메인 클래스 (엔터티 클래스)

    @Override
    public Post searchById(Long id) {
//        id (PK)로 포스트 1개를 검색하는 기능
//        select p.* from posts p where p.id = ?
        log.info("searchById (id = {})", id);

        QPost post = QPost.post;

        JPQLQuery<Post> query = from(post);

        query.where(post.id.eq(id)); // select ... from Post p
        Post entity = query.fetchOne();

        return entity;
    }
}
