package com.craftpg.application.usecase;

import com.craftpg.domain.BaseSecureDomain;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

public interface SecuredUseCase<E extends BaseSecureDomain<ID>, RES, CI, UI, PI, ID> {
    OperationResult<RES> create(CI input);

    @PreAuthorize("@ownershipChecker.checkOwnership(this, #id)")
    OperationResult<RES> update(UI input);

    @PreAuthorize("@ownershipChecker.checkOwnership(this, #id)")
    OperationResult<RES> findById(ID id);

    OperationResult<Page<RES>> findAll(PI pageable);

    @PreAuthorize("@ownershipChecker.checkOwnership(this, #id)")
    OperationResult<Void> delete(ID id);

    Class<E> getEntityClass();

    default boolean requiresOwnership() {
        return true;
    }

}
