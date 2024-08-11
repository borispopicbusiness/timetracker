package com.semiramide.timetracker.adapters.security.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminAccessTokenResponseBody {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("expires_in")
  private int accessTokenExpiresIn;

  @JsonProperty("refresh_expires_in")
  private int refreshTokenExpiresIn;
}
