package nl.tudelft.sem10.authenticationservice.domain;

import nl.tudelft.sem10.authenticationservice.application.UserDetailsServiceImpl;
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

    /**
     * Sets the user details service and password encoder to the authentication manager.
     *
     * @param auth builder that builds the authentication manager
     * @throws Exception if building of authentication manager goes wrong
     */
    @Autowired
    public void configureAuthManager(AuthenticationManagerBuilder auth) throws Exception {
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
        http = disableCsrf(http);

        // set session management to stateless
        http = setStateless(http);

        // set permissions
        setPermissions(http);

        // add JWT token filter
        addJwtFilter(http);
    }

    private HttpSecurity disableCsrf(HttpSecurity http) throws Exception {
        return http.csrf().disable();
    }

    private HttpSecurity setStateless(HttpSecurity http) throws Exception {
        return http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and();
    }

    private void setPermissions(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/encode/**")
            .permitAll()
            .antMatchers("/validate/**")
            .permitAll()
            .antMatchers("/getToken")
            .permitAll()
            .antMatchers("/student").hasAnyAuthority("STUDENT")
            .antMatchers("/teacher").hasAnyAuthority("TEACHER")
            .anyRequest().authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }

    private void addJwtFilter(HttpSecurity http) {
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
