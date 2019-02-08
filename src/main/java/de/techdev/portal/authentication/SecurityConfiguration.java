package de.techdev.portal.authentication;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import de.techdev.portal.authentication.google.GoogleOAuthCodeAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.sql.DataSource;

/**
 * Wraps the available {@link WebSecurityConfigurerAdapter} implementations.
 */
@Configuration
public class SecurityConfiguration {

    // The userDetailsManager @Bean needs to be outside of the WebSecurityConfigurerAdapter implementations
    // since the upgrade to Spring Boot 1.5.19. Otherwise the application does not start because of circular
    // dependencies from the JAR file!
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager detailsManager = new JdbcUserDetailsManager();
        detailsManager.setDataSource(dataSource);
        return detailsManager;
    }

    @Configuration
    @EnableWebSecurity
    @Profile("google-login")
    public class GoogleOAuthAuthentificationConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private AuthenticationEntryPoint entryPoint;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Value("${techdev.portal.google.client-id}")
        private String googleClientId;

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers(HttpMethod.GET, "/webjars/**", "/**/*.js", "/favicon.ico");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            RequestCache requestCache = new HttpSessionRequestCache();
            GoogleOAuthCodeAuthenticationFilter authenticationFilter =
                    new GoogleOAuthCodeAuthenticationFilter(requestCache, GoogleNetHttpTransport.newTrustedTransport(), googleClientId);
            authenticationFilter.setAuthenticationManager(authenticationManager);
            http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

            http.authorizeRequests()
                    .antMatchers("/login", "/", "/loggedOut").permitAll()
                    .antMatchers("/**").authenticated()
                    .and().logout().logoutSuccessUrl("/loggedOut").and()
                    .exceptionHandling()
                    .authenticationEntryPoint(entryPoint) // since we use a custom filter we have to set this redirect ourselves.
                    .and().requestCache().requestCache(requestCache); // since we need a reference in the custom filter we set it ourselves.
        }
    }

    @Configuration
    @EnableWebSecurity
    @Profile("!google-login")
    public class FormAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private DataSource dataSource;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.jdbcAuthentication().dataSource(dataSource);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers(HttpMethod.GET, "/webjars/**", "/favicon.ico");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/landing")
                    .and()
                    .logout().logoutSuccessUrl("/landing")
                    .and()
                    .authorizeRequests().antMatchers("/login").permitAll()
                    .antMatchers("/**").authenticated();
        }
    }
}
