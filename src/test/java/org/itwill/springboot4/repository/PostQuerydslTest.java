package org.itwill.springboot4.repository;


import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.domain.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Slf4j
@SpringBootTest
public class PostQuerydslTest {

    @Autowired
    private PostRepository postDao;

    @Test
    @DisplayName("postRepository는 PostQuerydsl와 postRepository의 인스턴스이다.")
    public void test1() {
        Assertions.assertInstanceOf(PostQuerydsl.class, postDao);
        Assertions.assertInstanceOf(PostRepository.class, postDao);

        Assertions.assertNotNull(postDao);

        Post entity = postDao.searchById(622l);
        log.info("entity = {}", entity);
    }


    @Test
    public void test2() {
        List<Post> posts = postDao.searchByTitleOrContent("cs");
        log.info("posts = {}", posts);
        Assertions.assertEquals(posts.size(), 1);

    }

    @Test
    public void test3() {
        List<Post> posts = postDao.searchByModifiedTime(LocalDateTime.of(2024, 1, 18, 19, 00),
            LocalDateTime.of(2024, 1, 18, 21, 00));
        log.info("posts = {}", posts);
        Assertions.assertEquals(1, posts.size());

    }

    @Test
    public void test4() {
        List<Post> posts = postDao.searchByKeywordAndAuthor("cs", "cs teacher");
        log.info("posts = {}", posts);
        Assertions.assertEquals(1, posts.size());

    }

    @Test
    public void test5() {
        List<Post> posts = postDao.searchByKeyword("cs", "고모");
        log.info("posts = {}", posts);
        Assertions.assertEquals(1, posts.size());
    }

    @Test
    public void test6() {
        Page<Post> page = postDao.searchByKeywordWithPagination(PageRequest.of(0, 2), "cs", "고모");

        log.info("totalPage={}", page.getTotalPages());
        log.info("getTotalElements()={}", page.getTotalElements());
        log.info("getNumber={}", page.getNumber());

        page.forEach(e->log.info("el = {}", e));
    }
}
