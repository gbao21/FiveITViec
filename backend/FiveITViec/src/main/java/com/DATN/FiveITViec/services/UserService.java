package com.DATN.FiveITViec.services;

import com.DATN.FiveITViec.dto.UserProfileDTO;
import com.DATN.FiveITViec.repository.ProfileCandidateRepository;
import com.DATN.FiveITViec.repository.ProfileEmployerRepository;
import com.DATN.FiveITViec.repository.RolesRepository;
import com.DATN.FiveITViec.repository.UserRepository;
import com.DATN.FiveITViec.constants.FiveITConstants;
import com.DATN.FiveITViec.dto.LoginDTO;

import com.DATN.FiveITViec.dto.UserManagementDTO;
import com.DATN.FiveITViec.model.ProfileCandidate;
import com.DATN.FiveITViec.model.ProfileEmployer;
import com.DATN.FiveITViec.model.Specialization;
import com.DATN.FiveITViec.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RolesRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ProfileCandidateRepository profileCandidateRepository;

	@Autowired
	private ProfileEmployerRepository profileEmployerRepository;

	public boolean existUser(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null && user.getUserId() > 0) {
			return true;
		}
		return false;
	}

	public String checkLogin(LoginDTO loginDTO) {
		User user = userRepository.findByEmail(loginDTO.getEmail());
		if (user != null && user.getUserId() > 0) {
			String encodedPassword = user.getPassword();
			boolean check = passwordEncoder.matches(loginDTO.getPassword(), encodedPassword);
			if (!check) {
				return FiveITConstants.NO_PASSWORD;
			} else if (user.getStatus().equals(FiveITConstants.DISABLE_STATUS)) {
				return FiveITConstants.DISABLE_STATUS;
			}
		} else {
			return FiveITConstants.NO_EMAIL;
		}
		return FiveITConstants.OK;
	}

	public String updatePassword(User user) {
		String newPass = generateRandomPassword();
		user.setPassword(passwordEncoder.encode(newPass));
		user = userRepository.save(user);
		if (user != null && user.getUserId() >= 0) {
			return newPass;
		}
		return null;
	}

	public String generateRandomPassword() {
		String specialChars = "!@#$%^&*()_-+=<>?/[]{},.";
		String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
		String numberChars = "0123456789";

		List<Character> passwordChars = new ArrayList<>();

		// Add one character from each group
		passwordChars.add(getRandomChar(specialChars));
		passwordChars.add(getRandomChar(uppercaseChars));
		passwordChars.add(getRandomChar(lowercaseChars));
		passwordChars.add(getRandomChar(numberChars));

		// Generate the remaining characters randomly
		for (int i = 4; i < 6; i++) {
			String allChars = specialChars + uppercaseChars + lowercaseChars + numberChars;
			char randomChar = allChars.charAt((int) (Math.random() * allChars.length()));
			passwordChars.add(randomChar);
		}
		Collections.shuffle(passwordChars);
		StringBuilder password = new StringBuilder();
		for (Character character : passwordChars) {
			password.append(character);
		}
		return password.toString();
	}

	private char getRandomChar(String charSet) {
		return charSet.charAt((int) (Math.random() * charSet.length()));
	}

	public Page<UserManagementDTO> getAllUsersByRoles(String managementRole, String email, String approval,
			LocalDate startDate, LocalDate endDate, String status, Pageable pageable) {
		Page<User> pageOfUsers = userRepository.findUserByRolesAndEmailAndApprovalContaining(managementRole, email,
				approval, startDate, endDate, status, pageable);

		List<UserManagementDTO> listUserProfileDTO = new ArrayList<>();

		for (User user : pageOfUsers.getContent()) {
			if (user.getRoles() != null) {
				if (managementRole.equals(FiveITConstants.CANDIDATE)
						&& user.getRoles().getRoleName().equals(FiveITConstants.CANDIDATE)
						&& user.getApproval().equals(FiveITConstants.APPROVAL_STATUS)) {
					ProfileCandidate profileCandidate = profileCandidateRepository
							.findProfileCandidateByUserId(user.getUserId());
					Set<Specialization> specializations = user.getSpecializations();
					UserManagementDTO userManagementDTO = new UserManagementDTO(user, profileCandidate,
							specializations);
					listUserProfileDTO.add(userManagementDTO);
				} else if (managementRole.equals(FiveITConstants.EMPLOYER)
						&& user.getRoles().getRoleName().equals(FiveITConstants.EMPLOYER)
						&& user.getApproval().equals(FiveITConstants.APPROVAL_STATUS)) {
					ProfileEmployer profileEmployer = profileEmployerRepository
							.findProfileEmployerByUserId(user.getUserId());
					Set<Specialization> specializations = user.getSpecializations();
					UserManagementDTO userManagementDTO = new UserManagementDTO(user, profileEmployer, specializations);
					listUserProfileDTO.add(userManagementDTO);
				} else if (managementRole.equals(FiveITConstants.EMPLOYER)
						&& user.getRoles().getRoleName().equals(FiveITConstants.EMPLOYER)
						&& user.getApproval().equals(FiveITConstants.WAITING_STATUS)) {
					ProfileEmployer profileEmployer = profileEmployerRepository
							.findProfileEmployerByUserId(user.getUserId());
					Set<Specialization> specializations = user.getSpecializations();
					UserManagementDTO userManagementDTO = new UserManagementDTO(user, profileEmployer, specializations);
					listUserProfileDTO.add(userManagementDTO);
				}
			}
		}

		return new PageImpl<>(listUserProfileDTO, pageable, pageOfUsers.getTotalElements());
	}

	public long countCandidatesByMonth(Integer year, Integer month, String roleName) {
		return userRepository.countUserByRole(year, month, roleName);
	}

	public boolean checkPasswordThenChange(String pwdUserEnter, String newPassword, User user) {
		boolean check = passwordEncoder.matches(pwdUserEnter, user.getPassword());
		if (check) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return true;
		} else {
			return false;
		}
	}

	public Page<UserManagementDTO> getEmployerByStatusApproval(String companyName,String address, Pageable pageable) {

        // Gọi phương thức userRepository.findEmployersWithCompanyName với các tham số.
        Page<User> pageOfUser = userRepository.findEmployersWithCompanyName(companyName,address,pageable);

        // Tiếp tục xử lý kết quả theo yêu cầu của bạn.
        List<UserManagementDTO> list = new ArrayList<>();
        for (User user : pageOfUser.getContent()) {
            ProfileEmployer profileEmployer = profileEmployerRepository.findProfileEmployerByUserId(user.getUserId());
            Set<Specialization> specializations = user.getSpecializations();
            UserManagementDTO userManagementDTO = new UserManagementDTO(user, profileEmployer, specializations);
            list.add(userManagementDTO);
        }
        return new PageImpl<>(list, pageable, pageOfUser.getTotalElements());
    }


	public List<UserProfileDTO> getLastedEmployer() {
		List<User> listUser = userRepository.findLatestRecordsWithLimit();
		List<UserProfileDTO> list = new ArrayList<>();
		for (User user : listUser.subList(0, Math.min(5, listUser.size()))) {
			UserProfileDTO profileDTO = new UserProfileDTO();
			ProfileEmployer profileEmployer = profileEmployerRepository.findProfileEmployerByUserId(user.getUserId());
			profileDTO.setCompanyName(profileEmployer.getCompanyName());
			profileDTO.setEmail(profileEmployer.getUser().getEmail());
			profileDTO.setAddress(profileEmployer.getAddress());
			profileDTO.setCompanyLogo(profileEmployer.getCompanyLogo());
			profileDTO.setPhoneNumber(profileEmployer.getPhoneNumber());
			profileDTO.setTaxNumber(profileEmployer.getTaxNumber());
			list.add(profileDTO);
		}
		return list;
	}


}
