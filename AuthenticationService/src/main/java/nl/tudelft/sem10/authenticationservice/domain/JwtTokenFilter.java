package nl.tudelft.sem10.authenticationservice.domain;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Spring Security filter placed in filter chain before authorizing access.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final transient SecurityContext securityContext =
            SecurityContextHolder.getContext();

    // use of transient for PMD
    @Autowired
    private transient JwtTokenUtil jwtTokenUtil;
    @Autowired
    private transient UserDetailsService userDetailsService;

    /**
     * Checks presence and validity of token and sets details.
     *
     * @param request     http request
     * @param response    http response
     * @param filterChain the chain of Spring Security filters
     * @throws ServletException if errors occur when handling servlets
     * @throws IOException      if errors occur when handling IO
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // get content of 'Authorization' header
        final String requestTokenHeader = request.getHeader("Authorization");

        // check token presence and validity
        if (!isValidToken(requestTokenHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT token is prepended by "Bearer " so remove it to keep only the token
        String token = requestTokenHeader.substring(7);
        // get netId from token
        String netId = fetchNetId(token);

        // if netId fetch was successful and no authentication has occurred yet
        if (netId != null && securityContext.getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(netId);

            // check if token matches the user
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                // set authentication
                setUsernamePasswordAuthenticationToken(userDetails, request);
            }
        }
        // continue through rest of filters
        filterChain.doFilter(request, response);
    }

    /**
     * Checks presence of a JWT token.
     *
     * @param tokenHeader Authorization header in request
     * @return true if present, false otherwise
     */
    private boolean isValidToken(final String tokenHeader) {
        if (tokenHeader == null) {
            return false;
        } else if (!tokenHeader.startsWith("Bearer ")) {
            System.out.println("JWT token is not prepended by 'Bearer ' string");
            return false;
        }
        return true;
    }

    /**
     * Gets the netId from the token.
     *
     * @param token token from which we need the matching netId
     * @return netId or null if problems occurred
     */
    private String fetchNetId(final String token) {
        try {
            return jwtTokenUtil.getNetIdFromToken(token);
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to retrieve JWT token");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token has expired");
        }
        return null;
    }

    /**
     * Set authentication details in the security context of the application.
     *
     * @param userDetails details of the user
     * @param request     the HTTP request
     */
    private void setUsernamePasswordAuthenticationToken(final UserDetails userDetails,
                                                        final HttpServletRequest request) {
        // create authentication token with user details and authorities (role)
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        // set details from http request
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // set authentication
        securityContext.setAuthentication(authToken);
    }
}