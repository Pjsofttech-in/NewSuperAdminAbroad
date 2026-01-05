package com.SuperAdminAbroad.Controller;

import com.SuperAdminAbroad.DTO.BranchDTO;
import com.SuperAdminAbroad.DTO.StaffDTO;
import com.SuperAdminAbroad.Service.BranchService;
import com.SuperAdminAbroad.Service.StaffService;
import com.SuperAdminAbroad.Service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "https://wayabroad.in")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private SuperAdminService superAdminService;

    @PostMapping("/createStaff")
    public ResponseEntity<StaffDTO> createStaff(
            @RequestBody StaffDTO dto,
            @RequestParam String branchEmail) {
        return ResponseEntity.ok(staffService.createStaff(dto, branchEmail));
    }


    @GetMapping("/getStaffById/{id}")
    public ResponseEntity<StaffDTO> getStaffById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @GetMapping("/getAllStaff")
    public ResponseEntity<List<StaffDTO>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    ///get all staff with branch code
    @GetMapping("/getAllStaffWithBranchCode")
    public ResponseEntity<List<StaffDTO>> getAllStaff(@RequestParam String branchCode) {
        List<StaffDTO> staffList = staffService.getAllStaff(branchCode);
        return ResponseEntity.ok(staffList);
    }

    @PutMapping("/updateStaff/{id}")
    public ResponseEntity<StaffDTO> updateStaff(@PathVariable Long id, @RequestBody StaffDTO dto) {
        return ResponseEntity.ok(staffService.updateStaff(id, dto));
    }

    @DeleteMapping("/deleteStaff/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok("Staff deleted successfully");
    }

    @GetMapping("/permissionForStaff")
    public Map<String, Boolean> getPermissionsByEmail(@RequestParam String staffEmail) {
        return staffService.getPermissionsByEmail(staffEmail);
    }

    @GetMapping("/branch/getbranchcode")
    public ResponseEntity<String> getBranchCodeByEmail(@RequestParam String email) {
        String branchCode = branchService
                .getAllBranches()
                .stream()
                .filter(branch -> branch.getBranchEmail().equalsIgnoreCase(email))
                .map(BranchDTO::getBranchCode)
                .findFirst()
                .orElse(null);

        return ResponseEntity.ok(branchCode);
    }

    @GetMapping("/staff/getbranchcode")
    public ResponseEntity<String> getBranchCodeByStaffEmail(@RequestParam String email) {
        String branchCode = staffService
                .getAllStaff()
                .stream()
                .filter(staff -> staff.getStaffEmail().equalsIgnoreCase(email))
                .map(StaffDTO::getBranchCode)
                .findFirst()
                .orElse(null);

        return ResponseEntity.ok(branchCode);
    }

}
