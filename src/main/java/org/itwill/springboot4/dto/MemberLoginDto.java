package org.itwill.springboot4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.itwill.springboot4.domain.MemberRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberLoginDto {
    private String username;
    @ToString.Exclude
    private String password;

    private MemberRole role = MemberRole.USER;
}
