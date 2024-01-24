package org.itwill.springboot4.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

@Getter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NaturalId
    @Basic(optional = false)
    private String username;

    @Basic(optional = false)
//    @ToString.Exclude
    private String password;

    @Basic(optional = false)
    private String email;

    @ToString.Exclude
    @Enumerated(EnumType.STRING) // DB 테이블에 저장할 때 상수 이름 (문자열)을 사용
    @ElementCollection(fetch = FetchType.LAZY) // 연관된 별도의 테이블을 사용
    @Builder.Default // Builderㅇㅔ서 null이 아닌 Set<> 객체를 생성하기 위해서
    private Set<MemberRole> roles = new HashSet<>();

    public void addRole(MemberRole... roles) {
        Collections.addAll(this.roles, roles);
    }

}
