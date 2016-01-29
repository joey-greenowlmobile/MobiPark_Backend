package com.greenowl.callisto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "T_USER")
public class User extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;
    @NotNull
    @Email
    @Size(min = 5, max = 255)
    @Column(length = 255, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @Size(min = 0, max = 100)
    @Column(length = 100)
    private String password;

    @Size(min = 0, max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(min = 0, max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Size(min = 0 , max = 50)
    @Column(name = "license_plate", length = 30, unique = true)
    private String licensePlate;
    @Size(min = 0, max = 20)
    @Column(name = "mobile_number", length = 30, unique = true)
    private String mobileNumber;

    private boolean activated = true;

    @Size(min = 2, max = 5)
    @Column(name = "lang_key", length = 5)
    private String langKey;

    @Column(name = "activation_key")
    private Integer activationKey;

    private String region;
    
    @Column(name ="stripe_token")
    private String stripeToken;
    @JsonIgnore
    @OneToMany(mappedBy = "owner", targetEntity = Device.class, fetch = FetchType.LAZY)
    private Set<Device> devices = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "T_USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authorities = new HashSet<>();


    @JsonIgnore
    @OneToMany(mappedBy = "profileHolder", targetEntity = PaymentProfile.class, fetch = FetchType.LAZY)
    private Set<PaymentProfile> paymentProfiles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", targetEntity = PlanSubscription.class, fetch = FetchType.LAZY)
    private Set<PlanSubscription> planSubscriptions = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "activityHolder", targetEntity = ParkingSaleActivity.class, fetch = FetchType.LAZY)
    private Set<ParkingSaleActivity> ParkingSaleAcitivities = new HashSet<>();

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Integer getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(Integer activationKey) {
        this.activationKey = activationKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
    
    public Set<PaymentProfile> getPaymentProfiles() {
		return paymentProfiles;
	}
    
	public Set<PlanSubscription> getPlanSubscriptions() {
		return planSubscriptions;
	}

	public void setPlanSubscriptions(Set<PlanSubscription> planSubscriptions) {
		this.planSubscriptions = planSubscriptions;
	}
    
	public void setPaymentProfiles(Set<PaymentProfile> paymentProfiles) {
		this.paymentProfiles = paymentProfiles;
	}

	public String getStripeToken() {
		return stripeToken;
	}

	public void setStripeToken(String stripeToken) {
		this.stripeToken = stripeToken;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return login.equals(user.login);

    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }


    
}