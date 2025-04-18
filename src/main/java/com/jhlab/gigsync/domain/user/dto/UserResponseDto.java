package com.jhlab.gigsync.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jhlab.gigsync.domain.user.entity.User;
import com.jhlab.gigsync.domain.user.type.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String email;
    private final String nickName;
    private final UserRole userRole;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    @Builder
    @JsonCreator
    public UserResponseDto(
            @JsonProperty("id") Long id,
            @JsonProperty("email") String email,
            @JsonProperty("nickName") String nickName,
            @JsonProperty("userRole") UserRole userRole,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("modifiedAt") LocalDateTime modifiedAt
    ) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.userRole = userRole;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickName(),
                user.getRole(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }
}
