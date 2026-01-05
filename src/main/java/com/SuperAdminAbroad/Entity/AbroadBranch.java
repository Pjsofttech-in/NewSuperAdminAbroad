package com.SuperAdminAbroad.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AbroadBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bid;

    @NotBlank(message = "Branch name is mandatory")
    private String branchName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Column(unique = true)
    private String branchEmail;
    private String branchCode;
    private String adminEmail;

    @NotNull
    private String contact;

    private String branchHeadName;
    private String address;
    private String city;
    private String district;
    private String state;
    private String country;

    @NotNull
    @Min(value = 100000, message = "Pincode should be 6 digits")
    @Max(value = 999999, message = "Pincode should be 6 digits")
    private Integer pincode;

    private String status;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be at least 8 characters")
    private String password;

    private boolean candGet;
    private boolean candPut;
    private boolean candPost;
    private boolean candDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "superAdmin_id")
    @JsonIgnore
    private AbroadSuperAdmin abroadSuperAdmin;

    @OneToMany(mappedBy = "abroadBranch",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AbroadStaff> staff;
}
