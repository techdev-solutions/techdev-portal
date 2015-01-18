package de.techdev.portal.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class OauthAuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    public static final String TRACKR_RESOURCE_ID = "techdev-services";
    public static final String TRACKR_PAGE_CLIENT = "trackr-page";

    @Value("${trackr.pageRedirectUris}")
    private String trackrPageRedirectUris;

    @Autowired
    private DataSource dataSource;

    @Bean
    public ApprovalStore approvalStore() {
            return new JdbcApprovalStore(dataSource);
    }
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient(TRACKR_PAGE_CLIENT)
               .resourceIds(TRACKR_RESOURCE_ID)
                .authorizedGrantTypes("authorization_code", "implicit") //TODO: what to set here?
                .authorities("ROLE_CLIENT")
                .scopes("read", "write")
                .redirectUris(trackrPageRedirectUris.split(","));
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).approvalStore(approvalStore());
    }

}
