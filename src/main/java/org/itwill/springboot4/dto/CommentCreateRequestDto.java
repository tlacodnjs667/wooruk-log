package org.itwill.springboot4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itwill.springboot4.domain.Comment;
import org.itwill.springboot4.domain.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateRequestDto {

    private Long postId;
    private String text;
    private String writer;

    public Comment toEntity() {
        return Comment.builder()
            .post(Post.builder().id(postId).build())
            .text(text)
            .writer(writer)
            .build();
    }
}
