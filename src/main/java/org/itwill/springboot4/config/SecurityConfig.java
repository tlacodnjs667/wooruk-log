package org.itwill.springboot4.config;

import org.itwill.springboot4.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// -> 스프링 컨테이너에서 객체를 생성하고 관리 -> 필요한 곳에 의존성 주입을 할 수 있음
// 로그인 할 때 사용할 비밀번호와 아이디를 임시로 만들어놓고
@EnableMethodSecurity // 컨트롤러 메서드 에너테이션 사용할 수 있는 권한 부여, 인증 활성화
public class SecurityConfig {

    /**
     * 사용자 관리(로그인, 로그아웃, 회원 가입 등)를 위한 서비스 인터페이스 스프링 부트 애플리케이션에서 스프링 시큐리티를 이용한 로그인/로그아웃을 하려면
     * UserDetailService 인터페이스를 구현하는 클래스가 있어야 함
     */
//    @Bean
//    public UserDetailsService inMemoryUserDetailsService() {
//
//        UserDetails user1 = User.withUsername("user1") // 로그인 사용자 이름
//            .password(passwordEncoder().encode("1111")) // 로그인 사용자 비밀 번호
//            .roles("USER") // 로그인 사용자 권한
//            .build();
//
//        UserDetails user2 = User.withUsername("user2") // 로그인 사용자 이름
//            .password(passwordEncoder().encode("2222")) // 로그인 사용자 비밀 번호
//            .roles("ADMIN", "USER") // 로그인 사용자 권한
//            .build();
//
//        UserDetails user3 = User.withUsername("user3") // 로그인 사용자 이름
//            .password(passwordEncoder().encode("3333")) // 로그인 사용자 비밀 번호
//            .roles("ADMIN") // 로그인 사용자 권한
//            .build();
//
//        return new InMemoryUserDetailsManager(user1, user2, user3);
//    }
    @Bean
    public static MemberService memberService() {
        return new MemberService();
    }

    /**
     * Spring Security 5 버전부터 비밀번호는 반드시 암호화를 해야 함 - 비밀번호를 암호화하지 않으면 HTTP 403(Access Denied, 접근 거부) -
     * 또는 HTTP 500 (Internal server error, 내부 서버 오류) 에러가 발생함 - 비밀번호 암호화를 할 수 있는 객체를 스프링 컨테이너가 bean으로
     * 관리해야 함
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder();
    }
//    스프링 시큐리티 필터 체인 객체

    /**
     * 로그인 로그아웃 관련 설정 로그인 페이지 (뷰), 로그아웃 페이지 (뷰) 설정 페이지 접근 권한, 인증 설정. (로그인 없이 접근 가능한 페이지 로그인해야만 접근
     * 가능한 페이지)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        CSRF 기능을 활성화한 경우
//         Ajax POST/PUT/DELETE 요청에서 csrf 토큰을 서버로 전송하지 않으면 http 403 에러가 발생
//        CSRF (Cross Site Request Forgery) 비활성화
        http.csrf((x) -> {
            x.disable();
        });
//      로그인 페이지 설정 = 스프링 시큐리티에서 제공하는 기본 페이지를 이용
//        http.formLogin(Customizer.withDefaults());
//        로그인 페이지를 Custom 페이지로 설정
        http.formLogin(login -> login.loginPage("/member/login"));
//        로그아웃 이후에 이동할 페이지 설정 - 홈 페이지 (/)
        http.logout((logout) -> logout.logoutSuccessUrl("/"));

/**
 * 페이지 접근 권한, 인증 설정
 * 1. authorizeHttpRequests() 메서드에서 직접 설정
 * -> 단점 : 새로운 요청 경로가 생길 때마다 설정 코드 (requestMatchers())를 변경
 * 2. 에너테이션을 사용하여 권한, 인증 설정
 *  (1) SecurityConfig 빈에서 @EnableMethodSecurity 에너테이션을 사용
 *  (2) 각 Controller 메서드에 @PreAuthorize 또는 @PostAuthorize 에너테이션을 사용
 *
 */

//            auth.anyRequest().authenticated(); // 모든 요청 주소에 대해서 (ROLE에 상관 없이)아이디/비밀번호를 로그인해야 한다는 설정
        /*
        http.authorizeHttpRequests((auth) ->
            auth.anyRequest().hasRole("USER") // 모든 요청 주소에 대해서 ("USER")로 로그인해야 한다는 설정
                auth
                    .requestMatchers("/post/create", "/post/delete/{postId}", "/post/modify/{postId}",
                        "/post/update/{postId}")
                    .hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE).hasRole("USER")
                    .anyRequest()
                    .permitAll()
        );
*/
        return http.build();
    }
}
