package com.SuperAdminAbroad.Controller;

import com.SuperAdminAbroad.DTO.BranchCodeNameDTO;
import com.SuperAdminAbroad.DTO.BranchDTO;
import com.SuperAdminAbroad.Service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "https://wayabroad.in")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping("/createBranch")
    public ResponseEntity<BranchDTO> createBranch(@RequestBody BranchDTO dto,
                                                  @RequestParam("superAdminEmail") String superAdminEmail) {
        BranchDTO createdBranch = branchService.createBranch(dto,superAdminEmail);
        return ResponseEntity.ok(createdBranch);
    }

    @GetMapping("/getBranchById/{id}")
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @GetMapping("/getAllBranches")
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @PutMapping("/updateBranch/{id}")
    public ResponseEntity<BranchDTO> updateBranch(@PathVariable Long id, @RequestBody BranchDTO dto) {
        return ResponseEntity.ok(branchService.updateBranch(id, dto));
    }

    @DeleteMapping("/deleteBranch/{id}")
    public ResponseEntity<String> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok("Branch deleted successfully");
    }

    @GetMapping("/permissionForBranch")
    public Map<String, Boolean> getPermissionsByEmail(@RequestParam String branchEmail) {
        return branchService.getPermissionsByEmail(branchEmail);
    }

    @GetMapping("/getBranchCodeNameMap")
    public ResponseEntity<Map<String, String>> getBranchCodeNameMap() {
        return ResponseEntity.ok(branchService.getBranchCodeNameMap());
    }


}