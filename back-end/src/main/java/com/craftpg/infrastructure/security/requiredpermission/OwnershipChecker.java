package com.craftpg.infrastructure.security.requiredpermission;

import com.craftpg.application.usecase.SecuredUseCase;
import com.craftpg.domain.BaseSecureDomain;
import com.craftpg.infrastructure.security.currentuser.CurrentUserProvider;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("ownershipChecker")
@RequiredArgsConstructor
public class OwnershipChecker {

    private final EntityManager entityManager;
    private final CurrentUserProvider currentUserProvider;

    public boolean checkOwnership(Object useCase, UUID id) {
        if (!(useCase instanceof SecuredUseCase<?, ?, ?, ?, ?, ?> secured)) return true;
        if (!secured.requiresOwnership()) return true; //disable

        Object entity = entityManager.find(secured.getEntityClass(), id);

        if (!(entity instanceof BaseSecureDomain<?> baseSecureDomain)) return false;

        UUID userId = currentUserProvider.getCurrentUserId();
        return userId.equals(baseSecureDomain.getOwnerUserId());
    }

}
