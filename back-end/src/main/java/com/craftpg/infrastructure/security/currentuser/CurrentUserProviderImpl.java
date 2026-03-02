package com.craftpg.infrastructure.security.currentuser;

import com.craftpg.infrastructure.exception.ApiException;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserProviderImpl implements CurrentUserProvider {

    @Override
    public UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ApiException("unauthorized");
        }

        var principal = authentication.getPrincipal();
        if (!(principal instanceof Jwt jwt)) {
            throw new ApiException("unauthorized");
        }

        try {
            return UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException ex) {
            throw new ApiException("unauthorized");
        }
    }
}
