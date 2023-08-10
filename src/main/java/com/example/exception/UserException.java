package com.example.exception;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserException extends RuntimeException {
   private int status;
   private String msg;
}
