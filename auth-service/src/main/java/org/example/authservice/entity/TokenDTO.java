package org.example.authservice.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {
    private String token;
    private String refreshToken;
    private String tokenHead;
}
