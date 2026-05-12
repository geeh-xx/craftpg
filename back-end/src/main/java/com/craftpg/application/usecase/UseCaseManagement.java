package com.craftpg.application.usecase;


import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;

public interface UseCaseManagement<RES, OUT extends OperationResult<RES>, CI, UI, PI, ID> {

    OUT create(final CI input);

    OUT update(final UI input);

    OUT findById(final ID id);

    OperationResult<Page<RES>> findAll(@Nonnull final PI pageable);

    OperationResult<Void> delete(final ID id);
}
