package com.jhlab.gigsync.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessagePayloadDto {
    private String receiverId;
    private String content;
}
