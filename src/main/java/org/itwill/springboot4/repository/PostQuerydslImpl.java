package org.itwill.springboot4.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.domain.Post;
import org.itwill.springboot4.domain.QPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Override
    public List<Post> searchByTitleOrContent(String keyword) {
        log.info("searchByTitleOrContent (keyword = {})", keyword);

        QPost post = QPost.post;
        JPQLQuery<Post> query = from(post);

        query.where(
            post.content.containsIgnoreCase(keyword).or(post.title.containsIgnoreCase(keyword)));
        query.orderBy(post.id.desc());

        return query.fetch();
    }

    @Override
    public List<Post> searchByModifiedTime(LocalDateTime from, LocalDateTime to) {
        log.info("searchByModifiedTime (from = {}, to = {})", from, to);

        QPost post = QPost.post;
        JPQLQuery<Post> query = from(post);
//        query.where(post.modifiedTime.after(from).or(post.modifiedTime.before(to)));
        query.where(post.modifiedTime.between(from, to));
        query.orderBy(post.id.desc());

        return query.fetch();
    }

    @Override
    public List<Post> searchByKeywordAndAuthor(String keyword, String author) {
        QPost post = QPost.post;

        JPQLQuery<Post> query = from(post);
        /***
         * Booleanbuilder (Querydsl)
         * ****/
//        BooleanBuilder builder = new BooleanBuilder();
//        builder.and(
//          post.title.containsIgnoreCase(keyword)
//              .or(post.content.containsIgnoreCase(keyword))
//        );
//
//        builder.and(post.author.eq(author));
//        조건 식이 복잡한 경우, 가독성을 올리기 위한 목적이 가장 클 것 같다

//        query.where(post.author.containsIgnoreCase(author).and(
//            post.content.containsIgnoreCase(keyword).or(post.title.containsIgnoreCase(keyword)))
//        );

        query.where((post.content.containsIgnoreCase(keyword)
            .or(post.title.containsIgnoreCase(keyword))).and(
            post.author.containsIgnoreCase(author)));

//        사실 어떤 케이스든 Builder를 사용하지 않고도 원하는 조건문을 만들 수 있을 것이다.
//
        return query.fetch();
    }

    @Override
    public List<Post> searchByKeyword(String... keyword) {
        QPost post = QPost.post;

        JPQLQuery<Post> query = from(post);

        BooleanBuilder builder = new BooleanBuilder();

        for (int i = 0; i < keyword.length; i++) {
            builder.and(
                post.title.containsIgnoreCase(keyword[i])
                    .or(post.content.containsIgnoreCase(keyword[i]))
                    .or(post.author.containsIgnoreCase(keyword[i]))
            );
        }

        query.where(builder);
        query.orderBy(post.id.desc());
        return query.fetch();
    }

    @Override
    public Page<Post> searchByKeywordWithPagination(Pageable pageable, String... keyword) {
        QPost post = QPost.post;

        JPQLQuery<Post> query = from(post);
        BooleanBuilder builder = new BooleanBuilder();

        for (int i = 0; i < keyword.length; i++) {
            builder.or(
                post.title.containsIgnoreCase(keyword[i])
                    .or(post.content.containsIgnoreCase(keyword[i]))
            );
        }

        query.where(builder);
        getQuerydsl().applyPagination(pageable, query);
//        페이지네이션은 querydsl 헬퍼 클래스를 이용해서 페이지네이션을 적용만 하면 됨!

        long total = query.fetchCount();
        log.info("total = {}", total);
//        fetchCount는 pageable을 적용하지 않았을 때, 전체 요소의 개수
//        query.orderBy(post.id.desc());
//
//        query.offset(pageable.getOffset()).limit(pageable.getPageSize());
//        List<Post> lists = query.fetch();
//        Long count = getCount(post, builder);

        return new PageImpl<>(query.fetch(), pageable, getCount(post, builder));
    }


    private Long getCount(QPost post, BooleanBuilder builder) {
        JPQLQuery<Post> query = from(post);
        query.where(builder);
        return query.fetchCount();
    }

    ;
}
