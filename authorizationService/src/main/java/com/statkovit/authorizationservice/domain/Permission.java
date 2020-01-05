package com.statkovit.authorizationservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "permission")
@SequenceGenerator(name = "default_gen", sequenceName = "permission_id_seq", allocationSize = 1)
@Getter
@Setter
public class Permission {

    public static final int MAX_LENGTH__PERMISSION = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = MAX_LENGTH__PERMISSION)
    private String name;
}
