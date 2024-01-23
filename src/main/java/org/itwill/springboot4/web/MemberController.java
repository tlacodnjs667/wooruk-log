package org.itwill.springboot4.web;

import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.dto.MemberJoinDto;
import org.itwill.springboot4.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/login")
    public void login() {
        log.info("GET - login()");
    }

    @GetMapping("/signup")
    public void signup() {
        log.info("GET - signup()");
    }

    @PostMapping("/signup")
    public String login(MemberJoinDto memberToJoin) {
        log.info("POST - signup(memberToJoin={})", memberToJoin);
        Integer memberId = memberService.join(memberToJoin);
        log.info("memberId={}", memberId);

        if (memberId == null) {
            return "redirect:/member/signup?error";
        }

        return "redirect:/";
    }


}
