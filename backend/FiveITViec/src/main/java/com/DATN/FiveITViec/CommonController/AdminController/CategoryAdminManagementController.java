package com.DATN.FiveITViec.CommonController.AdminController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.DATN.FiveITViec.model.JobCategory;
import com.DATN.FiveITViec.model.User;
import com.DATN.FiveITViec.repository.JobRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.DATN.FiveITViec.dto.CategoryDTO;
import com.DATN.FiveITViec.repository.JobCategoryRepository;
import com.DATN.FiveITViec.security.JWTGenerator;
import com.DATN.FiveITViec.services.CategoryService;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/auth/admin")
public class CategoryAdminManagementController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private JobCategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	public FileUploadService fileUploadService;

	@Autowired
	private JWTGenerator jwtGenerator;

	@Autowired
	private JobRepository jobRepository;

	@GetMapping("/getAllCategories")
	public ResponseEntity<?> getAllCates(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam(name = "cateTitle") String cateTitle,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				Page<CategoryDTO> getAllCates = categoryService.getAllCatesByTitleContaining(cateTitle, startDate,
						endDate, PageRequest.of(page, size));
				return new ResponseEntity<>(getAllCates, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>("Error", HttpStatus.UNAUTHORIZED);
	}
	@PostMapping("/saveCate")
	public ResponseEntity<String> saveCate(@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
										   @RequestParam("cateName") String cateName, @RequestParam("urlImg") String urlImg) throws IOException {

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				List<JobCategory> listCate = categoryRepository.findAll();
				boolean isCateNameExists = listCate.stream()
						.anyMatch(category -> category.getCategoryName().equals(cateName));

				if (isCateNameExists) {
					return new ResponseEntity<>("Category name is exists", HttpStatus.BAD_REQUEST);
				} else {
					JobCategory categoryJob = new JobCategory();
					categoryJob.setCategoryName(cateName);
					categoryJob.setCategoryImg(urlImg);
					categoryRepository.save(categoryJob);
					return new ResponseEntity<>("Save Successfully", HttpStatus.OK);
				}

			}
		}

		return new ResponseEntity<>("Save Fail", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/updateCate")
	public ResponseEntity<String> updateCate(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam("cateId") String cateId, @RequestParam("cateName") String cateName,
			@RequestParam("urlImg") String urlImg) throws IOException {

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				Optional<JobCategory> categoryJob = categoryRepository.findById(Long.parseLong(cateId));

				if (categoryJob.isPresent()) {
					JobCategory jobCategory = categoryJob.get();
					JobCategory cateFoundByCheckName = categoryRepository.findCateByName(cateName);
					if(categoryJob.get().getCategoryImg().equals(urlImg) && cateFoundByCheckName != null ){
						jobCategory.setCategoryImg(urlImg);
						return new ResponseEntity<>("Update Successfully", HttpStatus.OK);
					}
					else if( !(categoryJob.get().getCategoryImg().equals(urlImg)) || cateFoundByCheckName == null){
						jobCategory.setCategoryName(cateName);
						jobCategory.setCategoryImg(urlImg);
						categoryRepository.save(jobCategory);
						return new ResponseEntity<>("Update Successfully", HttpStatus.OK);
					}else{
						return new ResponseEntity<>("Update Fail", HttpStatus.BAD_REQUEST);
					}
				} else {
					return new ResponseEntity<>("Update Fail", HttpStatus.BAD_REQUEST);
				}

			}
		}
		return new ResponseEntity<>("Upload Fail", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/deleteCate")
	public ResponseEntity<String> deleteCate(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam("cateId") String cateId) throws IOException {

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				Optional<JobCategory> categoryJob = categoryRepository.findById(Long.parseLong(cateId));
				if (categoryJob.isPresent()) {
					categoryRepository.deleteById(categoryJob.get().getCategoryId());
						return new ResponseEntity<>("Delete Successfully", HttpStatus.OK);
				} else {
				return new ResponseEntity<>("Delete Fail", HttpStatus.BAD_REQUEST);
				}
			}
		}
		return new ResponseEntity<>("Delete Fail", HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/uploadAvtCate")
	public ResponseEntity<String> uploadFileImg(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam("file") MultipartFile multipartFile) throws IOException {
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				String fileURL = fileUploadService.uploadFile(multipartFile);
				if (isImageFile(multipartFile)) {return new ResponseEntity<>(fileURL, HttpStatus.OK);
				}
			}
		}

		return new ResponseEntity<>("Upload Fail", HttpStatus.BAD_REQUEST);
	}

	private boolean isImageFile(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && contentType.startsWith("image/");
	}

	@GetMapping("/getJobByCategory")
	public  ResponseEntity<?> getQuantityJobAndCvByMonth(
			@RequestHeader(name = "Authorization", required = false) String authorizationHeader,
			@RequestParam(name = "categoryId",required = false) String categoryId){
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			if (jwtGenerator.validateToken(token)) {
				List<JobCategory> list = categoryRepository.findAll();
				Map<Object, Object> totalJob = new HashMap<>();
				Map<String, Object> responseMap = new HashMap<>();
				for (int i = 0; i < list.size(); i++) {

					JobCategory jobCategory = list.get(i);
					String cateIdString = String.valueOf(jobCategory.getCategoryId());
					String cateName = jobCategory.getCategoryName();
					Integer countJob = jobRepository.getCountJobByCate(cateIdString);
					totalJob.put(cateName, countJob);
				}
				responseMap.put("jobMap",totalJob);
				return new ResponseEntity<>(responseMap, HttpStatus.OK);


			}
		}
		return new ResponseEntity<>("Error",  HttpStatus.UNAUTHORIZED);
	}

}
