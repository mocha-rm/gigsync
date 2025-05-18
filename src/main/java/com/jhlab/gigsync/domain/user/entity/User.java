package com.jhlab.gigsync.domain.user.entity;

import com.jhlab.gigsync.domain.board.entity.Board;
import com.jhlab.gigsync.domain.comment.entity.Comment;
import com.jhlab.gigsync.domain.user.type.UserRole;
import com.jhlab.gigsync.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;

    private boolean isEmailVerified;

    @Column(length = 100)
    private String password;

    @Column(length = 15)
    private String nickName;

    @Column(length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public User(String email, String password, String nickName, String phoneNumber, boolean isEmailVerified) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.isEmailVerified = isEmailVerified;
        this.role = UserRole.NORMAL;
    }

    public void updateNickname(String nickName) {
        this.nickName = nickName;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void registerAdminAccount() {
        this.role = UserRole.ADMIN;
    }
}
