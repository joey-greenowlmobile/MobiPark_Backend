package com.greenowl.callisto.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "T_PASSWORD_RESET_REQUEST")
public class PasswordResetRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "reset_token")
    @NotNull
    private String resetToken;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reset_pass_user_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private User resetUser; // The user responsible requesting this reset token

    @Column(name = "expires_at", nullable = false)
    private Long expiresAt;

    public PasswordResetRequest() {}

    public PasswordResetRequest(String resetToken, User resetUser, Long expiresAt) {
        this.resetToken = resetToken;
        this.resetUser = resetUser;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getResetUser() {
        return resetUser;
    }

    public void setResetUser(User resetUser) {
        this.resetUser = resetUser;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordResetRequest that = (PasswordResetRequest) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (resetToken != null ? !resetToken.equals(that.resetToken) : that.resetToken != null) return false;
        if (resetUser != null ? !resetUser.equals(that.resetUser) : that.resetUser != null) return false;
        return !(expiresAt != null ? !expiresAt.equals(that.expiresAt) : that.expiresAt != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (resetToken != null ? resetToken.hashCode() : 0);
        result = 31 * result + (resetUser != null ? resetUser.hashCode() : 0);
        result = 31 * result + (expiresAt != null ? expiresAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PasswordResetRequest{" +
                "id=" + id +
                ", resetToken='" + resetToken + '\'' +
                ", resetUser=" + resetUser +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
