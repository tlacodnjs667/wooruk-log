package org.itwill.springboot4.service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.domain.Member;
import org.itwill.springboot4.domain.MemberSecurityDto;
import org.itwill.springboot4.dto.MemberJoinDto;
import org.itwill.springboot4.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberService implements UserDetailsService {

    @Autowired
    private MemberRepository memberDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername(username={})", username);
        Optional<Member> opt = memberDao.findByUsername(username);

        if (opt.isPresent()) {
            return MemberSecurityDto.fromEntity(opt.get());
        } else {
            throw new UsernameNotFoundException("NOT_PRESENT");
        }
    }

    public Integer join(MemberJoinDto memberToJoin) {
        log.info("join={}", memberToJoin);

        Member memberToCheck = memberDao.findByUsername(memberToJoin.getUsername()).orElse(null);
        if (memberToCheck != null) {
            return null;
        }

        memberToJoin.encodePassword(passwordEncoder);

        Member member = Member.builder()
            .username(memberToJoin.getUsername())
            .password(memberToJoin.getPassword())
            .email(memberToJoin.getEmail())
            .build();

        member.addRole(memberToJoin.getRole());

        return memberDao.save(member).getId();
    }
}
