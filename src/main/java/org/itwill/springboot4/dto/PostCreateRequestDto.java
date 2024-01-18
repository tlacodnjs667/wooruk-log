package org.itwill.springboot4.dto;

import lombok.Data;
import org.itwill.springboot4.domain.Post;

@Data
public class PostCreateRequestDto {

    private String title;
    private String content;
    private String author;

    public Post toPost() {
        return Post.builder()
            .title(title)
            .content(content)
            .author(author).build();
    }

}
