package org.itwill.springboot4.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.domain.Comment;
import org.itwill.springboot4.domain.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentDao;

    @Autowired
    private PostRepository postDao;

    @Test
    public void testSave() {
        Post post = postDao.findById(619L).orElseThrow();

//        for (int i = 0; i < 5001; i++) {
//            Comment entity = Comment.builder()
//                .post(post)
//                .text("테스트 댓글입니다." + i)
//                .writer("admin")
//                .build();
//
//            System.out.println("전");
//            System.out.println("entity = " + entity);
//            System.out.println("entity.getCreatedTime() = " + entity.getCreatedTime());
//
//            commentDao.save(entity);
//
//            System.out.println("후");
//            System.out.println("entity = " + entity);
//            System.out.println("entity.getCreatedTime() = " + entity.getCreatedTime());
//        }
    }

    @Test
    @Transactional
    void testReadByPostId() {
        Sort sort = Sort.by("id").descending(); // id(PK) 컬럼 내림차순 정렬
        List<Comment> list = commentDao.findByPostId(615L, sort);

        Post post0 = list.get(0).getPost();

        Post post1 = list.get(1).getPost();

        if (post1 == post0) {
            System.out.println("list.get(0).getPost() = " + list.get(0).getPost());
        }

        assertEquals(post0, post1);

        list.forEach((x) -> System.out.println(x.toString()));
    }


}