package de.techdev.portal.core;

import de.techdev.portal.google.GoogleOAuthCodeAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Bean
    public GoogleOAuthCodeAuthenticationFilter googleAuthenticationFilter(RequestCache requestCache) {
        GoogleOAuthCodeAuthenticationFilter authenticationFilter = new GoogleOAuthCodeAuthenticationFilter(requestCache);
        authenticationFilter.setAuthenticationManager(authenticationManager);
        return authenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        RequestCache requestCache = new HttpSessionRequestCache();
        http.addFilterBefore(googleAuthenticationFilter(requestCache), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
            .antMatchers("/oauth/**", "/showUser").fullyAuthenticated()
            .and().csrf().disable()
            .logout().logoutSuccessUrl("/").and()
            .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/")) // since we use a custom filter we have to set this redirect ourselves.
            .and().requestCache().requestCache(requestCache) // since we need a reference in the custom filter we set it ourselves.
        ;
    }
}
