package org.itwill.springboot4.web;

import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.domain.Comment;
import org.itwill.springboot4.dto.CommentCreateRequestDto;
import org.itwill.springboot4.dto.CommentUpdateRequestDto;
import org.itwill.springboot4.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Map<String, Object>> getCommentsByPost(@PathVariable Long postId,
        @RequestParam(defaultValue = "0") Integer curPage) {
        log.info("postId={}, curPage={}", postId, curPage);
        Map<String, Object> result = commentService.getCommentsByPost(postId, curPage);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> createCommentByPost(@RequestBody CommentCreateRequestDto dto) {

        log.info("dto={}", dto);
        Long commentId = commentService.registerComment(dto);
        return ResponseEntity.ok(commentId);
    }


    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> updateComment(@RequestBody CommentUpdateRequestDto dto) {

        boolean result = commentService.updateComment(dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{cmtId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Object> deleteComment (@PathVariable Long cmtId) {
        commentService.deleteComment(cmtId);

        return ResponseEntity.status(204).build();
    }


}
