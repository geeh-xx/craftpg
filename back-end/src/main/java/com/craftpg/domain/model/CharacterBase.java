package com.craftpg.domain.model;

import com.craftpg.domain.input.CreateCharacterBaseInput;
import lombok.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "character_base")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterBase {

    @Id
    private UUID id;

    @Column(name = "owner_user_id", nullable = false)
    private UUID ownerUserId;

    @Column(nullable = false)
    private String name;

    private String race;

    @Column(name = "clazz")
    private String clazz;

    @Column(name = "attributes_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String attributesJson;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static CharacterBase create(@NonNull final CreateCharacterBaseInput input) {
        var character = new CharacterBase();
        character.id = UUID.randomUUID();
        character.ownerUserId = input.ownerUserId();
        character.name = input.name();
        character.race = input.race();
        character.clazz = input.clazz();
        character.attributesJson = input.attributesJson();
        character.createdAt = LocalDateTime.now();
        return character;
    }
}
