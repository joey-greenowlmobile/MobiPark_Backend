package com.greenowl.callisto.service;

import com.greenowl.callisto.domain.Authority;
import com.greenowl.callisto.domain.User;
import com.greenowl.callisto.repository.AuthorityRepository;
import com.greenowl.callisto.repository.UserRepository;
import com.greenowl.callisto.security.AuthoritiesConstants;
import com.greenowl.callisto.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Optional;

/**
 * Service class for managing users.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class UserService {

    private final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Async
    public void updateUserInformation(String login, String firstName, String lastName, String region) {
        Optional<User> optional = userRepository.findOneByLogin(login);
        if (!optional.isPresent()) {
            LOG.warn("Unable to update user information for login = {}", login);
            return;
        }
        User user = optional.get();
        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (region != null) user.setRegion(region);
        userRepository.save(user);
        LOG.debug("Changed Information for User: {}", user);
    }

    /**
     * Determine if a String input is a valid Email Address.
     *
     * @param email
     * @return
     */
    private boolean isValidEmailAddress(String email) {
        if (email == null) {
            return false;
        }
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void changePassword(String password) {
        User currentUser = getCurrentUser();
        String encryptedPassword = passwordEncoder.encode(password);
        currentUser.setPassword(encryptedPassword);
        userRepository.save(currentUser);
        LOG.debug("Changed password for User: {}", currentUser);
    }

    public void changePassword(String login, String password) {
        User user = getUser(login);
        if (user == null) return;
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        LOG.debug("Changed password for User: {}", user);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User currentUser = getCurrentUser();
        currentUser.getAuthorities().size(); // eagerly load the association
        return currentUser;
    }

    @Transactional(readOnly = true)
    public User getUserWithDevices() {
        return getUserWithDevices(SecurityUtils.getCurrentLogin());
    }

    @Transactional(readOnly = true)
    public User getUserWithDevices(String login) {
        User user = getUser(login);
        if (user.getDevices() != null) {
            user.getDevices().size();// eagerly load the association
        }
        return user;
    }

    /**
     * Retrieve user from data store and fully load all associations
     *
     * @return
     */
    @Transactional(readOnly = true)
    public User getFatUser(String login) {
        User user = getUser(login);
        user.getDevices().size();
        user.getAuthorities().size();
        return user;
    }

    /**
     * Retrieves currently authenticated user without doing any additional table joins.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        return getUser(SecurityUtils.getCurrentLogin());
    }

    /**
     * Pull user from data store with login = :login without any further joins.
     *
     * @param login
     * @return
     */
    public User getUser(String login) {
        return userRepository.findOneByLogin(login).get();
    }


    /**
     * Remove a user from data store, return true if user is successfully removed.
     *
     * @param user
     * @return
     */
    @Transactional
    public boolean deleteUser(User user) {
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.ADMIN);

        User foundUser = userRepository.findOneByLogin(user.getLogin()).get();
        foundUser.getAuthorities().size();

        if (foundUser.getAuthorities().contains(authority)) {
            return false;
        }
        userRepository.delete(foundUser);
        return !userRepository.exists(foundUser.getId());
    }


}
