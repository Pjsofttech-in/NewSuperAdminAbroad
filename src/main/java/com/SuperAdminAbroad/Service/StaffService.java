package com.SuperAdminAbroad.Service;

import com.SuperAdminAbroad.DTO.StaffDTO;

import java.util.List;
import java.util.Map;

public interface StaffService {
    StaffDTO createStaff(StaffDTO dto, String branchEmail);
    StaffDTO getStaffById(Long id);
    List<StaffDTO> getAllStaff();
    List<StaffDTO> getAllStaff(String branchCode);
    StaffDTO updateStaff(Long id, StaffDTO dto);
    void deleteStaff(Long id);

    Map<String, Boolean> getPermissionsByEmail(String staffEmail);
}