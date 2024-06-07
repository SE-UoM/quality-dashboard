package gr.uom.strategicplanning.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.uom.strategicplanning.utils.TokenUtil;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private static final Set<String> ALLOWED_PATHS = new HashSet<>();

    static {
        ALLOWED_PATHS.add("/login");
        ALLOWED_PATHS.add("/api/user/register");
        ALLOWED_PATHS.add("/api/user/verify");
        ALLOWED_PATHS.add("/api/user/verify/resend");
        ALLOWED_PATHS.add("/user/token/refresh");
        ALLOWED_PATHS.add("/swagger-ui");
        ALLOWED_PATHS.add("/api-ui");
        ALLOWED_PATHS.add("/api/organizations/names");
        ALLOWED_PATHS.add("/api/user/reset-password/request");
        ALLOWED_PATHS.add("/api/user/reset-password");
        ALLOWED_PATHS.add("/api/best-practices");
        ALLOWED_PATHS.add("/api/best-practices/random");
        ALLOWED_PATHS.add("/api/organizations");
        ALLOWED_PATHS.add("/api/projects/pending/total/org");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        System.out.println("servletPath: "+servletPath);
        System.out.println("request: "+request);

        if(isAllowedPath(servletPath)){ filterChain.doFilter(request,response); }

        else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try {
                    DecodedJWT decodedJWT = TokenUtil.getDecodedJWTfromToken(authorizationHeader);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(r ->{
                        authorities.add(new SimpleGrantedAuthority(r));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);
                }
                catch (Exception e){
                    System.err.println("Error with token: "+e.getMessage());
                    response.setHeader("error",e.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    //response.sendError(FORBIDDEN.value());
                    Map<String,String> error= new HashMap<>();
                    error.put("error_message", e.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            }
            else {
                filterChain.doFilter(request,response);
            }
        }
    }

    private boolean isAllowedPath(String path){
        return ALLOWED_PATHS.contains(path);
    }
}
