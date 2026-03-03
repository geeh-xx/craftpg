package com.craftpg.infrastructure.persistence.repository;

import org.jspecify.annotations.NonNull;

import com.craftpg.domain.model.CharacterBase;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterBaseRepository extends JpaRepository<CharacterBase, UUID> {

    List<CharacterBase> findByOwnerUserId(@NonNull final UUID ownerUserId);
}
