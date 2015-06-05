package de.techdev.portal.domain.user.update;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Deletage for a @{UserDetails} class for our edit user page.
 */
class UpdateUserModel {

    private final UserDetails userDetails;

    public UpdateUserModel(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getUsername() {
        return userDetails.getUsername();
    }

    public boolean isEnabled() {
        return userDetails.isEnabled();
    }

    public boolean hasAuthority(String roleName) {
        return userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals(roleName));
    }
}
