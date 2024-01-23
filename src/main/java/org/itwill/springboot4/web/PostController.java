package org.itwill.springboot4.web;

import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.domain.Post;
import org.itwill.springboot4.dto.PostCreateRequestDto;
import org.itwill.springboot4.dto.PostModifyDto;
import org.itwill.springboot4.dto.PostSearchRequestDto;
import org.itwill.springboot4.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public void getPostPage() {
    }

    @GetMapping("/modify/{postId}")
    @PreAuthorize("hasRole('USER')")
    public String getPostModifyPage(@PathVariable Long postId, Model model) {
        Post post = postService.getPostDetail(postId);

        model.addAttribute("post", post);

        log.info("post={}", post);
        return "/post/modify";
    }

    @GetMapping("/delete/{postId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String deletePost(@PathVariable Long postId) {

        postService.deletePost(postId);

        log.info("postId = {}", postId);
        return "redirect:/post/list?curPage=0&status=deleted";
    }

    @GetMapping("/list")
    public void getPostListPage(Model model, @RequestParam(defaultValue = "0") Integer curPage,
        @RequestParam(defaultValue = "id") String sort) {

        Page<Post> page = postService.getPostList(curPage, sort);
        model.addAttribute("page", page);
        model.addAttribute("curPage", curPage);
        model.addAttribute("sort", sort);
    }

    @GetMapping("/{postId}")
    public String getPostDetail(@PathVariable Long postId, Model model) {
        Post post = postService.getPostDetail(postId);
        model.addAttribute("post", post);

        return "/post/detail";
    }

    @ResponseBody
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')") // @PreAuthorize("authenticated()")
    public ResponseEntity<Long> createPost(@RequestBody PostCreateRequestDto entity) {
        log.info("들어온 post={}", entity);

        Post postCreated = postService.createPost(entity);
        return ResponseEntity.ok(postCreated.getId());
    }

    @ResponseBody
    @PostMapping("/modify")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> modifyPost(@RequestBody PostModifyDto entity) {
        log.info("post={}", entity);
        Post post = postService.modifyPost(entity);
        return ResponseEntity.ok(post.getId());
    }

    @GetMapping("/search")
    public void searchPost(@ModelAttribute PostSearchRequestDto entity, Model model) {
        log.info("search(entity={})", entity);
        Page<Post> page = postService.searchPosts(entity);
        model.addAttribute("page", page);
        model.addAttribute("curPage", entity.getCurPage());
        model.addAttribute("category", entity.getCategory());
        model.addAttribute("keyword", entity.getKeyword());
    }

}
