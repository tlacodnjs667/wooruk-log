package org.itwill.springboot4.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test() {
        assertNotNull(memberDao);
        log.info("memberDao={}", memberDao);

        assertNotNull(passwordEncoder);
        log.info("passwordEncoder={}", passwordEncoder);
    }

    @Test
    public void saveMemberTest() {

        for (int i = 5; i < 7; i++) {
            Member m = Member.builder().
                username("admin" + i )
                .password(passwordEncoder.encode(String.valueOf(i).repeat(5)))
                .email("admin" + i + "@aaaa.aaa").
                build();

            if (i % 2 == 0) {
                m.addRole(MemberRole.USER);
            }else {
                m.addRole(MemberRole.ADMIN);
            }
            log.info("save 후 : {}", m);
            memberDao.save(m);
            log.info("save 후 : {}", m);
        }

    }

    @Test
    public void testFindByUsername() {
        Optional<Member> m = memberDao.findByUsername("admin0");
        log.info("{}, {}", m, m.get().getRoles());
    }

}
