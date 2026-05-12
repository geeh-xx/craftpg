package com.craftpg.infrastructure.persistence.repository;

import com.craftpg.domain.model.Character.CharacterBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CharacterBaseRepository extends JpaRepository<CharacterBase, UUID> {

    List<CharacterBase> findByOwnerUserId(final UUID ownerUserId);

    Optional<CharacterBase> findByIdAndOwnerUserId(final UUID id, final UUID ownerUserId);

    Page<CharacterBase> findPageByOwnerUserId(final UUID ownerUserId, final Pageable pageable);

    boolean existsByIdAndOwnerUserId(final UUID id, final UUID ownerUserId);
}
