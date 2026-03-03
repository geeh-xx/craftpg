package com.craftpg.infrastructure.security.currentuser;

import com.craftpg.domain.model.AppUser;
import com.craftpg.infrastructure.persistence.repository.AppUserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserSyncService {

    private final AppUserRepository appUserRepository;

    public Optional<AppUser> syncFromSecurityContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            return Optional.empty();
        }

        return Optional.of(syncFromJwt(jwt));
    }

    public AppUser syncFromJwt(final Jwt jwt) {
        UUID userId;
        try {
            userId = UUID.fromString(jwt.getSubject());
        } catch (Exception ex) {
            throw new IllegalArgumentException("invalid jwt subject", ex);
        }

        var email = jwt.getClaimAsString("email");
        if (email == null) {
            throw new IllegalArgumentException("jwt has no usable email claim");
        }

        var displayName = firstNonBlank(
            jwt.getClaimAsString("name"),
            jwt.getClaimAsString("preferred_username"),
            jwt.getClaimAsString("given_name"),
            email
        );

        return appUserRepository
            .findById(userId)
            .orElseGet(() -> appUserRepository.save(AppUser.create(userId, email, displayName)));
    }

    private String firstNonBlank(final String... values) {
        for (var value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "unknown";
    }
}
