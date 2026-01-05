package com.SuperAdminAbroad.Repository;

import com.SuperAdminAbroad.DTO.BranchCodeNameDTO;
import com.SuperAdminAbroad.Entity.AbroadBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository  extends JpaRepository<AbroadBranch, Long> {
    boolean existsByBranchEmail(String branchEmail);

    Optional<AbroadBranch> findByBranchEmail(String branchEmail);

    int countByBranchEmail(String branchEmail);
    @Query("SELECT b FROM AbroadBranch b WHERE LOWER(b.branchEmail) = LOWER(:email) AND b.branchCode = :code")
    Optional<AbroadBranch> findAbroadBranchForLogin(@Param("email") String email, @Param("code") String code);

    @Query("SELECT b.branchCode, b.branchName FROM AbroadBranch b")
    List<Object[]> findAllBranchCodeAndNamePairs();


}
