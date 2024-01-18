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
public class CommentUpdateRequestDto {
    private Long commentId;
    private String text;
}
