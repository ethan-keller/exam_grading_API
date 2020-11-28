package nl.tudelft.sem10.authenticationservice.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// A LOT OF INFO FROM https://dzone.com/articles/spring-boot-security-json-web-tokenjwt-hello-world

/**
 * General Spring Security configuration class.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // use of transient for PMD
    @Autowired
    private transient JwtTokenFilter jwtTokenFilter;
    @Autowired
    private transient JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    /**
     * Creates a new service that handles user details.
     *
     * @return a new user details service
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    /**
     * Creates a new password encoder.
     *
     * @return a new password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Gets the Spring Bean for the authentication manager.
     *
     * @return authentication manager bean
     * @throws Exception if fetching fails
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Configures HTTP security.
     * Disables CSRF
     * Sets STATELESS policy
     * Configures endpoint authorizations based on roles
     * Configures Exception handling
     * Adds token filter in Spring Security Filter Chain
     *
     * @param http HTTP security configuration
     * @throws Exception if configuration fails.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // disable CSRF
        http = http.csrf().disable();

        // set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // set permissions
        http.authorizeRequests()
                //      .antMatchers("/").hasAnyAuthority("STUDENT", "TEACHER")
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/student").hasAnyAuthority("STUDENT")
                .antMatchers("/teacher").hasAnyAuthority("TEACHER")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);

        // add JWT token filter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
