package com.SuperAdminAbroad.DTO;

import lombok.Data;

@Data
public class StaffDTO {
    private Long id;
    private String staffName;
    private String staffEmail;
    private String contact;
    private String password;
    private String status;
    private String adminEmail;
    private String branchCode;
    private String branchEmail;
    private boolean cansGet;
    private boolean cansPut;
    private boolean cansPost;
    private boolean cansDelete;
}