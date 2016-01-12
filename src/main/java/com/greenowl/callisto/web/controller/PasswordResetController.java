package com.greenowl.callisto.web.controller;

import com.greenowl.callisto.domain.PasswordResetRequest;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.PasswordResetRepository;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.service.PasswordResetService;
import com.greenowl.callisto.web.rest.dto.ForgotPasswordRequest;
import com.greenowl.callisto.web.rest.dto.PasswordResetDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import java.util.Optional;

@Controller
@RequestMapping("/reset-password")
public class PasswordResetController {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetController.class);

    @Inject
    private PasswordResetRepository passwordResetRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private PasswordResetService passwordResetService;

    private static final String PASSWORD_RESET_VIEW = "change_pass";
    private static final String FORGOT_PASSWORD_VIEW = "forgot_password";

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ModelAndView changePassword(@ModelAttribute(value = "formRequest") PasswordResetDTO req, RedirectAttributes redirect) {
        String token = req.getToken();
        Optional<PasswordResetRequest> optional = passwordResetRepository.findOneByResetToken(token);
        if (!optional.isPresent()) {
            LOG.warn("Unable to find password reset token = {} request = {}", token, req);
            ModelAndView mav = new ModelAndView(FORGOT_PASSWORD_VIEW);
            mav.addObject("forgotPassReq", new ForgotPasswordRequest());
            return mav;
        }
        if (!validPassword(req.getPassword())) {
            ModelAndView mav = new ModelAndView(PASSWORD_RESET_VIEW);
            mav.addObject("error", "Password must be greater than 6 characters.");
            mav.addObject("formRequest", new PasswordResetDTO(token));
        }
        LOG.debug("Processing token = {} for new password = {}", req.getToken(), req.getPassword());
        PasswordResetRequest savedRequestToken = optional.get();
        User user = savedRequestToken.getResetUser();
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);
        passwordResetRepository.delete(savedRequestToken); // Expire Token
        ModelAndView mav = new ModelAndView("redirect:/welcome");
        redirect.addFlashAttribute("banner", "Password Successfully changed!");
        return mav;
    }

    private boolean validPassword(String s) {
        return !StringUtils.isEmpty(s) && s.length() >= 6;
    }

    @RequestMapping("/{token}")
    public ModelAndView resetToken(@PathVariable("token") final String token) {
        LOG.info("Request for password reset with token = {}", token);
        if (!passwordResetService.valid(token)) {
            ModelAndView mav = new ModelAndView(FORGOT_PASSWORD_VIEW);
            mav.addObject("forgotPassReq", new ForgotPasswordRequest());
            return mav;
        }
        ModelAndView mav = new ModelAndView(PASSWORD_RESET_VIEW);
        mav.addObject("formRequest", new PasswordResetDTO(token));
        return mav;
    }

}
