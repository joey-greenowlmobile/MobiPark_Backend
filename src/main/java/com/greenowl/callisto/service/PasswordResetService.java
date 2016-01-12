package com.greenowl.callisto.service;

import com.greenowl.callisto.domain.PasswordResetRequest;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.PasswordResetRepository;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.security.password.PasswordResetTokenProvider;
import com.greenowl.callisto.security.password.ResetPasswordToken;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

@Service
public class PasswordResetService {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetService.class);

    @Inject
    private PasswordResetRepository passwordResetRepository;

    @Inject
    private PasswordResetTokenProvider tokenProvider;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    public PasswordResetService() {
    }

    public boolean valid(String token) {
        if (StringUtils.isEmpty(token)) return false;
        Optional<PasswordResetRequest> tokenOptional = passwordResetRepository.findOneByResetToken(token);
        if (!tokenOptional.isPresent()) return false;
        PasswordResetRequest resetToken = tokenOptional.get();
        Long expiryTimestamp = resetToken.getExpiresAt();
        DateTime expirationDate = new DateTime(expiryTimestamp);
        return !DateTime.now().isAfter(expirationDate);
    }


}
