package com.SuperAdminAbroad.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BranchLoginResponse {
    private String token;
    private Map<String, Object> data;
    private String massage;
}
