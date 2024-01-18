package org.itwill.springboot4.repository;

import java.util.List;
import org.itwill.springboot4.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId, Sort sort);
    List<Comment> findByPostId(Long postId, Pageable pageable);
}
