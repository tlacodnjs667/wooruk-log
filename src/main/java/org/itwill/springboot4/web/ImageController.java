package org.itwill.springboot4.web;


import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itwill.springboot4.service.ImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/image")
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final ImageService imageService;

    @GetMapping("")
    public ResponseEntity<Resource> getPhoto(@RequestParam String imgUrl) throws IOException {
        log.info("imgUrl={}", imgUrl);

        Resource imgResource = imageService.getFile(imgUrl);

        return ResponseEntity.ok(imgResource);
    }

    @PostMapping(consumes = {MediaType.ALL_VALUE})
    public ResponseEntity<String> uploadPhoto(@RequestParam MultipartFile file,
        HttpServletRequest req)
        throws IOException {
        log.info("file = {}", file);
        String imgUrl = imageService.uploadFile(file, req.getServletPath());
        log.info("저장된 imgUrl={}", imgUrl);
        return ResponseEntity.ok(imgUrl);
    }

}
