package de.techdev.portal.domain.user.create;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

class CreateUserRequest {

    private String firstName;

    private String lastName;

    private boolean enabled;

    private List<SimpleGrantedAuthority> roles = new ArrayList<>();

    @NotEmpty
    @Email
    private String email;

    private String federalState;

    private boolean withTrackr;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFederalState() {
        return federalState;
    }

    public void setFederalState(String federalState) {
        this.federalState = federalState;
    }

    public boolean isWithTrackr() {
        return withTrackr;
    }

    public void setWithTrackr(boolean withTrackr) {
        this.withTrackr = withTrackr;
    }

    public List<SimpleGrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(List<SimpleGrantedAuthority> roles) {
        this.roles = roles;
    }
}
