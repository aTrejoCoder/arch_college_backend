package microservice.common_classes.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import microservice.common_classes.Utils.Response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTSecurity extends OncePerRequestFilter {

    private final SecretKey secretKey;

    @Autowired
    public JWTSecurity(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = extractToken(request);
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/v1/api/public/")) {
            chain.doFilter(request, response);
            return;
        }

        if (token != null && validateToken(token).isSuccess()) {
            Result<Claims> claimsResult = validateToken(token);
            if (claimsResult.isSuccess()) {
                Claims claims = claimsResult.getData();

                String username = getAccountNumber(claims);
                List<String> roles = getRoles(claims);

                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }




    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    public String generateToken(Long userId, String username, List<String> roles) {
        Claims claims = Jwts.claims();

        // Prefix each role with "ROLE_" individually
        List<String> prefixedRoles = roles.stream()
                .map(role -> "ROLE_" + role)
                .collect(Collectors.toList());

        claims.put("roles", prefixedRoles);
        claims.put("userId", userId);
        claims.put("username", username);

        Date now = new Date();
        long validityDuration = 3600000; // 1 hour
        Date validity = new Date(now.getTime() + validityDuration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserId(Claims claims) {
        return claims.get("userId", Long.class);
    }

    public String getAccountNumber(Claims claims) {
        return claims.get("username", String.class);
    }

    public Result<Claims> validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Result.success(claims);
        } catch (ExpiredJwtException e) {
            return Result.error("Token expired at " + e.getClaims().getExpiration());
        } catch (SignatureException e) {
            return Result.error("Invalid Token");
        } catch (Exception e) {
            return Result.error("Token parsing error");
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }

    public Result<Claims> getClaimsFromToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7); // Remove "Bearer " prefix

            Result<Claims> claimsResult = validateToken(token);
            if (!claimsResult.isSuccess()) {
                return Result.error(claimsResult.getErrorMessage());
            }

            return Result.success(claimsResult.getData());
        }
        return Result.error("Invalid Header Format");
    }

    public Result<Long> getUserIdFromToken(HttpServletRequest request) {
        Result<Claims> claimsResult = getClaimsFromToken(request);
        if (!claimsResult.isSuccess()) {
            return Result.error(claimsResult.getErrorMessage());
        }
        return Result.success(getUserId(claimsResult.getData()));
    }

    public String getAccountNumberFromToken(HttpServletRequest request) {
        Result<Claims> claimsResult = getClaimsFromToken(request);
        if (!claimsResult.isSuccess()) {
            throw new RuntimeException(claimsResult.getErrorMessage());
        }
        return getAccountNumber(claimsResult.getData());
    }

    public Result<List<String>> getRolesFromToken(HttpServletRequest request) {
        Result<Claims> claimsResult = getClaimsFromToken(request);
        if (!claimsResult.isSuccess()) {
            return Result.error(claimsResult.getErrorMessage());
        }
        return Result.success(getRoles(claimsResult.getData()));
    }
}