package com.SuperAdminAbroad.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AbroadSuperAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    private String password;
    private String name;

    @OneToMany(mappedBy = "abroadSuperAdmin",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AbroadBranch> abroadBranches;

    private boolean cansGet;
    private boolean cansPut;
    private boolean cansPost;
    private boolean cansDelete;

}
