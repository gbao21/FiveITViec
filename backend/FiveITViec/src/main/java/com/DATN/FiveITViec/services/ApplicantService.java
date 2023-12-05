package com.DATN.FiveITViec.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.DATN.FiveITViec.dto.ApplicantDTO;
import com.DATN.FiveITViec.model.Applicant;
import com.DATN.FiveITViec.repository.ApplicantRepository;

@Service
public class ApplicantService {
	@Autowired
	private ApplicantRepository applicantRepository;

	public Page<ApplicantDTO> getAllApplicantByJobId(String jobId, Pageable pageable) {
		Page<Applicant> pageOfApply = applicantRepository.findAllApplicantByJobId(jobId, pageable);

		List<ApplicantDTO> listApplicantDTO = new ArrayList<>();
		for (Applicant apply : pageOfApply.getContent()) {
			ApplicantDTO applyDTO = new ApplicantDTO();
			applyDTO.setApplicantId(apply.getApplicantId());
			applyDTO.setCoverletter(apply.getCoverletter());
			applyDTO.setCv(apply.getCv());
			applyDTO.setEmail(apply.getEmail());
			applyDTO.setFullName(apply.getFullName());
			applyDTO.setJobId(apply.getJob().getJobId());
			applyDTO.setPhoneNumber(apply.getPhoneNumber());
			applyDTO.setStatus(apply.getStatus());
			applyDTO.setUserId(apply.getUser().getUserId());
			applyDTO.setCreatedAt(apply.getCreatedAt());
			listApplicantDTO.add(applyDTO);
		}

		return new PageImpl<>(listApplicantDTO, pageable, pageOfApply.getTotalElements());
	}
}
