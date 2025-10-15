package com.sprint.mission.discodeit.auth.jwt;


import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// (1)
@Component
public class JwtTokenProvider  {
  @Getter
  @Value("${jwt.key}")
  private String secretKey;

  @Getter
  @Value("${jwt.access-token-expiration-minutes}")
  private int accessTokenExpirationMinutes;

  @Getter
  @Value("${jwt.refresh-token-expiration-minutes}")
  private int refreshTokenExpirationMinutes;

  //액세스 토큰 발급
  public String generateAccessToken(Map<String, Object> claims,
      String subject) {
    try {
      JWSSigner signer = new MACSigner(secretKey.getBytes(StandardCharsets.UTF_8));

      Date expiration = new Date(System.currentTimeMillis() + (long) accessTokenExpirationMinutes * 60 * 1000L);

      JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
          .subject(subject)
          .claim("roles", claims.get("roles"))
          .expirationTime(expiration)
          .issueTime(new Date())
          .build();

      SignedJWT signedJWT = new SignedJWT(
          new JWSHeader(JWSAlgorithm.HS256),
          claimsSet
      );

      signedJWT.sign(signer);
      return signedJWT.serialize();
    } catch (Exception e) {
      throw new RuntimeException("JWT 발급 실패", e);
    }
  }

  //리프레쉬 토큰 발급
  public String generateRefreshToken(String subject) {
    try {
      JWSSigner signer = new MACSigner(secretKey.getBytes(StandardCharsets.UTF_8));

      Date expiration = new Date(System.currentTimeMillis() + (long) refreshTokenExpirationMinutes * 60 * 1000L);

      JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
          .subject(subject)
          .expirationTime(expiration)
          .issueTime(new Date())
          .build();

      SignedJWT signedJWT = new SignedJWT(
          new JWSHeader(JWSAlgorithm.HS256),
          claimsSet
      );

      signedJWT.sign(signer);
      return signedJWT.serialize();
    } catch (Exception e) {
      throw new RuntimeException("JWT 발급 실패", e);
    }
  }

  // 토큰 검증
  public boolean validateToken(String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(secretKey.getBytes(StandardCharsets.UTF_8));
      boolean verified = signedJWT.verify(verifier);

      Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
      return verified && expiration.after(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  public Map<String, Object> getClaims(String token) {
    if (validateToken(token)) {
      throw new RuntimeException("JWT 검증 실패");
    }
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
      return claimsSet.getClaims();
    } catch (Exception e) {
      throw new RuntimeException("JWT 파싱 실패", e);
    }
  }
}
