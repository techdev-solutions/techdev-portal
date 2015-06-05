package de.techdev.portal.domain.user.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static de.techdev.portal.core.OauthAuthorizationServerConfiguration.TRACKR_PAGE_CLIENT;

@Service
class TokenDeleter {

    @Autowired
    private TokenStore tokenStore;

    /**
     * Delete all access tokens for a user belonging to the trackr page client.
     */
    void deleteTrackrAngularClientTokens(String username) {
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(TRACKR_PAGE_CLIENT, username);
        for (OAuth2AccessToken token : tokens) {
            tokenStore.removeAccessToken(token);
        }
    }
}
