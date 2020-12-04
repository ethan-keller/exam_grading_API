package nl.tudelft.sem10.authenticationservice.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.tudelft.sem10.authenticationservice.application.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

/**
 * Test suite for JWT token security filter.
 */
class JwtTokenFilterTest {

    private final transient JwtTokenFilter filter = new JwtTokenFilter();
    @Mock
    private transient HttpServletRequest req;
    @Mock
    private transient HttpServletResponse res;
    @Mock
    private transient FilterChain filterChain;
    @Mock
    private transient JwtTokenUtil utils;
    @Mock
    private transient UserDetailsServiceImpl detailsService;

    /**
     * Mock set up and field injection.
     *
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        // initialize mock objects
        MockitoAnnotations.initMocks(this);

        // set mocked token utils field
        Field utilField = filter.getClass().getDeclaredField("jwtTokenUtil");
        utilField.setAccessible(true);
        utilField.set(filter, utils);

        // set mocked user detail service field
        Field userServiceField = filter.getClass().getDeclaredField("userDetailsService");
        userServiceField.setAccessible(true);
        userServiceField.set(filter, detailsService);
    }

    /**
     * Checks correct behaviour for invalid authorization header.
     *
     * @throws ServletException       if errors occur when handling servlets
     * @throws IOException            if errors occur when handling IO
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    @Test
    void invalidHeader() throws ServletException,
            IOException,
            NoSuchFieldException,
            IllegalAccessException {
        final SecurityContext securityContext = getMockedSecurityContext();

        // cannot get header -> null
        when(req.getHeader("Authorization")).thenReturn(null);
        filter.doFilterInternal(req, res, filterChain);

        // no 'Bearer ' prefix
        when(req.getHeader("Authorization")).thenReturn("no_bearer_prefix");
        filter.doFilterInternal(req, res, filterChain);

        verify(filterChain, times(2)).doFilter(req, res);
        verify(securityContext, times(0)).getAuthentication();
        verify(securityContext, times(0)).setAuthentication(any());
    }

    /**
     * Checks correct behaviour for expired tokens.
     *
     * @throws ServletException       if errors occur when handling servlets
     * @throws IOException            if errors occur when handling IO
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    @Test
    void expiredToken() throws ServletException,
            IOException,
            NoSuchFieldException,
            IllegalAccessException {
        // setup
        final SecurityContext securityContext = getMockedSecurityContext();
        String token = "invalid_token123";
        validHeader(token);

        // 'token' is expired
        when(utils.getNetIdFromToken(token)).thenThrow(ExpiredJwtException.class);
        filter.doFilterInternal(req, res, filterChain);

        verify(filterChain, times(1)).doFilter(req, res);
        verify(securityContext, times(0)).getAuthentication();
        verify(securityContext, times(0)).setAuthentication(any());
    }

    /**
     * Checks correct behaviour for tokens tha contain illegal arguments.
     *
     * @throws ServletException       if errors occur when handling servlets
     * @throws IOException            if errors occur when handling IO
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    @Test
    void illegalArgumentsTokens() throws ServletException,
            IOException,
            NoSuchFieldException,
            IllegalAccessException {
        // setup
        final SecurityContext securityContext = getMockedSecurityContext();
        String token = "invalid_token123";
        validHeader(token);

        // 'token' is not a token
        when(utils.getNetIdFromToken(token)).thenThrow(IllegalArgumentException.class);
        filter.doFilterInternal(req, res, filterChain);

        verify(filterChain, times(1)).doFilter(req, res);
        verify(securityContext, times(0)).getAuthentication();
        verify(securityContext, times(0)).setAuthentication(any());
    }

    /**
     * Checks correct behaviour for already authenticated requests.
     *
     * @throws ServletException       if errors occur when handling servlets
     * @throws IOException            if errors occur when handling IO
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    @Test
    void alreadyAuthenticated() throws NoSuchFieldException,
            IllegalAccessException,
            ServletException,
            IOException {

        // set up
        String token = "valid_token123";
        String netId = "jSnow";
        validHeader(token);
        fetchNetIdSetUp(netId);
        final SecurityContext securityContext = getMockedSecurityContext();

        // set up already authenticated
        validAuthentication(securityContext);

        filter.doFilterInternal(req, res, filterChain);
        verify(filterChain, times(1)).doFilter(req, res);
        verify(detailsService, times(0)).loadUserByUsername(any());
    }

    /**
     * Checks correct behaviour when token gets invalidated.
     *
     * @throws ServletException       if errors occur when handling servlets
     * @throws IOException            if errors occur when handling IO
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    @Test
    void invalidatedToken() throws NoSuchFieldException,
            IllegalAccessException,
            IOException,
            ServletException {
        // set up
        String token = "token123";
        String netId = "jSnow";
        final SecurityContext securityContext = getMockedSecurityContext();
        validHeader(token);
        fetchNetIdSetUp(netId);
        // mock user details service
        UserDetails userDetails = new UserDetailsImpl(new User(netId, "pass", 0));
        when(detailsService.loadUserByUsername(netId)).thenReturn(userDetails);

        // not yet authenticated
        when(securityContext.getAuthentication()).thenReturn(null);

        // invalidated token
        when(utils.validateToken(token, userDetails)).thenReturn(false);

        filter.doFilterInternal(req, res, filterChain);
        verify(filterChain, times(1)).doFilter(req, res);
        verify(securityContext, times(0)).setAuthentication(any());
    }

    /**
     * Checks correct behaviour when token gets validated.
     *
     * @throws ServletException       if errors occur when handling servlets
     * @throws IOException            if errors occur when handling IO
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    @Test
    void validatedToken() throws NoSuchFieldException,
            IllegalAccessException,
            ServletException,
            IOException {
        // set up
        String token = "token123";
        String netId = "jSnow";
        final SecurityContext securityContext = getMockedSecurityContext();
        validHeader(token);
        fetchNetIdSetUp(netId);
        // mock user details service
        UserDetails userDetails = new UserDetailsImpl(new User(netId, "pass", 0));
        when(detailsService.loadUserByUsername(netId)).thenReturn(userDetails);

        // validated token
        when(utils.validateToken(token, userDetails)).thenReturn(true);

        filter.doFilterInternal(req, res, filterChain);

        // check if authentication has been set
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        verify(securityContext, times(1))
                .setAuthentication(authToken);
        verify(filterChain, times(1)).doFilter(req, res);
    }

    /**
     * Test helper method for mocking and injecting security context field.
     *
     * @return a mocked security context
     * @throws NoSuchFieldException   if field does not exist
     * @throws IllegalAccessException if access modifiers block access
     */
    private SecurityContext getMockedSecurityContext()
            throws NoSuchFieldException, IllegalAccessException {
        // mock the security context of Spring Security
        final SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        // insert mock into security context field
        Field securityContextField = filter.getClass().getDeclaredField("securityContext");
        securityContextField.setAccessible(true);
        securityContextField.set(filter, securityContext);

        return securityContext;
    }

    /**
     * Test helper method to make mocked Http request return a valid header.
     *
     * @param token the token
     */
    private void validHeader(String token) {
        when(req.getHeader("Authorization")).thenReturn("Bearer " + token);
    }

    /**
     * Test helper method to make mocked token utility class return a netId.
     *
     * @param netId the netId that needs to be returned
     */
    private void fetchNetIdSetUp(String netId) {
        when(utils.getNetIdFromToken(anyString())).thenReturn(netId);
    }

    /**
     * Test helper method to create an already authenticated environment.
     *
     * @param securityContext security context to modify
     */
    private void validAuthentication(final SecurityContext securityContext) {
        // dummy authentication
        Authentication dummyAuth = Mockito.mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(dummyAuth);
    }
}
