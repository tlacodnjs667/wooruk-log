package org.itwill.springboot4.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.itwill.springboot4.domain.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postDao;

    //    @Test
    void testFindAll() {
        assertNotNull(postDao);

        // db 테이블에서 전체 검색 테스트

        List<Post> list = postDao.findAll();
        list.forEach(e -> System.out.println(e));
    }

        @Test
    void testSave() {

        for (int i = 100; i < 301; i++) {
            Post entity = Post.builder()
                .title("DummyTitle" + i)
                .content("DummyContent" + i)
                .author("작가미상" + i).build();

            Post result = postDao.save(entity);

            System.out.println("result = " + result);
            System.out.println("entity = " + entity);
        }

//        엔터티의 id필드가 null인 경우 insert 쿼리를 실행
//        엔터티의 id 필드 값으로 select할 수 있는 값이 없을 경우

//        setter가 없는데 id가 바뀌어져 있고, 다른 것들도 있음(시간 등)
//        자바에 reflection 이라는 기능이 있고, 이것을 사용하면 바이트코드로 필드 이름이나 값을 가져올 수 있다.

//        엔터티에 setter를 안 주는 이유
//        => save나 update 메서드를 사용하지 않아도, setter를 사용하여 인스턴스(select한 인스턴스 or 반환값으로 받은 인스턴스)의 필드 값을 바꿔버리면,
//        transaction이 끝나는 순간 DB에서는 업데이트 문장이 실행됨
//        즉, Setter로 인해 의도치 않게 DB의 정보가 변경될 수 있으므로 jpa 사용 시 Setter 구현하지 않는 것을 추천
    }

    @Test
    void testUpdate() {

    }

    @Test
    void testDelete() {
        postDao.deleteById(2L);
//        id로 select 쿼리를 실행한 후, 엔터티가 존재할 때 delete 쿼리를 실행함
    }


}