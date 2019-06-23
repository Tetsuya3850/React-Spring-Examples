package com.example.instagramserver.storage;

import com.example.instagramserver.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/images")
public class ImageUploadController {

    private final PostRepository postRepository;
    private final ImageStorageService imageStorageService;

    @Autowired
    public ImageUploadController(PostRepository postRepository,
                                 ImageStorageService imageStorageService){
        this.postRepository = postRepository;
        this.imageStorageService = imageStorageService;
    }

    @PostMapping("")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        imageStorageService.store(file);
        String fileUrl = request.getRequestURL().toString() + "/" + file.getOriginalFilename();
        return fileUrl;
    }

    @GetMapping("/{filename:.+}")
    public Resource serveFile(@PathVariable String filename) {
        return imageStorageService.loadAsResource(filename);
    }

}
