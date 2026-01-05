package com.SuperAdminAbroad.Repository;

import com.SuperAdminAbroad.Entity.AbroadStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<AbroadStaff, Long> {
    boolean existsByStaffEmail(String staffEmail);
    Optional<AbroadStaff> findAbroadStaffByStaffEmail(String staffEmail);
    Optional<AbroadStaff> findByStaffEmailAndBranchCode(String email, String branchCode);
    List<AbroadStaff> findByBranchCode(String branchCode);
    Optional<AbroadStaff> findByStaffEmail(String staffEmail);




}
