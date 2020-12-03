package nl.tudelft.sem10.authenticationservice.domain;

import io.jsonwebtoken.ExpiredJwtException;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
    @Mock
    private transient SecurityContextHolder securityContextHolder;

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

    @Test
    void invalidHeader() throws ServletException, IOException {
        // cannot get header -> null
        when(req.getHeader("Authorization")).thenReturn(null);
        filter.doFilterInternal(req, res, filterChain);

        // no 'Bearer ' prefix
        when(req.getHeader("Authorization")).thenReturn("no_bearer_prefix");
        filter.doFilterInternal(req, res, filterChain);


        verify(filterChain, times(2)).doFilter(req, res);
    }

    @Test
    void invalidToken() throws ServletException, IOException {
        String token = "this_is_an_invalid_token";
        when(req.getHeader("Authorization")).thenReturn("Bearer " + token);

        // 'token' is expired
        when(utils.getNetIdFromToken(token)).thenThrow(ExpiredJwtException.class);
        filter.doFilterInternal(req, res, filterChain);

        // reset mocked utils
        utils = Mockito.mock(JwtTokenUtil.class);

        // 'token' it's not a token
        when(utils.getNetIdFromToken(token)).thenThrow(IllegalArgumentException.class);
        filter.doFilterInternal(req, res, filterChain);

        verify(filterChain, times(2)).doFilter(req, res);
    }

    @Test
    void alreadyAuthenticated() throws NoSuchFieldException,
            IllegalAccessException,
            ServletException,
            IOException {
        // mock the security context of Spring Security
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        // insert mock into security context field
        Field securityContextField = filter.getClass().getDeclaredField("securityContext");
        securityContextField.setAccessible(true);
        securityContextField.set(filter, securityContext);

        // dummy authentication object
        Authentication dummyAuth = Mockito.mock(Authentication.class);

        // return dummy when asked for authentication object
        Mockito.when(securityContext.getAuthentication()).thenReturn(dummyAuth);

        filter.doFilterInternal(req, res, filterChain);
        verify(filterChain, times(1)).doFilter(req, res);
    }
}