package com.DATN.FiveITViec.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.DATN.FiveITViec.dto.CategoryDTO;
import com.DATN.FiveITViec.model.JobCategory;
import com.DATN.FiveITViec.repository.JobCategoryRepository;

@Service
public class CategoryService {

	@Autowired
	JobCategoryRepository categoryRepository;

	public Page<CategoryDTO> getAllCatesByTitleContaining(String cateTitle, LocalDate startDate, LocalDate endDate,
														  Pageable pageable) {
		Page<JobCategory> pageOfCates = categoryRepository.findCatesByTitleContaining(cateTitle, startDate, endDate,
				pageable);

		List<CategoryDTO> listCatesDTO = new ArrayList<>();

		for (JobCategory cate : pageOfCates.getContent()) {
			CategoryDTO cateDTO = new CategoryDTO();
			cateDTO.setCategoryId(cate.getCategoryId());
			cateDTO.setCategoryName(cate.getCategoryName());
			cateDTO.setCategoryImg(cate.getCategoryImg());
			cateDTO.setCreatedAt(cate.getCreatedAt());
			listCatesDTO.add(cateDTO);
		}

		return new PageImpl<>(listCatesDTO, pageable, pageOfCates.getTotalElements());
	}
}