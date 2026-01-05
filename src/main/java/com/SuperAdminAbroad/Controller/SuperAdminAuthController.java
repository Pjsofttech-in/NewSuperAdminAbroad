package com.SuperAdminAbroad.Controller;

import com.SuperAdminAbroad.Entity.AbroadSuperAdmin;
import com.SuperAdminAbroad.JWT.JwtUtil;
import com.SuperAdminAbroad.Service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "https://wayabroad.in")
@RequestMapping("/superAdmin")
@RequiredArgsConstructor
public class SuperAdminAuthController {

    @Autowired
    private SuperAdminService superAdminService;

    @Autowired
    private JwtUtil jwtService;

    @PostMapping("/register")
    public ResponseEntity<AbroadSuperAdmin> register(@RequestBody AbroadSuperAdmin admin) {
        return ResponseEntity.ok(superAdminService.saveSuperAdmin(admin));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<AbroadSuperAdmin> getById(@PathVariable Long id) {
        return ResponseEntity.ok(superAdminService.getSuperAdminById(id));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AbroadSuperAdmin>> getAll() {
        return ResponseEntity.ok(superAdminService.getAllSuperAdmins());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AbroadSuperAdmin> update(@PathVariable Long id, @RequestBody AbroadSuperAdmin admin) {
        return ResponseEntity.ok(superAdminService.updateSuperAdmin(id, admin));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        superAdminService.deleteSuperAdmin(id);
        return ResponseEntity.ok("SuperAdmin deleted successfully");
    }

    @GetMapping("/existByEmail")
    public ResponseEntity<Boolean> doesSuperAdminEmailExist(@RequestParam String email) {
        return ResponseEntity.ok(superAdminService.superAdminEmailExists(email));
    }

    @GetMapping("/permissionForAdmin")
    public Map<String, Boolean> getPermissionsByEmail(@RequestParam String email) {
        return superAdminService.getPermissionsByEmail(email);
    }

}