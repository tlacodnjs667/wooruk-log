package org.itwill.springboot4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.itwill.springboot4.domain.MemberRole;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberJoinDto {

    private String username;
//    @ToString.Exclude
    private String password;
    private String email;
    private MemberRole role = MemberRole.USER;

    public void encodePassword(PasswordEncoder encoder) {
        password = encoder.encode(password);
    }
}
