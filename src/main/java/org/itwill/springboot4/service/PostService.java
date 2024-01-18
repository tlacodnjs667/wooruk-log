package org.itwill.springboot4.service;

import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.domain.Post;
import org.itwill.springboot4.dto.PostCreateRequestDto;
import org.itwill.springboot4.dto.PostModifyDto;
import org.itwill.springboot4.dto.PostSearchRequestDto;
import org.itwill.springboot4.repository.PostQuerydsl;
import org.itwill.springboot4.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PostService {

    @Autowired
    private PostRepository postDao;
    private PostQuerydsl postQuerydsl;

    public Post createPost(PostCreateRequestDto entity) {
        Post post = entity.toPost();
        postDao.save(post);

        return post;
    }

    public Post getPostDetail(Long postId) {
        Post post = postDao.findById(postId).orElse(null);

        return post;
    }

    public void deletePost(Long postId) {
        postDao.deleteById(postId);
    }

    public Page<Post> getPostList(Integer curPage, String sort) {
        Pageable pageable = PageRequest.of(curPage, 10, Sort.by(Direction.DESC, sort));

        Page<Post> page = postDao.findAll(pageable);

        page.forEach(e -> log.info("page={}", e));
        return page;
    }

    @Transactional
    public Post modifyPost(PostModifyDto entity) {
        Post post = entity.toPost();
        postDao.save(post);
        return post;
    }

    public Page<Post> searchPosts(PostSearchRequestDto entity) {
        Page<Post> result = null;
        Pageable pageable = PageRequest.of(entity.getCurPage(), 10);

        switch (entity.getCategory()) {
            case "t" ->
                result = postDao.findByTitleContainingIgnoreCase(entity.getKeyword(), pageable);
            case "c" ->
                result = postDao.findByContentContainingIgnoreCase(entity.getKeyword(), pageable);
            case "tc" -> result = postDao.findByTitleContainingOrContentContainingAllIgnoreCase(
                entity.getKeyword(), entity.getKeyword(), pageable);
            case "a" ->
                result = postDao.findByAuthorContainingIgnoreCase(entity.getKeyword(), pageable);
            default ->
                result = postDao.findByTitleContainingOrContentContainingOrAuthorContainingAllIgnoreCase(
                    entity.getKeyword(), entity.getKeyword(), entity.getKeyword(), pageable);
        }

        return result;
    }
}
