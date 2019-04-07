package com.example.fileupload;

import com.example.fileupload.post.Post;
import com.example.fileupload.post.PostRepository;
import com.example.fileupload.storage.StorageFileNotFoundException;
import com.example.fileupload.storage.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class FileUploadController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private StorageService storageService;

    @PostMapping("")
    public Post handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        storageService.store(file);
        String fileName = file.getOriginalFilename();
        Post newPost = new Post();
        newPost.setFilename(fileName);
        newPost.setFileurl(request.getRequestURL().toString() + "files/" + fileName);
        postRepository.save(newPost);
        return newPost;
    }

    @GetMapping("")
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public Resource serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return file;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
