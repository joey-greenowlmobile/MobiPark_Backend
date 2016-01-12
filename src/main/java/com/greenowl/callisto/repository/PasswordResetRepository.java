package com.greenowl.callisto.repository;

import com.greenowl.callisto.domain.PasswordResetRequest;
import com.greenowl.callisto.domain.User;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordResetRequest, Long> {

    Optional<PasswordResetRequest> findOneByResetToken(String token);

    @Query("select t from PasswordResetRequest t where t.expiresAt > ?1")
    List<PasswordResetRequest> findInvalidTokens(DateTime dateTime);

    @Query("select t from PasswordResetRequest t where t.resetUser = ?1")
    Optional<PasswordResetRequest> findOneByUser(User user);

    @Query("select r from PasswordResetRequest r where r.resetToken = ?1")
    Optional<PasswordResetRequest> findOneByToken(String token);
}
