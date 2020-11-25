package nl.tudelft.sem10.authenticationservice.domain;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Spring Security filter before authorizing access.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

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
     * @throws ServletException overridden from super class
     * @throws IOException      overridden from super class
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        // JWT token is prepended by "Bearer " so remove it to keep only the token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String token = requestTokenHeader.substring(7);
            try {
                String netId = jwtTokenUtil.getNetIdFromToken(token);

                if (netId != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(netId);

                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to retrieve JWT token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT token has expired");
            }
        } else {
            System.out.println("JWT token is not prepended by 'Bearer ' string");
        }

        filterChain.doFilter(request, response);
    }
}