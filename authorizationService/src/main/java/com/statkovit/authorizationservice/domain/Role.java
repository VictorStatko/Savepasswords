package com.statkovit.authorizationservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@SequenceGenerator(name = "default_gen", sequenceName = "role_id_seq", allocationSize = 1)
@Getter
@Setter
public class Role implements GrantedAuthority {

    public static final int MAX_LENGTH__AUTHORITY = 32;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "authority", nullable = false, length = MAX_LENGTH__AUTHORITY)
    private String authority;

    @ManyToMany
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();
}
