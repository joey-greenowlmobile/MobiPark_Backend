package com.greenowl.callisto.service;

import com.greenowl.callisto.domain.PaymentProfile;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.factory.PaymentProfileFactory;
import com.greenowl.callisto.repository.PaymentProfileRepository;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.service.util.PaymentProfileUtil;
import com.greenowl.callisto.web.rest.dto.PaymentProfileDTO;
import com.greenowl.callisto.web.rest.dto.payment.CardProfile;
import com.greenowl.callisto.web.rest.dto.payment.CreatePaymentProfileRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class StripeAccountService {

    private final Logger LOG = LoggerFactory.getLogger(StripeAccountService.class);

    @Inject
    private PaymentProfileRepository paymentProfileRepository;

    @Inject
    private UserRepository userRepository;

    public PaymentProfile getPaymentProfileById(Long id) {
        return paymentProfileRepository.getPaymentProfileById(id);
    }

    public PaymentProfileDTO registerPaymentProfile(CreatePaymentProfileRequest req, String email, String cardToken) {
        return createPaymentInformation(req.getCard(), req.getCreated(), req.getId(), req.getLiveMode(), req.getUsed(), email, cardToken);
    }

    private PaymentProfileDTO createPaymentInformation(CardProfile card, String created, String id, Boolean livemode, Boolean used, String login, String cardToken) {
        Optional<User> optUser = userRepository.findOneByLogin(login);
        if (optUser.isPresent()) {
            User user = optUser.get();
            PaymentProfile savedProfile = PaymentProfileFactory.create(card, created, livemode, used, user, cardToken);
            paymentProfileRepository.save(savedProfile);
            return PaymentProfileUtil.getPaymentProfileDTO(savedProfile);
        }
        return null;
    }

    public List<PaymentProfileDTO> getAllPaymentProfileDTOs(User user) {
        List<PaymentProfileDTO> paymentProfileDTOs = new ArrayList<>();
        List<PaymentProfile> paymentProfiles = paymentProfileRepository.getPaymentProfilesByUser(user);
        paymentProfileDTOs.addAll(paymentProfiles.stream().map(PaymentProfileUtil::getPaymentProfileDTO).collect(Collectors.toList()));
        return paymentProfileDTOs;
    }

}