package com.SuperAdminAbroad.Repository;

import com.SuperAdminAbroad.Entity.AbroadSuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuperAdminRepository extends JpaRepository<AbroadSuperAdmin, Long> {
    Optional<AbroadSuperAdmin> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<AbroadSuperAdmin> findByEmailIgnoreCase(String email);

}
