package com.SuperAdminAbroad.Service;

import com.SuperAdminAbroad.DTO.BranchCodeNameDTO;
import com.SuperAdminAbroad.DTO.BranchDTO;

import java.util.List;
import java.util.Map;

public interface BranchService {
    BranchDTO createBranch(BranchDTO dto, String superAdminEmail);
    BranchDTO getBranchById(Long id);
    List<BranchDTO> getAllBranches();
    BranchDTO updateBranch(Long id, BranchDTO dto);
    void deleteBranch(Long id);

    Map<String, Boolean> getPermissionsByEmail(String branchEmail);

    Map<String, String> getBranchCodeNameMap();

}