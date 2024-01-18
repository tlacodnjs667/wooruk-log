package org.itwill.springboot4.repository;


import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.domain.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class PostQuerydslTest {

    @Autowired
    private PostRepository postDao;

    @Test
    @DisplayName("postRepository는 PostQuerydsl와 postRepository의 인스턴스이다.")
    public void test1 () {
        Assertions.assertInstanceOf(PostQuerydsl.class, postDao);
        Assertions.assertInstanceOf(PostRepository.class, postDao);

        Assertions.assertNotNull(postDao);

        Post entity = postDao.searchById(622l);
        log.info("entity = {}", entity);
    }
}
