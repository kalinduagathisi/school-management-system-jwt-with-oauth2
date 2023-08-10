package com.example.dto.responseDto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommonResponseDTO {
    private boolean success;
    private String msg;
    private Object body;
}
