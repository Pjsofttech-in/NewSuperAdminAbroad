package com.SuperAdminAbroad.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {
    private Long bid;
    private String branchName;
    private String branchEmail;
    private String branchCode;
    private String contact;
    private String branchHeadName;
    private String adminEmail;
    private String address;
    private String city;
    private String district;
    private String state;
    private String country;
    private Integer pincode;
    private String status;
    private String password;
    private boolean candGet;
    private boolean candPut;
    private boolean candPost;
    private boolean candDelete;
}
