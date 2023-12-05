package com.DATN.FiveITViec.CommonController.EmployerController;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.BlogDTO;
import com.DATN.FiveITViec.dto.ContactDTO;
import com.DATN.FiveITViec.model.Applicant;
import com.DATN.FiveITViec.model.Blog;
import com.DATN.FiveITViec.model.Job;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.repository.BlogRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.BlogService;
import com.DATN.FiveITViec.services.FileUploadService;
import com.DATN.FiveITViec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/auth/employer")
public class BlogManagementController {
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    public FileUploadService fileUploadService;
    @Autowired
    public BlogRepository blogRepository;

    @GetMapping("/getBlogsEmployer")
    public ResponseEntity<?> getBlogs(@RequestHeader(name="Authorization", required = false)String authorizationHeader,
                                      @RequestParam(name="status") String status, @RequestParam(name="approval") String approval,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "5") int size
    ) throws IOException {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String email = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(email);
                if(user != null){
                    Page<BlogDTO> getAllBlog = blogService.getAllBlogsByUsersAndStatusAndApproval(user.getUserId(), status,approval,PageRequest.of(page, size));
                    return new ResponseEntity<>(getAllBlog, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Unauthorized Request", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/postBlogEmployer")
    public ResponseEntity<?> createNewBlog(@RequestHeader(name="Authorization", required = false)String authorizationHeader,
                                      @RequestParam("blogTitle") String blogTitle,
                                      @RequestParam("blogContent") String blogContent,
                                      @RequestParam(value = "blogImg", required = false) MultipartFile blogImg,
                                      @RequestParam("author") String author) throws IOException {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                String email = jwtGenerator.getUsernameFromJWT(token);
                User user = userRepository.findByEmail(email);
                if(user.getRoles().getRoleName().equals(FiveITConstants.EMPLOYER)){
                    String imgURL = fileUploadService.uploadFile(blogImg);
                    Blog blog = new Blog();
                    blog.setUser(user);
                    blog.setBlogTitle(blogTitle);
                    blog.setBlogContent(blogContent);
                    blog.setBlogImg(imgURL);
                    blog.setAuthor(author);
                    blog.setStatus(FiveITConstants.DISABLE_STATUS);
                    blog.setApproval(FiveITConstants.WAITING_STATUS);
                    blogRepository.save(blog);
                    return new ResponseEntity<>("Success", HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<>("Fail", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/updateBlogEmployer")
    public ResponseEntity<String> uploadFile(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam("blogId") String blogId,
            @RequestParam("blogTitle") String blogTitle,
            @RequestParam("file") Object file,
            @RequestParam("blogContent") String blogContent,
            @RequestParam("author") String author)
            throws IOException {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                Optional<Blog> blog = blogRepository.findById(Long.valueOf(blogId));
                if(blog.isPresent()){
                    blog.get().setBlogContent(blogContent);
                    blog.get().setBlogTitle(blogTitle);
                    blog.get().setAuthor(author);
                    blog.get().setBlogContent(blogContent);
                    if (file instanceof String) {
                        String fileAsString = (String) file;
                        blog.get().setBlogImg(fileAsString);
                    } else {
                        MultipartFile fileMultipartFile = (MultipartFile) file;
                        String fileURL = fileUploadService.uploadFile(fileMultipartFile); // Lưu trữ tệp tải lên
                        if (isImageFile(fileMultipartFile)) {
                            blog.get().setBlogImg(fileURL);
                        }
                    }
                    blogRepository.save(blog.get());
                    return new ResponseEntity<>("Successfully", HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>("Upload Fail", HttpStatus.BAD_REQUEST);
    }


    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }




}
