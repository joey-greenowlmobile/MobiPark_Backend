package com.greenowl.callisto.security.password;

import org.joda.time.DateTime;
import org.springframework.security.crypto.codec.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordResetTokenProvider {

    private String secretKey;
    private int tokenValidity;

    public PasswordResetTokenProvider(String secretKey, int tokenValidity) {
        this.secretKey = secretKey;
        this.tokenValidity = tokenValidity;
    }

    public ResetPasswordToken getResetToken(String email) {
        return createToken(email, DateTime.now().getMillis());
    }

    private ResetPasswordToken createToken(String email, long currentTime) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        String resetToken = new String(Hex.encode(digest.digest((email + ":" + currentTime + ":" + secretKey).getBytes())));
        long validity = System.currentTimeMillis() + 1000L * tokenValidity;
        DateTime expireDate = DateTime.now().plus(validity);
        return new ResetPasswordToken(resetToken, expireDate);
    }

}
