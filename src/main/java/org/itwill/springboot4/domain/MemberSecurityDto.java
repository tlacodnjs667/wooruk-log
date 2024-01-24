package org.itwill.springboot4.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// (1) UserDetails 인터페이스를 직접 구현하건나
// (2) UserDetails 인터페이스를 구현하는 User 클래스를 상속


public class MemberSecurityDto extends User {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private String email;

    private MemberSecurityDto(String username, String password, String email,
        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.email = email; // super보다 아래쪽에 있어야 한다.
    }

    public static MemberSecurityDto fromEntity(Member entity) {
//        List<SimpleGrantedAuthority> authorities = entity.getRoles().stream().map(x -> new SimpleGrantedAuthority(x.getAuthority())).toList();

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (MemberRole role : entity.getRoles()) {
            SimpleGrantedAuthority auth = new SimpleGrantedAuthority(role.getAuthority());
            authorities.add(auth);
        }

        return new MemberSecurityDto(entity.getUsername(), entity.getPassword(), entity.getEmail(),
            authorities);
    }

    ;

}

/*
public class MemberSecurityDto implements UserDetails {


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    } // false 반환 시 만료되었다는 것 => 로그인 불가

    @Override
    public boolean isAccountNonLocked() {
        return false;
    } // false 반환 시 만료되었다는 것 => 로그인 불가

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
*/