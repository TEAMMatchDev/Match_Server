package com.example.matchinfrastructure.oauth.apple.service;

import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.OtherServerException;
import com.example.matchcommon.properties.AppleProperties;
import com.example.matchinfrastructure.oauth.apple.client.AppleFeignClient;
import com.example.matchinfrastructure.oauth.apple.dto.AppleAuthTokenResponse;
import com.example.matchinfrastructure.oauth.apple.dto.ApplePublicResponse;
import com.example.matchinfrastructure.oauth.apple.dto.AppleUserRes;
import com.example.matchinfrastructure.oauth.apple.dto.Key;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.matchcommon.constants.MatchStatic.CLASS_PATH;
import static com.example.matchcommon.constants.MatchStatic.KID;
import static com.example.matchcommon.exception.errorcode.OtherServerErrorCode.OTHER_SERVER_BAD_REQUEST;
import static com.example.matchcommon.exception.errorcode.OtherServerErrorCode.OTHER_SERVER_INTERNAL_SERVER_ERROR;
import static com.example.matchinfrastructure.oauth.apple.exception.AppleErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppleAuthService {
    private final AppleFeignClient appleFeignClient;
    private final AppleProperties appleProperties;


    public AppleUserRes appleLogin(String identityToken) {
        JsonParser parser = new JsonParser();
        ApplePublicResponse applePublicResponse = appleFeignClient.getPublicKey();

        System.out.println(applePublicResponse.getKeys().get(0).getKid());

        String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));

        String header = new String(Base64.getDecoder().decode(headerOfIdentityToken), StandardCharsets.UTF_8);

        JsonObject parseHeader = (new Gson()).fromJson(header, JsonObject.class);
        String kidValue = parseHeader.get("kid").getAsString();
        String algValue = parseHeader.get("alg").getAsString();
        Key foundKey = getMatchedKeyBy(kidValue, algValue, applePublicResponse.getKeys());

        PublicKey publicKey = getPublicKey(foundKey);

        Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();
        JsonObject userInfoObject = (JsonObject) parser.parse(new Gson().toJson(userInfo));
        String iss = userInfoObject.get("iss").getAsString();
        String aud = userInfoObject.get("aud").getAsString();

        checkValidationInfo(iss, aud);

        String appleId  = String.valueOf(userInfoObject.get("sub"));
        String email = String.valueOf(userInfoObject.get("email"));

        return new AppleUserRes(email, appleId);
    }

    private void checkValidationInfo(String iss, String aud) {
        if (!Objects.equals(iss, "https://appleid.apple.com"))
            throw new BadRequestException(APPLE_BAD_REQUEST);

        if (!Objects.equals(aud, "com.dev.match"))
            throw new BadRequestException(APPLE_BAD_REQUEST);
    }


    public Key getMatchedKeyBy(String kid, String alg, List<Key> keys) {
        return keys.stream()
                .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
                .findFirst().orElseThrow(()->new BadRequestException(MISMATCH_APPLE_KEY));
    }


    public PublicKey getPublicKey(Key key) {
        String nStr = key.getN();
        String eStr = key.getE();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(0));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(0));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception exception) {
            throw new BadRequestException(FAIL_MAKE_PUBLIC_KEY);
        }
    }

    public void revokeUser(String code) {

        String appleAuthToken = generateAuthToken(code);

        if (appleAuthToken != null) {
            System.out.println(appleAuthToken);
            RestTemplate restTemplate = new RestTemplateBuilder().build();
            String revokeUrl = "https://appleid.apple.com/auth/revoke";

            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("client_id", appleProperties.getBundleId());
            params.add("client_secret", createClientSecret());
            params.add("token", appleAuthToken);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            restTemplate.postForEntity(revokeUrl, httpEntity, String.class);
        }

    }

    private String generateAuthToken(String code) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String authUrl = "https://appleid.apple.com/auth/token";
        String secret = createClientSecret();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", appleProperties.getBundleId());
        log.info("secret: " + secret);
        params.add("client_secret", secret);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        log.info(params.toString());
        log.info(String.valueOf(httpEntity));
        try {
            ResponseEntity<AppleAuthTokenResponse> response = restTemplate.postForEntity(authUrl, httpEntity, AppleAuthTokenResponse.class);
            log.info("상태코드:"+response.getStatusCode());
            log.info("토큰:"+response.getBody().getAccess_token());
            return response.getBody().getAccess_token();
        } catch (HttpClientErrorException e) {
            log.error(String.valueOf(e));
            log.error(e.getMessage());
            throw new OtherServerException(OTHER_SERVER_BAD_REQUEST);
        }
    }

    private String createClientSecret(){
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> jwtHeader = new HashMap<>();

        jwtHeader.put("kid", KID);
        jwtHeader.put("alg", "ES256");

        return Jwts.builder()
                .setHeaderParams(jwtHeader)
                .setIssuer(appleProperties.getTeamId())
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간 - UNIX 시간
                .setExpiration(expirationDate) // 만료 시간
                .setAudience("https://appleid.apple.com")
                .setSubject(appleProperties.getBundleId())
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey(){
        ClassPathResource resource = new ClassPathResource(CLASS_PATH);
        try (InputStream inputStream = resource.getInputStream()) {
            String privateKeyPEM = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            try (PEMParser pemParser = new PEMParser(new StringReader(privateKeyPEM))) {
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
                PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
                return converter.getPrivateKey(object);
            } catch (IOException e) {
                log.error(String.valueOf(e));
                throw new OtherServerException(OTHER_SERVER_BAD_REQUEST);
            }
        } catch (IOException e) {
            log.error(String.valueOf(e));
            throw new OtherServerException(OTHER_SERVER_BAD_REQUEST);
        }
    }
}
