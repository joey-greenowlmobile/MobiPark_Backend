package com.greenowl.callisto.factory;

import com.greenowl.callisto.domain.Authority;
import com.greenowl.callisto.domain.User;

import java.util.Set;

public class UserFactory {

    private UserFactory() {
    }

    /**
     * Create A new User Entity.
     */
    public static User create(String login, String firstName, String lastName, String region,
                              String encryptedPassword, Set<Authority> authorities, String stripeToken) {
        User newUser = new User();
        newUser.setLogin(login);
        // new user gets initially a generated password, to avoid having to pick a password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setStripeToken(stripeToken);
        //for now, hard code
        String langKey = "en";
        newUser.setLangKey(langKey);
        // new user is active by default
        newUser.setActivated(true);
        // activation key is not needed for now.
        newUser.setActivationKey(null);
        newUser.setRegion(region);
        newUser.setAuthorities(authorities);
        return newUser;
    }


}
