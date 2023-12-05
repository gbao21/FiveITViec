package com.DATN.FiveITViec.services;

import com.DATN.FiveITViec.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecializationService {
    @Autowired
    SpecializationRepository specializationRepository;

    public void deleteSpecializationById(Long id) {
        specializationRepository.deleteById(id);
    }


}
