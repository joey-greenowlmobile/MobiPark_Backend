package com.greenowl.callisto.repository;

import com.greenowl.callisto.domain.User;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.activationKey = ?1")
    User getUserByActivationKey(Integer activationKey);

    @Query("select u from User u where u.activated = false and u.createdDate > ?1")
    List<User> findNotActivatedUsersByCreationDateBefore(DateTime dateTime);

    User findOneByActivationKey(String activationKey);

    Optional<User> findOneByLogin(String login);

    @Query("select u from User u where u.login = ?1")
    User findSingleUserByLogin(String login);

    Optional<User> findOneByMobileNumber(String mobileNumber);

}
