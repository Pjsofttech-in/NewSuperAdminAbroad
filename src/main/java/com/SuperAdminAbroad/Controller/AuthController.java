package com.SuperAdminAbroad.Controller;

import com.SuperAdminAbroad.DTO.*;
import com.SuperAdminAbroad.Entity.AbroadBranch;
import com.SuperAdminAbroad.Entity.AbroadStaff;
import com.SuperAdminAbroad.Entity.AbroadSuperAdmin;
import com.SuperAdminAbroad.JWT.AuthRequest;
import com.SuperAdminAbroad.JWT.AuthResponse;
import com.SuperAdminAbroad.JWT.JwtUtil;
import com.SuperAdminAbroad.Repository.BranchRepository;
import com.SuperAdminAbroad.Repository.StaffRepository;
import com.SuperAdminAbroad.Repository.SuperAdminRepository;
import com.SuperAdminAbroad.Service.SuperAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "https://wayabroad.in")
@RestController
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private SuperAdminService superAdminService;

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private StaffRepository staffRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        System.out.println("🔐 SuperAdmin Login Attempt - Email: " + request.getEmail());

        // Null check
        if (request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Email and Password must not be null"
            ));
        }

        Optional<AbroadSuperAdmin> optionalAdmin =
                superAdminRepository.findByEmailIgnoreCase(request.getEmail().toLowerCase());

        if (optionalAdmin.isEmpty()) {
            System.out.println("❌ No SuperAdmin found for provided email.");
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Invalid email or password"
            ));
        }

        AbroadSuperAdmin admin = optionalAdmin.get();

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            System.out.println("❌ Password mismatch for: " + admin.getEmail());
            return ResponseEntity.status(401).body(Map.of(
                    "message", "Invalid email or password"
            ));
        }

        // Generate JWT token
        String token = jwtUtil.generateTokenFromEmail(admin.getEmail());

        // Build response map
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", new AuthResponse(token, admin.getEmail()));
        responseBody.put("message", "successful");

        return ResponseEntity.ok(responseBody);
    }



    @PostMapping("/branchlogin")
    public ResponseEntity<?> branchLogin(@Valid @RequestBody BranchLoginRequest authRequest) {

        // Debug log
        System.out.println("🔐 Login Attempt - Email: " + authRequest.getEmail() + ", Branch Code: " + authRequest.getBranchCode());

        // Null validation
        if (authRequest.getEmail() == null || authRequest.getBranchCode() == null || authRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Email, Branch Code, and Password must not be null");
        }

        Optional<AbroadBranch> optionalBranch = branchRepository.findAbroadBranchForLogin(
                authRequest.getEmail().toLowerCase(), authRequest.getBranchCode());

        if (optionalBranch.isEmpty()) {
            System.out.println("❌ No branch found for provided email/code.");
            return ResponseEntity.status(401).body("Invalid credentials: Email or branch code not found");
        }

        AbroadBranch abroadBranch = optionalBranch.get();

        if (!passwordEncoder.matches(authRequest.getPassword(), abroadBranch.getPassword())) {
            System.out.println("❌ Password mismatch for: " + abroadBranch.getBranchEmail());
            return ResponseEntity.status(401).body("Invalid credentials: Incorrect password");
        }

        // Generate JWT token
        String token = jwtUtil.generateTokenFromEmail(abroadBranch.getBranchEmail());

        // Response data
        Map<String, Object> data = new HashMap<>();
        data.put("bid", abroadBranch.getBid());
        data.put("branchEmail", abroadBranch.getBranchEmail());
        data.put("branchCode", abroadBranch.getBranchCode());
        data.put("branchName", abroadBranch.getBranchName());
//        data.put("message", "successful");

        String massage= "successful";
        BranchLoginResponse response = new BranchLoginResponse(token, data,massage);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stafflogin")
    public ResponseEntity<?> staffLogin(@Valid @RequestBody BranchLoginRequest authRequest) {
        System.out.println("🔐 Staff Login Attempt - Email: " + authRequest.getEmail());

        if (authRequest.getEmail() == null || authRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Email and Password must not be null");
        }

        Optional<AbroadStaff> optionalStaff;

        // ✅ If branchCode is provided → check both email + branchCode
        if (authRequest.getBranchCode() != null && !authRequest.getBranchCode().isBlank()) {
            optionalStaff = staffRepository.findByStaffEmailAndBranchCode(
                    authRequest.getEmail().toLowerCase(), authRequest.getBranchCode());
        } else {
            // ✅ If branchCode is NOT provided → check only email
            optionalStaff = staffRepository.findByStaffEmail(authRequest.getEmail().toLowerCase());
        }

        if (optionalStaff.isEmpty()) {
            System.out.println("❌ No staff found for provided credentials.");
            return ResponseEntity.status(401).body("Invalid credentials: Email or Branch code not found");
        }

        AbroadStaff staff = optionalStaff.get();

        if (!passwordEncoder.matches(authRequest.getPassword(), staff.getPassword())) {
            System.out.println("❌ Password mismatch for: " + staff.getStaffEmail());
            return ResponseEntity.status(401).body("Invalid credentials: Incorrect password");
        }

        // ✅ Update status to Active on successful login
        staff.setStatus("Active");
        staffRepository.save(staff);

        // Generate JWT token
        String token = jwtUtil.generateTokenFromEmail(staff.getStaffEmail());

        // Response Data
        Map<String, Object> data = new HashMap<>();
        data.put("id", staff.getId());
        data.put("staffName", staff.getStaffName());
        data.put("staffEmail", staff.getStaffEmail());
        data.put("branchCode", staff.getBranchCode());   // auto-fetched from DB
        data.put("branchEmail", staff.getBranchEmail());
        data.put("adminEmail", staff.getAdminEmail());
        data.put("status", staff.getStatus());

        String message = "successful";

        return ResponseEntity.ok(new BranchLoginResponse(token, data, message));
    }

    @PostMapping("/stafflogout")
    public ResponseEntity<?> staffLogout(@RequestParam String email) {
        Optional<AbroadStaff> optionalStaff = staffRepository.findByStaffEmail(email.toLowerCase());

        if (optionalStaff.isEmpty()) {
            return ResponseEntity.status(404).body("Staff not found");
        }

        AbroadStaff staff = optionalStaff.get();
        staff.setStatus("Inactive");
        staffRepository.save(staff);

        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        return superAdminService.sendOtp(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {
        return superAdminService.resetPassword(email, otp, newPassword);
    }

}