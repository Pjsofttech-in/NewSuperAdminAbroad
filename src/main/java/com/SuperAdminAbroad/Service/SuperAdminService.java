package com.SuperAdminAbroad.Service;

import com.SuperAdminAbroad.Entity.AbroadSuperAdmin;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SuperAdminService extends UserDetailsService {
    AbroadSuperAdmin saveSuperAdmin(AbroadSuperAdmin admin);
    Optional<AbroadSuperAdmin> authenticate(String email, String rawPassword);
    AbroadSuperAdmin updateSuperAdmin(Long id, AbroadSuperAdmin admin);
    void deleteSuperAdmin(Long id);
    AbroadSuperAdmin getSuperAdminById(Long id);
    List<AbroadSuperAdmin> getAllSuperAdmins();
    boolean superAdminEmailExists(String email);
    Map<String, Boolean> getPermissionsByEmail(String email);

    ResponseEntity<String> sendOtp(String email);
    ResponseEntity<String> resetPassword(String email, String otp, String newPassword);
}