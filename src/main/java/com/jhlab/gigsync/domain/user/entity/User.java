package com.jhlab.gigsync.domain.user.entity;

import com.jhlab.gigsync.domain.user.type.UserRole;
import com.jhlab.gigsync.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 15)
    private String nickName;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
