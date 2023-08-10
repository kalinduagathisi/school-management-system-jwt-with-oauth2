package com.example.dto.responseDto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorMsgResponseDto {
    private boolean success;
    private int status;
    private String msg;
}

