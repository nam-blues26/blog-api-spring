package com.blog.controllers;

import com.blog.models.Post;
import com.blog.models.dtos.PostDTO;
import com.blog.responses.PostResponse;
import com.blog.services.IPostRedisService;
import com.blog.services.IPostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/post")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private IPostRedisService postRedisService;

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createPost(@Valid @RequestParam("file") MultipartFile file,
                                        @RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam("description") String desc,
                                        @RequestParam("category_id") long category_id) {
        try {
            if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File is too large! Maximum size is 10MB");
            }
            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("File must be an image");
            }
            String filename = storeFile(file);
            String slug = generateSlug(title);
            PostDTO postDTO = PostDTO.builder()
                    .title(title)
                    .content(content)
                    .categoryId(category_id)
                    .desc(desc)
                    .slug(slug)
                    .userId(1L)
                    .build();
            postService.createPost(postDTO,filename);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping()
    public ResponseEntity<?> getPosts() {
        try {
            List<PostResponse> postResponseList = postRedisService.getPostsRedis();
            if (postResponseList.isEmpty()) {
                List<PostResponse> posts = postService.getPosts();
                postRedisService.savePostsRedis(posts);
                return ResponseEntity.ok(posts);
            }
            return ResponseEntity.ok(postResponseList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/related/{slug}")
    public ResponseEntity<?> getRelatedPosts(@PathVariable("slug") String slug) {
        try {
            List<PostResponse> post = postService.getRelatedPost(slug);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<?> getPostById(@PathVariable("slug") String slug) {
        try {
            PostResponse post = postService.getPostBySlug(slug);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/update-with-image/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updatePost(@PathVariable("id") long id,
                                        @RequestParam("file") MultipartFile file,
                                        @RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam("description") String desc,
                                        @RequestParam("category_id") long category_id) {
        try {
            PostDTO postDTO = PostDTO.builder()
                    .title(title)
                    .content(content)
                    .categoryId(category_id)
                    .desc(desc)
                    .slug("")
                    .userId(1L)
                    .build();

                if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                String filename = storeFile(file);
                postService.updatePost(id, postDTO, filename);

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping(value = "/update-without-image/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updatePost(@PathVariable("id") long id,
                                        @RequestParam("title") String title,
                                        @RequestParam("content") String content,
                                        @RequestParam("description") String desc,
                                        @RequestParam("category_id") long category_id) {
        try {
            PostDTO postDTO = PostDTO.builder()
                    .title(title)
                    .content(content)
                    .categoryId(category_id)
                    .desc(desc)
                    .slug("")
                    .userId(1L)
                    .build();

                postService.updatePost(id, postDTO);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeletePost(@PathVariable("id") long postId) {
        try {

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/notfound.jpeg").toUri()));
                //return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
    public static String generateSlug(String title) {
        // Loại bỏ dấu và chuyển đổi thành chữ thường
        String slug = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
        slug = slug.replaceAll("[^a-z0-9\\s-]", "");
        slug = slug.replaceAll("\\s+", "-");

        return slug;
    }
}
