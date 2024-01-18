package org.itwill.springboot4.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Builder
@Getter @ToString
@Entity
@Table(name = "posts")
// 세터 메서드 안 만들려고 하고 계심 => 왜? DB와
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 테이블 생성 시 Generated as identity
    private Long id;

    private String title;
    private String content;
    private String author;

    @OneToMany(fetch = FetchType.LAZY) @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "POST_ID")
    private List<Comment> comments;

}
