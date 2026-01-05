package com.SuperAdminAbroad.Serviceimpl;

import com.SuperAdminAbroad.Entity.AbroadBranch;
import com.SuperAdminAbroad.Entity.AbroadStaff;
import com.SuperAdminAbroad.Entity.AbroadSuperAdmin;
import com.SuperAdminAbroad.Exception.ResourceNotFoundException;
import com.SuperAdminAbroad.Repository.BranchRepository;
import com.SuperAdminAbroad.Repository.StaffRepository;
import com.SuperAdminAbroad.Repository.SuperAdminRepository;
import com.SuperAdminAbroad.Service.EmailService;
import com.SuperAdminAbroad.Service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

    @Autowired
    private SuperAdminRepository repository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private StaffRepository staffRepository;


    private final Map<String, String> otpStorage = new HashMap<>();

    @Override
    public AbroadSuperAdmin saveSuperAdmin(AbroadSuperAdmin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setCansPut(true);
        admin.setCansDelete(true);
        admin.setCansGet(true);
        admin.setCansPost(true);
        return repository.save(admin);
    }

    @Override
    public Optional<AbroadSuperAdmin> authenticate(String email, String rawPassword) {
        Optional<AbroadSuperAdmin> adminOpt = repository.findByEmail(email);
        if (adminOpt.isPresent() && passwordEncoder.matches(rawPassword, adminOpt.get().getPassword())) {
            return adminOpt;
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AbroadSuperAdmin admin = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }

    // Inject your password encoder

    @Override
    public AbroadSuperAdmin updateSuperAdmin(Long id, AbroadSuperAdmin updatedAdmin) {
        AbroadSuperAdmin existingAdmin = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SuperAdmin not found with id: " + id));

        // Update email
        existingAdmin.setEmail(
                updatedAdmin.getEmail() != null && !updatedAdmin.getEmail().isBlank()
                        ? updatedAdmin.getEmail()
                        : existingAdmin.getEmail()
        );

        // Update name
        existingAdmin.setName(
                updatedAdmin.getName() != null && !updatedAdmin.getName().isBlank()
                        ? updatedAdmin.getName()
                        : existingAdmin.getName()
        );

        // Update password (if provided)
        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isBlank()) {
            existingAdmin.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
        }

        return repository.save(existingAdmin);
    }


    @Override
    public void deleteSuperAdmin(Long id) {
        repository.deleteById(id);
    }

    @Override
    public AbroadSuperAdmin getSuperAdminById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SuperAdmin not found with id: " + id));
    }

    @Override
    public List<AbroadSuperAdmin> getAllSuperAdmins() {
        return repository.findAll();
    }

    @Override
    public boolean superAdminEmailExists(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Map<String, Boolean> getPermissionsByEmail(String email) {
        AbroadSuperAdmin admin = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("SuperAdmin not found with email: " + email));

        Map<String, Boolean> permissions = new HashMap<>();
        permissions.put("cansGet", admin.isCansGet());
        permissions.put("cansPut", admin.isCansPut());
        permissions.put("cansPost", admin.isCansPost());
        permissions.put("cansDelete", admin.isCansDelete());

        return permissions;
    }

    @Override
    public ResponseEntity<String> sendOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(9000) + 1000); // 4-digit OTP

        boolean userFound = false;

        if (repository.existsByEmail(email)) {
            userFound = true;
        } else if (branchRepository.existsByBranchEmail(email)) {
            userFound = true;
        } else if (staffRepository.existsByStaffEmail(email)) {
            userFound = true;
        }

        if (!userFound) {
            return ResponseEntity.badRequest().body("Email not registered");
        }

        otpStorage.put(email, otp);

        // ✅ Construct clean, plain-text email body
        String subject = "Your OTP Code";
        String body = "Dear User,\n\nYour OTP for password reset is: " + otp + "\n\nThank you,\nTeam SuperAdmin Abroad";

        // ✅ Send OTP via email
        emailService.sendEmail(email, subject, body);

        return ResponseEntity.ok("OTP sent successfully to " + email);
    }


    @Override
    public ResponseEntity<String> resetPassword(String email, String otp, String newPassword) {
        if (!otpStorage.containsKey(email)) {
            return ResponseEntity.badRequest().body("No OTP found for this email");
        }

        String storedOtp = otpStorage.get(email);
        if (!storedOtp.equals(otp)) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        // Check and update password for SuperAdmin
        Optional<AbroadSuperAdmin> superAdminOpt = repository.findByEmail(email);
        if (superAdminOpt.isPresent()) {
            AbroadSuperAdmin admin = superAdminOpt.get();
            admin.setPassword(passwordEncoder.encode(newPassword));
            repository.save(admin);
            otpStorage.remove(email);
            return ResponseEntity.ok("SuperAdmin password reset successfully");
        }

        // Check and update password for Branch
        Optional<AbroadBranch> branchOpt = branchRepository.findByBranchEmail(email);
        if (branchOpt.isPresent()) {
            AbroadBranch abroadBranch = branchOpt.get();
            abroadBranch.setPassword(passwordEncoder.encode(newPassword));
            branchRepository.save(abroadBranch);
            otpStorage.remove(email);
            return ResponseEntity.ok("Branch password reset successfully");
        }

        // Check and update password for Staff
        Optional<AbroadStaff> staffOpt = staffRepository.findAbroadStaffByStaffEmail(email);
        if (staffOpt.isPresent()) {
            AbroadStaff staff = staffOpt.get();
            staff.setPassword(passwordEncoder.encode(newPassword));
            staffRepository.save(staff);
            otpStorage.remove(email);
            return ResponseEntity.ok("Staff password reset successfully");
        }

        return ResponseEntity.status(404).body("User not found for email: " + email);
    }
}