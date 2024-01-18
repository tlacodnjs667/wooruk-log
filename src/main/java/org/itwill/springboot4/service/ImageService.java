package org.itwill.springboot4.service;

import java.io.IOException;
import org.itwill.springboot4.util.FileUploader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final FileUploader fileUploader;

    public ImageService(FileUploader fileUploader) {
        this.fileUploader = fileUploader;
    }

    public String uploadFile(MultipartFile file, String servletPath) throws IOException {
        return fileUploader.upload(file, servletPath);
    }

    public Resource getFile(String imgUrl) throws IOException {
        return fileUploader.download(imgUrl);
    }

}
