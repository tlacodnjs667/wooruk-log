package org.itwill.springboot4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itwill.springboot4.domain.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostModifyDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    public Post toPost() {
        return Post.builder()
            .id(id)
            .title(title)
            .content(content)
            .author(author).build();
    }

}
