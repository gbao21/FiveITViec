package com.DATN.FiveITViec.CommonController.AdminController;

import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.BlogDTO;
import com.DATN.FiveITViec.model.Blog;
import com.DATN.FiveITViec.repository.BlogRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController()
@RequestMapping("/auth/admin")
public class BlogAdminManagementController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private JWTGenerator jwtGenerator;


    @GetMapping("/getAllBlogs")
    public ResponseEntity<?> getAllBlogs(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name="blogTitle") String blogTitle,
            @RequestParam(name="status") String status,
            @RequestParam(name="approval") String approval,
            @RequestParam(name="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                    Page<BlogDTO> getAllBlogs = blogService.getAllBlogsByTitleContainingAndStatusAndApproval(blogTitle, status,approval, startDate, endDate, PageRequest.of(page, size));
                    return new ResponseEntity<>(getAllBlogs, HttpStatus.OK);
                }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }


    @PutMapping("/updateBlogStatus")
    public ResponseEntity<String> updateBlogStatus(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name="blogId") String blogId,
            @RequestParam(name="status") String status,
            @RequestParam(name="approval", required = false) String approval
            ){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                Optional<Blog> blog = blogRepository.findById(Long.valueOf(blogId));
                if(blog.isPresent()){
                    if(approval !=null && approval.equals(FiveITConstants.WAITING_STATUS)){
                        blog.get().setStatus(FiveITConstants.ENABLE_STATUS);
                        blog.get().setApproval(FiveITConstants.APPROVAL_STATUS);
                    }else{
                    blog.get().setStatus(status);
                    }
                    blogRepository.save(blog.get());
                    return new ResponseEntity<>("Updated", HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/getTotalApprovedBlogByMonth")
    public  ResponseEntity<?> getTotalApprovedBlogByMonth(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name = "year",required = false) String year){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                int yearCurrent = 0;
                if(year!=null){
                    yearCurrent= Integer.valueOf(year);
                }else{
                    yearCurrent = YearMonth.now().getYear();
                }
                Map<Object, Object> totalApprovedBlog= new HashMap<>();
                Map<Object, Object> responseMap = new HashMap<>();
                for(int i = 1;i<=12; i++){
                    long blog = blogRepository.countAllBlogByMonthAndStatus(FiveITConstants.APPROVAL_STATUS,i,yearCurrent);
                    totalApprovedBlog.put(i,blog);

                }
                responseMap.put("approvedBlogMap",totalApprovedBlog);


                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/getTotalWaitingBlogByMonth")
    public  ResponseEntity<?> getTotalWaitingBlogByMonth(
            @RequestHeader(name = "Authorization", required = false) String authorizationHeader,
            @RequestParam(name = "year",required = false) String year){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtGenerator.validateToken(token)) {
                int yearCurrent = 0;
                if(year!=null){
                    yearCurrent= Integer.valueOf(year);
                }else{
                    yearCurrent = YearMonth.now().getYear();
                }
                Map<Object, Object> totalWaitingBlog = new HashMap<>();
                Map<Object, Object> responseMap = new HashMap<>();
                for(int i = 1;i<=12; i++){
                    long blog = blogRepository.countAllBlogByMonthAndStatus(FiveITConstants.WAITING_STATUS,i,yearCurrent);
                    totalWaitingBlog.put(i,blog);

                }
                responseMap.put("waitingBlogMap",totalWaitingBlog);


                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
    }
}

