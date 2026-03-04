package com.example.loantraking.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("system");
            }

            // Prefer email from JWT claims for resource-server auth.
            String jwtEmail = extractEmailFromJwt(authentication);
            if (jwtEmail != null) {
                return Optional.of(jwtEmail);
            }

            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return Optional.ofNullable(((UserDetails) principal).getUsername());
            }
            if (principal instanceof String) {
                return Optional.ofNullable((String) principal);
            }
            // try to reflectively read getEmail() or getUsername()
            try {
                Method m = principal.getClass().getMethod("getEmail");
                Object email = m.invoke(principal);
                if (email != null) return Optional.of(email.toString());
            } catch (NoSuchMethodException ignored) {}
            try {
                Method m = principal.getClass().getMethod("getUsername");
                Object uname = m.invoke(principal);
                if (uname != null) return Optional.of(uname.toString());
            } catch (NoSuchMethodException ignored) {}

            return Optional.of("system");
        } catch (Exception ex) {
            return Optional.of("system");
        }
    }

    private String extractEmailFromJwt(Authentication authentication) {
        Jwt jwt = null;
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            jwt = jwtAuth.getToken();
        } else if (authentication.getPrincipal() instanceof Jwt principalJwt) {
            jwt = principalJwt;
        }

        if (jwt == null) {
            return null;
        }

        Object claim = jwt.getClaims().get("email");
        if (claim == null) claim = jwt.getClaims().get("preferred_username");
        if (claim == null) claim = jwt.getClaims().get("upn");
        if (claim == null) claim = jwt.getClaims().get("sub");

        return claim instanceof String value ? value : null;
    }
}
