package org.itwill.springboot4.web;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.domain.Comment;
import org.itwill.springboot4.dto.CommentCreateRequestDto;
import org.itwill.springboot4.dto.CommentUpdateRequestDto;
import org.itwill.springboot4.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId,
        @RequestParam(defaultValue = "0") Integer curPage) {
        log.info("postId={}, curPage={}", postId, curPage);
        List<Comment> result = commentService.getCommentsByPost(postId, curPage);
        result.forEach(el -> log.info("el={}", el.getPost()));
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Long> createCommentByPost(@RequestBody CommentCreateRequestDto dto) {

        log.info("dto={}", dto);
        Long commentId = commentService.registerComment(dto);
        return ResponseEntity.ok(commentId);
    }


    @PutMapping
    public ResponseEntity<Boolean> updateComment(@RequestBody CommentUpdateRequestDto dto) {

        boolean result = commentService.updateComment(dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{cmtId}")
    public ResponseEntity<Object> deleteComment (@PathVariable Long cmtId) {
        commentService.deleteComment(cmtId);

        return ResponseEntity.status(204).build();
    }


}
