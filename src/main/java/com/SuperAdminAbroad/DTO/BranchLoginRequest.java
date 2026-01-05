package com.SuperAdminAbroad.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchLoginRequest {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    // 🔹 Optional field (no @NotBlank)
    private String branchCode;

    public BranchLoginRequest() {}

    public BranchLoginRequest(String email, String password, String branchCode) {
        this.email = email;
        this.password = password;
        this.branchCode = branchCode;
    }
}
