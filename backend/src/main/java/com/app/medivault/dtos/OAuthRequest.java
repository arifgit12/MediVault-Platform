package com.app.medivault.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthRequest {
    private String provider; // GOOGLE, APPLE
    private String idToken;
    private String email;
    private String name;
}
