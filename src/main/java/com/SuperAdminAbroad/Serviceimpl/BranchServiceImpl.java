package com.SuperAdminAbroad.Serviceimpl;

import com.SuperAdminAbroad.DTO.BranchCodeNameDTO;
import com.SuperAdminAbroad.DTO.BranchDTO;
import com.SuperAdminAbroad.Entity.AbroadBranch;
import com.SuperAdminAbroad.Entity.AbroadSuperAdmin;
import com.SuperAdminAbroad.Exception.ResourceNotFoundException;
import com.SuperAdminAbroad.Repository.BranchRepository;
import com.SuperAdminAbroad.Repository.SuperAdminRepository;
import com.SuperAdminAbroad.Service.BranchService;
import com.SuperAdminAbroad.Service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private SuperAdminService superAdminService;

    private static final String PREFIX = "BCH";
    private static final Random RANDOM = new Random();

    private static final int MAX_BRANCH_LIMIT = 5;

    @Override
    public BranchDTO createBranch(BranchDTO dto, String superAdminEmail) {
        // Check if branch email already exists
        if (branchRepository.existsByBranchEmail(dto.getBranchEmail())) {
            throw new RuntimeException("Branch email already exists");
        }

        // Fetch SuperAdmin using email
        AbroadSuperAdmin abroadSuperAdmin = superAdminRepository.findByEmail(superAdminEmail)
                .orElseThrow(() -> new RuntimeException("SuperAdmin with email not found: " + superAdminEmail));

        // Map DTO to entity
        AbroadBranch abroadBranch = mapToEntity(dto);
        abroadBranch.setBranchCode(generateBranchCode());
        abroadBranch.setAbroadSuperAdmin(abroadSuperAdmin);
        abroadBranch.setAdminEmail(superAdminEmail);
        abroadBranch.setPassword(passwordEncoder.encode(abroadBranch.getPassword()));

        AbroadBranch savedAbroadBranch = branchRepository.save(abroadBranch);
        return mapToDTO(savedAbroadBranch);
    }


    private String generateBranchCode() {
        int randomNum = RANDOM.nextInt(900) + 100; // 3-digit code
        return PREFIX + randomNum;
    }

    private AbroadBranch mapToEntity(BranchDTO dto) {
        AbroadBranch b = new AbroadBranch();
        b.setBid(dto.getBid());
        b.setBranchName(dto.getBranchName());
        b.setBranchEmail(dto.getBranchEmail());
        b.setContact(dto.getContact());
        b.setBranchHeadName(dto.getBranchHeadName());
        b.setAddress(dto.getAddress());
        b.setCity(dto.getCity());
        b.setDistrict(dto.getDistrict());
        b.setState(dto.getState());
        b.setCountry(dto.getCountry());
        b.setPincode(dto.getPincode());
        b.setStatus(dto.getStatus());
        b.setPassword(dto.getPassword());
        b.setCandDelete(dto.isCandDelete());
        b.setCandPut(dto.isCandPut());
        b.setCandPost(dto.isCandPost());
        b.setCandGet(dto.isCandGet());
        b.setAdminEmail(dto.getAdminEmail());
        return b;
    }

    private BranchDTO mapToDTO(AbroadBranch abroadBranch) {
        BranchDTO dto = new BranchDTO();
        dto.setBid(abroadBranch.getBid());
        dto.setBranchName(abroadBranch.getBranchName());
        dto.setBranchEmail(abroadBranch.getBranchEmail());
        dto.setBranchCode(abroadBranch.getBranchCode());
        dto.setContact(abroadBranch.getContact());
        dto.setBranchHeadName(abroadBranch.getBranchHeadName());
        dto.setAddress(abroadBranch.getAddress());
        dto.setCity(abroadBranch.getCity());
        dto.setDistrict(abroadBranch.getDistrict());
        dto.setState(abroadBranch.getState());
        dto.setCountry(abroadBranch.getCountry());
        dto.setPincode(abroadBranch.getPincode());
        dto.setStatus(abroadBranch.getStatus());
        dto.setPassword(abroadBranch.getPassword());
        dto.setCandGet(abroadBranch.isCandGet());
        dto.setCandPost(abroadBranch.isCandPost());
        dto.setCandPut(abroadBranch.isCandPut());
        dto.setCandDelete(abroadBranch.isCandDelete());
        dto.setAdminEmail(abroadBranch.getAdminEmail());
        return dto;
    }

    @Override
    public BranchDTO getBranchById(Long id) {
        AbroadBranch abroadBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
        return mapToDTO(abroadBranch);
    }

    @Override
    public List<BranchDTO> getAllBranches() {
        return branchRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public BranchDTO updateBranch(Long id, BranchDTO dto) {
        AbroadBranch abroadBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));

        abroadBranch.setBranchName(dto.getBranchName() != null ? dto.getBranchName() : abroadBranch.getBranchName());
        abroadBranch.setBranchEmail(dto.getBranchEmail() != null ? dto.getBranchEmail() : abroadBranch.getBranchEmail());
        abroadBranch.setContact(dto.getContact() != null ? dto.getContact() : abroadBranch.getContact());
        abroadBranch.setBranchHeadName(dto.getBranchHeadName() != null ? dto.getBranchHeadName() : abroadBranch.getBranchHeadName());
        abroadBranch.setAddress(dto.getAddress() != null ? dto.getAddress() : abroadBranch.getAddress());
        abroadBranch.setCity(dto.getCity() != null ? dto.getCity() : abroadBranch.getCity());
        abroadBranch.setDistrict(dto.getDistrict() != null ? dto.getDistrict() : abroadBranch.getDistrict());
        abroadBranch.setState(dto.getState() != null ? dto.getState() : abroadBranch.getState());
        abroadBranch.setCountry(dto.getCountry() != null ? dto.getCountry() : abroadBranch.getCountry());
        abroadBranch.setPincode(dto.getPincode() != null ? dto.getPincode() : abroadBranch.getPincode());
        abroadBranch.setStatus(dto.getStatus() != null ? dto.getStatus() : abroadBranch.getStatus());
        abroadBranch.setPassword(dto.getPassword() != null ? passwordEncoder.encode(dto.getPassword()) : passwordEncoder.encode(abroadBranch.getPassword()));

        return mapToDTO(branchRepository.save(abroadBranch));
    }


    @Override
    public void deleteBranch(Long id) {
        AbroadBranch abroadBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
        branchRepository.delete(abroadBranch);
    }

    @Override
    public Map<String, Boolean> getPermissionsByEmail(String branchEmail) {
        AbroadBranch abroadBranch = branchRepository.findByBranchEmail(branchEmail)
                .orElseThrow(() -> new RuntimeException("Branch not found with email: " + branchEmail));

        Map<String, Boolean> permissions = new HashMap<>();
        permissions.put("candGet", abroadBranch.isCandGet());
        permissions.put("candPut", abroadBranch.isCandPut());
        permissions.put("candPost", abroadBranch.isCandPost());
        permissions.put("candDelete", abroadBranch.isCandDelete());

        return permissions;
    }

    @Override
    public Map<String, String> getBranchCodeNameMap() {
        List<Object[]> results = branchRepository.findAllBranchCodeAndNamePairs();
        Map<String, String> map = new HashMap<>();

        for (Object[] row : results) {
            String code = (String) row[0];
            String name = (String) row[1];
            map.put(code, name);
        }

        return map;
    }



}
