package com.SuperAdminAbroad.Serviceimpl;

import com.SuperAdminAbroad.DTO.StaffDTO;
import com.SuperAdminAbroad.Entity.AbroadBranch;
import com.SuperAdminAbroad.Entity.AbroadStaff;
import com.SuperAdminAbroad.Exception.ResourceNotFoundException;
import com.SuperAdminAbroad.Repository.BranchRepository;
import com.SuperAdminAbroad.Repository.StaffRepository;
import com.SuperAdminAbroad.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BranchRepository branchRepository;

    @Override
    public StaffDTO createStaff(StaffDTO dto, String branchEmail) {
        if (staffRepository.existsByStaffEmail(dto.getStaffEmail())) {
            throw new RuntimeException("⚠️ Staff email already exists");
        }

        AbroadBranch abroadBranch = branchRepository.findByBranchEmail(branchEmail)
                .orElseThrow(() -> new ResourceNotFoundException("❌ Branch not found with email: " + branchEmail));

        AbroadStaff staff = mapToEntity(dto);
        staff.setPassword(passwordEncoder.encode(dto.getPassword()));
        staff.setAbroadBranch(abroadBranch);
        staff.setBranchEmail(abroadBranch.getBranchEmail());
        staff.setBranchCode(abroadBranch.getBranchCode());
        staff.setAdminEmail(abroadBranch.getAdminEmail());


        AbroadStaff savedStaff = staffRepository.save(staff);
        return mapToDTO(savedStaff);
    }

    @Override
    public StaffDTO getStaffById(Long id) {
        AbroadStaff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
        return mapToDTO(staff);
    }

    @Override
    public List<StaffDTO> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StaffDTO> getAllStaff(String branchCode) {
        return staffRepository.findByBranchCode(branchCode).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StaffDTO updateStaff(Long id, StaffDTO dto) {
        AbroadStaff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));

        staff.setStaffName(dto.getStaffName() != null && !dto.getStaffName().isBlank() ? dto.getStaffName() : staff.getStaffName());
        staff.setStaffEmail(dto.getStaffEmail() != null && !dto.getStaffEmail().isBlank() ? dto.getStaffEmail() : staff.getStaffEmail());
        staff.setContact(dto.getContact() != null && !dto.getContact().isBlank() ? dto.getContact() : staff.getContact());
        staff.setAdminEmail(dto.getAdminEmail() != null && !dto.getAdminEmail().isBlank() ? dto.getAdminEmail() : staff.getAdminEmail());
        staff.setBranchCode(dto.getBranchCode() != null && !dto.getBranchCode().isBlank() ? dto.getBranchCode() : staff.getBranchCode());
        staff.setBranchEmail(dto.getBranchEmail() != null && !dto.getBranchEmail().isBlank() ? dto.getBranchEmail() : staff.getBranchEmail());



        return mapToDTO(staffRepository.save(staff));
    }


    @Override
    public void deleteStaff(Long id) {
        AbroadStaff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
        staffRepository.delete(staff);
    }

    @Override
    public Map<String, Boolean> getPermissionsByEmail(String staffEmail) {
        AbroadStaff staff = staffRepository.findAbroadStaffByStaffEmail(staffEmail)
                .orElseThrow(() -> new RuntimeException("Staff not found with email: " + staffEmail));

        Map<String, Boolean> permissions = new HashMap<>();
        permissions.put("cansGet", staff.isCansGet());
        permissions.put("cansPut", staff.isCansPut());
        permissions.put("cansPost", staff.isCansPost());
        permissions.put("cansDelete", staff.isCansDelete());

        return permissions;
    }

    private StaffDTO mapToDTO(AbroadStaff staff) {
        StaffDTO dto = new StaffDTO();
        dto.setId(staff.getId());
        dto.setStaffName(staff.getStaffName());
        dto.setStaffEmail(staff.getStaffEmail());
        dto.setContact(staff.getContact());
        dto.setPassword(staff.getPassword());
        dto.setAdminEmail(staff.getAdminEmail());
        dto.setBranchCode(staff.getBranchCode());
        dto.setBranchEmail(staff.getBranchEmail());
        dto.setCansGet(staff.isCansGet());
        dto.setCansPut(staff.isCansPut());
        dto.setCansPost(staff.isCansPost());
        dto.setCansDelete(staff.isCansDelete());
        dto.setStatus(staff.getStatus());
        return dto;
    }

    private AbroadStaff mapToEntity(StaffDTO dto) {
        AbroadStaff staff = new AbroadStaff();
        staff.setId(dto.getId());
        staff.setStaffName(dto.getStaffName());
        staff.setStaffEmail(dto.getStaffEmail());
        staff.setContact(dto.getContact());
        staff.setPassword(dto.getPassword());
        staff.setAdminEmail(dto.getAdminEmail());
        staff.setBranchCode(dto.getBranchCode());
        staff.setBranchEmail(dto.getBranchEmail());
        staff.setCansGet(dto.isCansGet());
        staff.setCansPut(dto.isCansPut());
        staff.setCansPost(dto.isCansPost());
        staff.setCansDelete(dto.isCansDelete());
        staff.setStatus(dto.getStatus());
        return staff;
    }
}