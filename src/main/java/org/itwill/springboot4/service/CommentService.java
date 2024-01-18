package org.itwill.springboot4.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.domain.Comment;
import org.itwill.springboot4.dto.CommentCreateRequestDto;
import org.itwill.springboot4.dto.CommentUpdateRequestDto;
import org.itwill.springboot4.repository.CommentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentDao;

    public List<Comment> getCommentsByPost(Long postId, Integer curPage) {
        Pageable pagination = PageRequest.of(curPage, 5, Sort.by("createdTime").descending());
        List<Comment> result = commentDao.findByPostId(postId, pagination);

        return result;
    }

    @Transactional
    public Long registerComment(CommentCreateRequestDto dto) {
        Comment comment = dto.toEntity();
        commentDao.save(comment);
        return comment.getId();
    }

    @Transactional
    public Boolean updateComment(CommentUpdateRequestDto dto) {
        Comment comment = commentDao.findById(dto.getCommentId()).orElse(null);
        if (comment != null) {
            comment.modifyComment(dto.getText());
        }

        return (comment != null);
    }

    public void deleteComment(Long cmtId) {
        commentDao.deleteById(cmtId);
    }
}
