package com.example.matchinfrastructure.oauth.apple.service;

import com.example.matchcommon.exception.BadRequestException;
import com.example.matchinfrastructure.oauth.apple.client.AppleFeignClient;
import com.example.matchinfrastructure.oauth.apple.dto.ApplePublicResponse;
import com.example.matchinfrastructure.oauth.apple.dto.AppleUserRes;
import com.example.matchinfrastructure.oauth.apple.dto.Key;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static com.example.matchinfrastructure.oauth.apple.exception.AppleErrorCode.*;

@Service
@RequiredArgsConstructor
public class AppleAuthService {
    private final AppleFeignClient appleFeignClient;
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

        String appleId  = userInfoObject.get("sub").getAsString();
        String email = userInfoObject.get("email").getAsString();

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
}
