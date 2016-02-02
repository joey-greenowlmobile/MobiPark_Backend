package com.greenowl.callisto.service.register;

import com.greenowl.callisto.config.Constants;
import com.greenowl.callisto.domain.Authority;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.factory.UserFactory;
import com.greenowl.callisto.repository.AuthorityRepository;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.service.MailService;
import com.greenowl.callisto.service.util.UserUtil;
import com.greenowl.callisto.web.rest.dto.UserDTO;
import com.greenowl.callisto.web.rest.dto.user.CreateUserRequest;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.greenowl.callisto.security.AuthoritiesConstants.USER;

@Service
public class RegistrationService {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    public UserDTO register(CreateUserRequest req, String stripeToken) {
        return createUserInformation(req.getEmail(), req.getFirstName(), req.getLastName(),
                req.getRegion(), req.getLicensePlate(), req.getMobileNumber(), req.getPassword(), stripeToken);
    }

    public String stripeRegister(CreateUserRequest req) {
        Stripe.apiKey = Constants.STRIPE_TEST_KEY;
        Map<String, Object> customerParams = new HashMap<String, Object>();
        if (req.getFirstName() != null | req.getLastName() != null) {
            customerParams.put("description", req.getFirstName() + req.getLastName());
        }
        customerParams.put("email", req.getEmail());
        try {
            Customer cu = Customer.create(customerParams);
            return cu.getId();
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
                | APIException e) {
            return null;
        }

    }

    /**
     * Create a new user and persist that user into the data store.
     *
     * @param email
     * @param firstName
     * @param lastName
     * @return newUser
     */
    private UserDTO createUserInformation(String email, String firstName, String lastName,
                                          String region, String licensePlate, String mobileNumber, String desiredPassword, String stripeToken) {
        Authority authority = authorityRepository.findOne(USER);
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        String encryptedPassword = passwordEncoder.encode(desiredPassword);
        User newUser = UserFactory.create(email, firstName, lastName, region, licensePlate, mobileNumber, encryptedPassword, authorities, stripeToken);
        LOG.debug("Created Information for User: {}", newUser);
        User savedUser = userRepository.save(newUser);
        UserDTO dto = UserUtil.getUserDTO(savedUser);
        LOG.info("Returning newly created user {}", dto);
        return dto;
    }


}
