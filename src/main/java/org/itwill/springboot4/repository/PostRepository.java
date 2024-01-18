package org.itwill.springboot4.repository;

import org.itwill.springboot4.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostQuerydsl {

    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Post> findByContentContainingIgnoreCase(String content, Pageable pageable);

    Page<Post> findByTitleContainingOrContentContainingAllIgnoreCase(String title,
        String content, Pageable pageable);

    Page<Post> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

    Page<Post> findByTitleContainingOrContentContainingOrAuthorContainingAllIgnoreCase(String title, String content, String author, Pageable pageable);
}
